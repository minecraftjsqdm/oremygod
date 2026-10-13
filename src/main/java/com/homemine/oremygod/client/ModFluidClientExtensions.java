package com.homemine.oremygod.client;

import com.homemine.oremygod.OreMyGodMod;
import com.homemine.oremygod.registry.ModFluids;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

/**
 * 4 种末世水晶流体的<b>客户端渲染</b>注册（规格书第 3.3 节）。
 *
 * <p>贴图已换成本模组自有资源 {@code oremygod:block/<流体名>_still} 与 {@code _flow}，
 * 颜色直接烘焙在贴图里（亮红 / 污浊暗红 / 助剂琥珀 / 成品炽白红），因此 tint 取纯白不再叠色。
 * 没有这一段的话，流体在世界里会用缺失贴图渲染。</p>
 *
 * <p>本类只在客户端加载（{@code value = Dist.CLIENT}），服务端不会碰它。</p>
 */
@EventBusSubscriber(modid = OreMyGodMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ModFluidClientExtensions {

    private ModFluidClientExtensions() {
    }

    /** 无 tint：贴图本身已带颜色，纯白表示不做乘法叠色。 */
    private static final int NO_TINT = 0xFFFFFFFF;

    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        // 熔融末世水晶：亮红，最高自发光
        event.registerFluidType(fluid("molten_final_crystal"),
                ModFluids.MOLTEN_FINAL_CRYSTAL_TYPE.value());
        // 污浊熔融末世水晶：暗红去饱和，表达「待净化」
        event.registerFluidType(fluid("impure_molten_final_crystal"),
                ModFluids.IMPURE_MOLTEN_FINAL_CRYSTAL_TYPE.value());
        // 锻造助剂：琥珀金褐（下界合金 + 末地陨铁的合金感）
        event.registerFluidType(fluid("molten_forging_additive"),
                ModFluids.MOLTEN_FORGING_ADDITIVE_TYPE.value());
        // 熔融锻造水晶：最亮的成品级炽红
        event.registerFluidType(fluid("molten_forged_crystal"),
                ModFluids.MOLTEN_FORGED_CRYSTAL_TYPE.value());
        // 熔融嫦娥玉石：玉石青绿（2026-10-03 新增）
        event.registerFluidType(fluid("molten_change_jade"),
                ModFluids.MOLTEN_CHANGE_JADE_TYPE.value());
        // 熔融末地陨铁：暗紫钢灰（2026-10-03 新增）
        event.registerFluidType(fluid("molten_end_meteorite"),
                ModFluids.MOLTEN_END_METEORITE_TYPE.value());
    }

    private static IClientFluidTypeExtensions fluid(String name) {
        ResourceLocation still = ResourceLocation.fromNamespaceAndPath(
                OreMyGodMod.MODID, "block/" + name + "_still");
        ResourceLocation flow = ResourceLocation.fromNamespaceAndPath(
                OreMyGodMod.MODID, "block/" + name + "_flow");
        return new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return still;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return flow;
            }

            @Override
            public int getTintColor() {
                return NO_TINT;
            }
        };
    }
}
