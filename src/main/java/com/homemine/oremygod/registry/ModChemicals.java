package com.homemine.oremygod.registry;

import com.homemine.oremygod.OreMyGodMod;
import mekanism.api.chemical.Chemical;
import mekanism.common.registration.impl.ChemicalDeferredRegister;
import mekanism.common.registration.impl.SlurryRegistryObject;

/**
 * Mekanism 5x 产线所需的 slurry 注册（规格书第 6.4 节「阶段 4」）。
 *
 * <p>Mekanism 的化学物质注册表<b>不是数据包注册表</b>（{@code CHEMICAL_REGISTRY} 是 NeoForge
 * {@code DefaultedRegistry}），所以 5x 线无法纯 JSON 实现 —— 必须在这里注册。
 * 反过来说，2x/3x/4x 那几条（enriching / purifying / injecting / crushing）只用现成的
 * {@code mekanism:oxygen} / {@code hydrogen_chloride} 加我方物品，纯 JSON 即可，不需要本类。</p>
 *
 * <p>{@code registerSlurry} 会按 Mekanism 约定自动生成
 * {@code oremygod:dirty_final_crystal} 与 {@code oremygod:clean_final_crystal} 两个化学物质。</p>
 *
 * <p>本类只在 Mekanism 存在时才被加载（注册处有 {@code ModList.isLoaded} 守卫）。</p>
 */
public final class ModChemicals {

    private ModChemicals() {
    }

    public static final ChemicalDeferredRegister CHEMICALS =
            new ChemicalDeferredRegister(OreMyGodMod.MODID);

    /** 末世水晶的脏/净 slurry 对。tint 取设计稿的紫黑色系。 */
    public static final SlurryRegistryObject<Chemical, Chemical> FINAL_CRYSTAL_SLURRY =
            CHEMICALS.registerSlurry("final_crystal", builder -> builder.tint(0xFF2A1A4A));
}
