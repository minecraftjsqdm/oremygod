package com.homemine.oremygod.compat;

import com.homemine.oremygod.registry.ModFluids;
import cy.jdkdigital.productivemetalworks.event.CastingRecipeEvent;
import cy.jdkdigital.productivemetalworks.recipe.ItemCastingRecipe;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.silentchaos512.gear.api.part.PartList;
import net.silentchaos512.gear.core.component.GearConstructionData;
import net.silentchaos512.gear.gear.material.MaterialInstance;
import net.silentchaos512.gear.gear.part.PartInstance;
import net.silentchaos512.gear.setup.SgDataComponents;
import net.silentchaos512.gear.util.GearData;

/**
 * <b>寂静装备的浇筑升级</b>：把寂静装备里所有「嫦娥玉石」部件换成「锻造水晶」部件。
 *
 * <p>玩法：铸件台/铸造盆放入寂静装备（<b>被消耗</b>）+ 浇 1000 mB 熔融锻造水晶。</p>
 *
 * <h2>为什么必须同时处理三种存放形式</h2>
 * 寂静装备的材质<b>不是</b>都存在同一个组件里（这是第一次实现失败的原因，靠读实机物品组件查出来的）：
 * <ul>
 *   <li><b>成品装备</b>（剑、镐、盔甲…）→ {@code silentgear:construction}
 *       = {@link GearConstructionData}，里面是 {@link PartList}（一串 {@link PartInstance}），
 *       每个部件自带自己的材质列表；成品装备上<b>没有</b> {@code material_list} 组件。</li>
 *   <li><b>复合部件</b>（剑刃、工具杆…）→ {@code silentgear:material_list}（{@code List<MaterialInstance>}）。</li>
 *   <li>单材质物品 → {@code silentgear:material_single}。</li>
 * </ul>
 *
 * <h2>为什么用 codec 往返而不是直接 new</h2>
 * {@code MaterialInstance} / {@code PartInstance} 的材质都包在 {@code DataResource} 里，而
 * {@code DataResource} <b>没有公开工厂方法</b>（只有 get/getNullable/getId）。所以这里把实例编码成 NBT，
 * 递归把里面所有 {@code oremygod:change_jade} 字符串（材质 id 与物品 id 各一处）替换成
 * {@code oremygod:forged_crystal}，再解码回来——好处是<b>材质修饰符（材料分级、星光充能）原样保留</b>，
 * 且不依赖字段名（用的是通用递归替换）。
 *
 * <p>编解码都用 {@code RegistryOps.create(NbtOps.INSTANCE, level.registryAccess())}，
 * 因为实例里含 ItemStack，其组件解析可能需要注册表上下文。</p>
 *
 * <p>另：{@code CastingRecipeEvent} 是 {@code IModBusEvent}（已核实），本类注册在 mod 总线上；
 * 事件在数据包查询之前触发、结果非空即短路，因此动态构成的寂静装备无需（也无法写）数据包配方；
 * PMW 按 {@code (cast 物品+全部组件, fluid, table/basin)} 缓存事件结果，故本方法必须是纯函数。</p>
 */
public final class SilentGearCastingHandler {

    private SilentGearCastingHandler() {
    }

    /** 每件装备浇筑一次所需熔融锻造水晶（mB），与自带装备那条路线保持一致。 */
    private static final int FORGED_CRYSTAL_COST = 1000;

    private static final ResourceLocation CHANGE_JADE =
            ResourceLocation.fromNamespaceAndPath("oremygod", "change_jade");
    private static final ResourceLocation FORGED_CRYSTAL =
            ResourceLocation.fromNamespaceAndPath("oremygod", "forged_crystal");

    public static void onCasting(CastingRecipeEvent event) {
        if (event.fluid.getFluid() != ModFluids.MOLTEN_FORGED_CRYSTAL.get()) {
            return;
        }
        ItemStack cast = event.cast;
        if (cast.isEmpty()) {
            return;
        }

        RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, event.level.registryAccess());
        ItemStack result = upgrade(cast, ops, event);
        if (result.isEmpty()) {
            return; // 不是寂静装备，或里面根本没有嫦娥玉石
        }

