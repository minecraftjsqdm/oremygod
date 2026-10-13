package com.homemine.oremygod.compat;

import com.homemine.oremygod.registry.ModFluids;
import com.homemine.oremygod.registry.ModItems;
import cy.jdkdigital.productivemetalworks.event.CastingRecipeEvent;
import cy.jdkdigital.productivemetalworks.recipe.ItemCastingRecipe;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

/**
 * 铸件台浇筑升级：<b>熔融锻造水晶 + 嫦娥玉石装备 → 末世水晶装备</b>，并让产物
 * <b>继承原装备的全部数据组件</b>（附魔、自定义名、属性修饰符…），同时剥除绑定诅咒。
 *
 * <p>为什么必须挂事件而不能只写数据包配方：Productive Metalworks 的 {@code item_casting} 产物是
 * 一个固定的 ItemStack，数据包无法把「被消耗的 cast 身上那些组件」搬到产物上。而
 * {@link CastingRecipeEvent} 是 <b>mod 总线事件</b>，在 datapack 查询<b>之前</b>触发，结果非空即短路，
 * 因此我在这里直接给出带组件的配方，优先级高于数据包。</p>
 *
 * <p><b>两个必须遵守的约束</b>（来自对 PMW 字节码的核对）：</p>
 * <ol>
 *   <li>PMW 会按 {@code (cast 物品+全部数据组件, fluid, table/basin)} 缓存事件结果，命中缓存不再派发
 *       —— 好处是不同附魔/命名的装备会各触发一次（组件继承才可行），代价是<b>本方法必须是纯函数</b>，
 *       绝不能依赖坐标、时间等外部状态。</li>
 *   <li>本类只在 PMW 存在时才被加载（注册处有 {@code ModList.isLoaded} 守卫），
 *       因此可以直接引用 PMW 的类而不会在缺失时抛 {@code NoClassDefFoundError}。</li>
 * </ol>
 *
 * <p>注意：本模组的用例是「cast <b>被消耗</b>且要继承组件」，所以不要照搬「消耗型模具无需继承」的常规判断。</p>
 */
public final class FinalCrystalCastingHandler {

    private FinalCrystalCastingHandler() {
    }

    /** 每件嫦娥玉石装备 → 对应的末世水晶装备。延迟构造：注册表此时才已填充完毕。 */
    private static Map<Item, Item> upgrades() {
        return Map.ofEntries(
                Map.entry(ModItems.CHANGE_JADE_SWORD.get(), ModItems.FINAL_CRYSTAL_SWORD.get()),
                Map.entry(ModItems.CHANGE_JADE_PICKAXE.get(), ModItems.FINAL_CRYSTAL_PICKAXE.get()),
                Map.entry(ModItems.CHANGE_JADE_AXE.get(), ModItems.FINAL_CRYSTAL_AXE.get()),
                Map.entry(ModItems.CHANGE_JADE_SHOVEL.get(), ModItems.FINAL_CRYSTAL_SHOVEL.get()),
                Map.entry(ModItems.CHANGE_JADE_HOE.get(), ModItems.FINAL_CRYSTAL_HOE.get()),
                Map.entry(ModItems.CHANGE_JADE_HELMET.get(), ModItems.FINAL_CRYSTAL_HELMET.get()),
                Map.entry(ModItems.CHANGE_JADE_CHESTPLATE.get(), ModItems.FINAL_CRYSTAL_CHESTPLATE.get()),
                Map.entry(ModItems.CHANGE_JADE_LEGGINGS.get(), ModItems.FINAL_CRYSTAL_LEGGINGS.get()),
                Map.entry(ModItems.CHANGE_JADE_BOOTS.get(), ModItems.FINAL_CRYSTAL_BOOTS.get()));
    }

    /**
     * 单次浇筑所需熔融锻造水晶量（mb）。
     *
     * <p><b>必须与数据包配方 {@code data/oremygod/recipe/productivemetalworks/item_casting/*.json}
     * 里的 {@code fluid.amount} 保持一致。</b>原因：本事件在数据包查询之前短路，所以玩家实际消耗的是
     * 这个常量；而 JEI 读的是配方管理器里的数据包配方（事件结果不进 JEI）。两处一旦不一致，
     * JEI 就会撒谎 —— 曾经这里是 1000、数据包是 90，就是这个问题。</p>
     */
    private static final int FORGED_CRYSTAL_COST = 1000;

    public static void onCasting(CastingRecipeEvent event) {
        ItemStack cast = event.cast;
        if (cast.isEmpty()) {
            return;
        }
        // 只处理「熔融锻造水晶」这一种流体
        if (event.fluid.getFluid() != ModFluids.MOLTEN_FORGED_CRYSTAL.get()) {
            return;
        }
        Item target = upgrades().get(cast.getItem());
        if (target == null) {
            return;
        }

        ItemStack result = new ItemStack(target, 1);
        // 继承原装备的全部数据组件（附魔 / 自定义名 / 属性修饰符 / 耐久 …）
        result.applyComponents(cast.getComponents());
        // 末世水晶装备不可破坏：清掉耐久相关，避免继承出「已损坏」的装备
        result.remove(DataComponents.DAMAGE);
        // 免疫绑定诅咒：升级时剥除
        stripBindingCurse(result, event.level.registryAccess());

        event.setRecipe(new ItemCastingRecipe(
                Ingredient.of(cast.getItem()),
                SizedFluidIngredient.of(ModFluids.MOLTEN_FORGED_CRYSTAL.get(), FORGED_CRYSTAL_COST),
                result,
                true)); // cast（嫦娥玉石装备）被消耗
    }

    /**
     * 从物品的附魔组件里移除「绑定诅咒」。1.21.1 的附魔是数据组件，需要重建整个
     * {@link ItemEnchantments} —— {@code Mutable.set(holder, 0)} 不保证删除，所以这里只拷贝等级 &gt; 0 的项。
     */
    public static void stripBindingCurse(ItemStack stack, HolderLookup.Provider registries) {
        ItemEnchantments enchantments = stack.get(DataComponents.ENCHANTMENTS);
        if (enchantments == null || enchantments.isEmpty()) {
            return;
        }
        Holder<Enchantment> curse = registries.holderOrThrow(Enchantments.BINDING_CURSE);
        if (enchantments.getLevel(curse) <= 0) {
            return;
        }

        ItemEnchantments.Mutable rebuilt = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        for (Holder<Enchantment> holder : enchantments.keySet()) {
            int level = enchantments.getLevel(holder);
            if (level > 0 && !holder.equals(curse)) {
                rebuilt.set(holder, level);
            }
        }
        stack.set(DataComponents.ENCHANTMENTS, rebuilt.toImmutable());
    }
}
