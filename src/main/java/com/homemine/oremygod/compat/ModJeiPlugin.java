package com.homemine.oremygod.compat;

import com.homemine.oremygod.OreMyGodMod;
import com.homemine.oremygod.registry.ModFluids;
import com.homemine.oremygod.registry.ModItems;
import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * JEI 插件：给<b>嫦娥玉石、锻造水晶及其熔融态</b>加「信息」页（物品/流体上按 R 或 U 时能看到用途与获取链）。
 *
 * <p><b>为什么走 API 而不是语言键</b>：JEI 19.x 里已经<b>没有</b> {@code jei.information.<uid>} 这类语言键
 * （把整个 jar 的常量池搜过，不存在该前缀字符串），信息页必须由模组自己通过
 * {@link IRecipeRegistration#addIngredientInfo(List, mezz.jei.api.ingredients.IIngredientType, Component...)}
 * 注册。物品用 {@link VanillaTypes#ITEM_STACK}，流体用 {@link NeoForgeTypes#FLUID_STACK}。</p>
 *
 * <p>本类由 JEI 的 {@code @JeiPlugin} 注解扫描自动发现，不需要在 {@code OreMyGodMod} 里手动注册；
 * 只用到 JEI 的 API 类型，JEI 缺失时本类不会被加载（JEI 自己负责扫描）。</p>
 */
@JeiPlugin
public class ModJeiPlugin implements IModPlugin {

    private static final ResourceLocation UID =
            ResourceLocation.fromNamespaceAndPath(OreMyGodMod.MODID, "info");

    /** 给流体信息页用的展示量（JEI 只用它做图标渲染，不影响配方）。 */
    private static final int DISPLAY_AMOUNT = 1000;

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // ---- 嫦娥玉石 ----
        Component changeJadeInfo = Component.translatable("jei.oremygod.change_jade.info");
        registration.addIngredientInfo(new ItemStack(ModItems.CHANGE_JADE.get()),
                VanillaTypes.ITEM_STACK, changeJadeInfo);
        registration.addIngredientInfo(new ItemStack(ModItems.CHANGE_JADE_NUGGET.get()),
                VanillaTypes.ITEM_STACK, changeJadeInfo);
        registration.addIngredientInfo(new ItemStack(ModItems.CHANGE_JADE_BLOCK_ITEM.get()),
                VanillaTypes.ITEM_STACK, changeJadeInfo);

        // ---- 锻造水晶 ----
        registration.addIngredientInfo(new ItemStack(ModItems.FORGED_CRYSTAL.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.oremygod.forged_crystal.info"));

        // ---- 熔融嫦娥玉石（源 + 流动都登记，保证 JEI 两种流体条目都有信息）----
        Component moltenJadeInfo = Component.translatable("jei.oremygod.molten_change_jade.info");
        registration.addIngredientInfo(moltenJadeFluids(), NeoForgeTypes.FLUID_STACK, moltenJadeInfo);

        // ---- 熔融锻造水晶 ----
        Component moltenForgedInfo = Component.translatable("jei.oremygod.molten_forged_crystal.info");
        registration.addIngredientInfo(moltenForgedFluids(), NeoForgeTypes.FLUID_STACK, moltenForgedInfo);

        // ---- 对应的桶（玩家更多是拿桶去查）----
        registration.addIngredientInfo(new ItemStack(ModItems.MOLTEN_CHANGE_JADE_BUCKET.get()),
                VanillaTypes.ITEM_STACK, moltenJadeInfo);
        registration.addIngredientInfo(new ItemStack(ModItems.MOLTEN_FORGED_CRYSTAL_BUCKET.get()),
                VanillaTypes.ITEM_STACK, moltenForgedInfo);
    }

    private static List<FluidStack> moltenJadeFluids() {
        return List.of(
                new FluidStack(ModFluids.MOLTEN_CHANGE_JADE.get(), DISPLAY_AMOUNT),
                new FluidStack(ModFluids.MOLTEN_CHANGE_JADE_FLOWING.get(), DISPLAY_AMOUNT));
    }

    private static List<FluidStack> moltenForgedFluids() {
        return List.of(
                new FluidStack(ModFluids.MOLTEN_FORGED_CRYSTAL.get(), DISPLAY_AMOUNT),
                new FluidStack(ModFluids.MOLTEN_FORGED_CRYSTAL_FLOWING.get(), DISPLAY_AMOUNT));
    }
}