        event.setRecipe(new ItemCastingRecipe(
                Ingredient.of(cast.getItem()),
                SizedFluidIngredient.of(ModFluids.MOLTEN_FORGED_CRYSTAL.get(), FORGED_CRYSTAL_COST),
                result,
                true)); // cast（原寂静装备）被消耗
    }

    /** @return 升级后的装备；不适用时返回空栈（表示本次浇筑不由本监听器接管）。 */
    private static ItemStack upgrade(ItemStack cast, RegistryOps<Tag> ops, CastingRecipeEvent event) {
        // ---- 1. 成品装备：silentgear:construction ----
        GearConstructionData construction = cast.get(SgDataComponents.GEAR_CONSTRUCTION.get());
        if (construction != null) {
            List<PartInstance> rebuilt = new ArrayList<>();
            boolean changed = false;
            for (PartInstance part : construction.parts()) {
                PartInstance upgraded = upgradePart(part, ops);
                if (upgraded != null) {
                    rebuilt.add(upgraded);
                    changed = true;
                } else {
                    rebuilt.add(part);
                }
            }
            if (!changed) {
                return ItemStack.EMPTY;
            }
            ItemStack result = cast.copy();
            result.set(SgDataComponents.GEAR_CONSTRUCTION.get(), new GearConstructionData(
                    PartList.of(rebuilt),
                    construction.isExample(),
                    construction.brokenCount(),
                    construction.repairedCount()));
            return finish(result, event);
        }

        // ---- 2. 复合部件：silentgear:material_list ----
        List<MaterialInstance> list = cast.get(SgDataComponents.MATERIAL_LIST.get());
        if (list != null && !list.isEmpty()) {
            List<MaterialInstance> replaced = new ArrayList<>(list.size());
            boolean changed = false;
            for (MaterialInstance instance : list) {
                MaterialInstance upgraded = upgradeMaterial(instance, ops);
                if (upgraded != null) {
                    replaced.add(upgraded);
                    changed = true;
                } else {
                    replaced.add(instance);
                }
            }
            if (!changed) {
                return ItemStack.EMPTY;
            }
            ItemStack result = cast.copy();
            result.set(SgDataComponents.MATERIAL_LIST.get(), List.copyOf(replaced));
            return finish(result, event);
        }

        // ---- 3. 单材质物品：silentgear:material_single ----
        MaterialInstance single = cast.get(SgDataComponents.MATERIAL_SINGLE.get());
        if (single != null) {
            MaterialInstance upgraded = upgradeMaterial(single, ops);
            if (upgraded == null) {
                return ItemStack.EMPTY;
            }
            ItemStack result = cast.copy();
            result.set(SgDataComponents.MATERIAL_SINGLE.get(), upgraded);
            return finish(result, event);
        }

        return ItemStack.EMPTY;
    }

    /** 部件里只要含嫦娥玉石就整体重建；否则返回 null 表示"不用动"。 */
    private static PartInstance upgradePart(PartInstance part, RegistryOps<Tag> ops) {
        if (part == null) {
            return null;
        }
        boolean hasJade = false;
        for (MaterialInstance instance : part.getMaterials()) {
            if (instance != null && CHANGE_JADE.equals(instance.getId())) {
                hasJade = true;
                break;
            }
        }
        if (!hasJade) {
            return null;
        }
        Tag tag = encode(PartInstance.CODEC, part, ops);
        if (tag == null || !replaceJadeInTag(tag)) {
            return null;
        }
        return decode(PartInstance.CODEC, tag, ops);
    }

    /** 单个材质实例：是嫦娥玉石就换掉（保留分级等修饰符），否则返回 null。 */
    private static MaterialInstance upgradeMaterial(MaterialInstance instance, RegistryOps<Tag> ops) {
        if (instance == null || !CHANGE_JADE.equals(instance.getId())) {
            return null;
        }
        Tag tag = encode(MaterialInstance.CODEC, instance, ops);
        if (tag == null || !replaceJadeInTag(tag)) {
            return null;
        }
        return decode(MaterialInstance.CODEC, tag, ops);
    }

    /** 换完材质后重算装备数据，并套用与自带装备路线相同的清理规则。 */
    private static ItemStack finish(ItemStack result, CastingRecipeEvent event) {
        GearData.recalculateGearData(result, null);
        result.remove(DataComponents.DAMAGE);
        FinalCrystalCastingHandler.stripBindingCurse(result, event.level.registryAccess());
        return result;
    }

    /**
     * 递归把 NBT 里所有取值等于 {@code oremygod:change_jade} 的字符串换成 {@code oremygod:forged_crystal}。
     * 材质实例里材质 id 与物品 id 各出现一次，这样一次遍历全部覆盖，且不依赖字段名。
     *
     * @return 是否真的改动了内容
     */
    private static boolean replaceJadeInTag(Tag tag) {
        boolean changed = false;
        if (tag instanceof CompoundTag compound) {
            for (String key : new ArrayList<>(compound.getAllKeys())) {
                Tag child = compound.get(key);
                if (child instanceof StringTag string && isJade(string.getAsString())) {
                    compound.putString(key, FORGED_CRYSTAL.toString());
                    changed = true;
                } else if (child != null && replaceJadeInTag(child)) {
                    changed = true;
                }
            }
        } else if (tag instanceof ListTag list) {
            for (int i = 0; i < list.size(); i++) {
                Tag child = list.get(i);
                if (child instanceof StringTag string && isJade(string.getAsString())) {
                    list.set(i, StringTag.valueOf(FORGED_CRYSTAL.toString()));
                    changed = true;
                } else if (child != null && replaceJadeInTag(child)) {
                    changed = true;
                }
            }
        }
        return changed;
    }

    private static boolean isJade(String value) {
        return CHANGE_JADE.toString().equals(value) || "oremygod:change_jade".equals(value);
    }

    private static <T> Tag encode(com.mojang.serialization.Codec<T> codec, T value, RegistryOps<Tag> ops) {
        return codec.encodeStart(ops, value).result().orElse(null);
    }

    private static <T> T decode(com.mojang.serialization.Codec<T> codec, Tag tag, RegistryOps<Tag> ops) {
        return codec.parse(ops, tag).result().orElse(null);
    }
}
