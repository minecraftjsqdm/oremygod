package com.homemine.oremygod;

import com.homemine.oremygod.compat.ManaSynergy;
import com.homemine.oremygod.loot.ModLootFunctions;
import com.homemine.oremygod.loot.ModLootModifiers;
import com.homemine.oremygod.registry.ModArmorMaterials;
import com.homemine.oremygod.registry.ModBlocks;
import com.homemine.oremygod.registry.ModCreativeTabs;
import com.homemine.oremygod.registry.ModFluids;
import com.homemine.oremygod.registry.ModItems;
import com.homemine.oremygod.world.ModBiomeModifierSerializers;
import com.homemine.oremygod.world.ModPlacementModifierTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(OreMyGodMod.MODID)
public class OreMyGodMod {
    public static final String MODID = "oremygod";

    public OreMyGodMod(IEventBus modBus) {
        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModFluids.FLUID_TYPES.register(modBus);
        ModFluids.FLUIDS.register(modBus);
        ModFluids.FLUID_BLOCKS.register(modBus);
        ModArmorMaterials.ARMOR_MATERIALS.register(modBus);
        ModPlacementModifierTypes.PLACEMENT_MODIFIERS.register(modBus);
        ModBiomeModifierSerializers.BIOME_MODIFIER_SERIALIZERS.register(modBus);
        ModLootModifiers.GLOBAL_LOOT_MODIFIERS.register(modBus);
        ModLootFunctions.LOOT_FUNCTIONS.register(modBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modBus);

        modBus.addListener(ModCreativeTabs::onBuildContents);

        // PMW 软依赖：铸件台浇筑升级（把嫦娥玉石装备的全部数据组件继承给末世水晶装备，并剥除绑定诅咒）。
        // 用 isLoaded 守卫包住方法引用 —— PMW 缺失时 FinalCrystalCastingHandler 不会被加载，
        // 因此可以放心在它的方法签名里直接引用 PMW 的事件类型。
        if (net.neoforged.fml.ModList.get().isLoaded("productivemetalworks")) {
            modBus.addListener(com.homemine.oremygod.compat.FinalCrystalCastingHandler::onCasting);
            // 寂静装备路线：浇筑熔融锻造水晶时，把装备里所有嫦娥玉石部件升级成锻造水晶部件。
            // 同样只在本模组事件监听器里用方法引用，两个模组缺任一都不会加载该类。
            if (net.neoforged.fml.ModList.get().isLoaded("silentgear")) {
                modBus.addListener(com.homemine.oremygod.compat.SilentGearCastingHandler::onCasting);
            }
        }

        // 绑定诅咒守卫（不依赖任何外部模组）：铁砧入口拦下 + 背包周期兜底剥离。
        NeoForge.EVENT_BUS.addListener(com.homemine.oremygod.compat.FinalCrystalCurseGuard::onAnvilUpdate);
        NeoForge.EVENT_BUS.addListener(com.homemine.oremygod.compat.FinalCrystalCurseGuard::onPlayerTick);

        // Mekanism 软依赖：5x 产线的 dirty/clean slurry（化学注册表非数据包，必须 Java 注册）。
        if (net.neoforged.fml.ModList.get().isLoaded("mekanism")) {
            com.homemine.oremygod.registry.ModChemicals.CHEMICALS.register(modBus);
        }

        // Ad Astra 软依赖：嫦娥玉石 / 末世水晶头盔自带太空呼吸。
        // EntityOxygenEvent 是 Ad Astra 的静态注册函数式接口（不是 NeoForge 事件），
        // 用 isLoaded 守卫包住方法引用，Ad Astra 缺失时本类不会被加载。
        if (net.neoforged.fml.ModList.get().isLoaded("ad_astra")) {
            com.homemine.oremygod.compat.AdAstraOxygenHandler.register();
        }

        // Game bus: dynamic equipment attribute bonuses (soft mana synergy).
        NeoForge.EVENT_BUS.addListener(ManaSynergy::onItemAttributeModifiers);
    }
}
