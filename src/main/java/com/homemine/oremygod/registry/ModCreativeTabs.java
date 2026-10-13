package com.homemine.oremygod.registry;

import com.homemine.oremygod.OreMyGodMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OreMyGodMod.MODID);

    /** Dedicated page listing every item added by this mod. */
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> OREMYGOD_TAB =
            CREATIVE_MODE_TABS.register("oremygod", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.oremygod"))
                    .icon(() -> new ItemStack(ModItems.CHANGE_JADE.get()))
                    .displayItems((params, output) -> {
                        // blocks
                        output.accept(ModItems.END_METEORITE_ORE_ITEM);
                        output.accept(ModItems.END_METEORITE_BLOCK_ITEM);
                        output.accept(ModItems.CHANGE_JADE_ORE_ITEM);
                        output.accept(ModItems.CHANGE_JADE_BLOCK_ITEM);
                        output.accept(ModItems.BUDDING_FINAL_CRYSTAL_ITEM);
                        output.accept(ModItems.FINAL_CRYSTAL_CLUSTER_ITEM);
                        output.accept(ModItems.FINAL_CRYSTAL_BLOCK_ITEM);
                        // materials
                        output.accept(ModItems.END_METEORITE_DEBRIS);
                        output.accept(ModItems.END_METEORITE_SCRAP);
                        output.accept(ModItems.END_METEORITE_INGOT);
                        output.accept(ModItems.END_METEORITE_NUGGET);
                        output.accept(ModItems.CHANGE_JADE);
                        output.accept(ModItems.CHANGE_JADE_NUGGET);
                        // final crystal：获取与加工链（规格书 3.2 / R8）
                        output.accept(ModItems.RAW_FINAL_CRYSTAL);
                        output.accept(ModItems.FINAL_CRYSTAL_BALL);
                        output.accept(ModItems.FORGING_ADDITIVE_INGOT);
                        output.accept(ModItems.FORGED_CRYSTAL);
                        output.accept(ModItems.FINAL_CRYSTAL_DUST);
                        output.accept(ModItems.FINAL_CRYSTAL_CLUMP);
                        output.accept(ModItems.FINAL_CRYSTAL_SHARD);
                        output.accept(ModItems.DIRTY_FINAL_CRYSTAL_DUST);
                        // 流体桶
                        output.accept(ModItems.MOLTEN_FINAL_CRYSTAL_BUCKET);
                        output.accept(ModItems.IMPURE_MOLTEN_FINAL_CRYSTAL_BUCKET);
                        output.accept(ModItems.MOLTEN_FORGING_ADDITIVE_BUCKET);
                        output.accept(ModItems.MOLTEN_FORGED_CRYSTAL_BUCKET);
                        output.accept(ModItems.MOLTEN_CHANGE_JADE_BUCKET);
                        output.accept(ModItems.MOLTEN_END_METEORITE_BUCKET);
                        // smithing templates
                        output.accept(ModItems.END_METEORITE_UPGRADE_TEMPLATE);
                        output.accept(ModItems.CHANGE_JADE_UPGRADE_TEMPLATE);
                        // end meteorite tools & weapons
                        output.accept(ModItems.END_METEORITE_SWORD);
                        output.accept(ModItems.END_METEORITE_PICKAXE);
                        output.accept(ModItems.END_METEORITE_AXE);
                        output.accept(ModItems.END_METEORITE_SHOVEL);
                        output.accept(ModItems.END_METEORITE_HOE);
                        // end meteorite armor
                        output.accept(ModItems.END_METEORITE_HELMET);
                        output.accept(ModItems.END_METEORITE_CHESTPLATE);
                        output.accept(ModItems.END_METEORITE_LEGGINGS);
                        output.accept(ModItems.END_METEORITE_BOOTS);
                        // change jade tools & weapons
                        output.accept(ModItems.CHANGE_JADE_SWORD);
                        output.accept(ModItems.CHANGE_JADE_PICKAXE);
                        output.accept(ModItems.CHANGE_JADE_AXE);
                        output.accept(ModItems.CHANGE_JADE_SHOVEL);
                        output.accept(ModItems.CHANGE_JADE_HOE);
                        // change jade armor
                        output.accept(ModItems.CHANGE_JADE_HELMET);
                        output.accept(ModItems.CHANGE_JADE_CHESTPLATE);
                        output.accept(ModItems.CHANGE_JADE_LEGGINGS);
                        output.accept(ModItems.CHANGE_JADE_BOOTS);
                        // final crystal tools & weapons
                        output.accept(ModItems.FINAL_CRYSTAL_SWORD);
                        output.accept(ModItems.FINAL_CRYSTAL_PICKAXE);
                        output.accept(ModItems.FINAL_CRYSTAL_AXE);
                        output.accept(ModItems.FINAL_CRYSTAL_SHOVEL);
                        output.accept(ModItems.FINAL_CRYSTAL_HOE);
                        // final crystal armor
                        output.accept(ModItems.FINAL_CRYSTAL_HELMET);
                        output.accept(ModItems.FINAL_CRYSTAL_CHESTPLATE);
                        output.accept(ModItems.FINAL_CRYSTAL_LEGGINGS);
                        output.accept(ModItems.FINAL_CRYSTAL_BOOTS);
                    })
                    .build());

    public static void onBuildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModItems.END_METEORITE_ORE_ITEM);
            event.accept(ModItems.END_METEORITE_BLOCK_ITEM);
            event.accept(ModItems.CHANGE_JADE_ORE_ITEM);
            event.accept(ModItems.CHANGE_JADE_BLOCK_ITEM);
            event.accept(ModItems.BUDDING_FINAL_CRYSTAL_ITEM);
            event.accept(ModItems.FINAL_CRYSTAL_CLUSTER_ITEM);
            event.accept(ModItems.FINAL_CRYSTAL_BLOCK_ITEM);
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.END_METEORITE_DEBRIS);
            event.accept(ModItems.END_METEORITE_SCRAP);
            event.accept(ModItems.END_METEORITE_INGOT);
            event.accept(ModItems.END_METEORITE_UPGRADE_TEMPLATE);
            event.accept(ModItems.CHANGE_JADE_UPGRADE_TEMPLATE);
            event.accept(ModItems.CHANGE_JADE);
            event.accept(ModItems.RAW_FINAL_CRYSTAL);
            event.accept(ModItems.FINAL_CRYSTAL_BALL);
            event.accept(ModItems.FORGING_ADDITIVE_INGOT);
            event.accept(ModItems.FORGED_CRYSTAL);
            event.accept(ModItems.FINAL_CRYSTAL_DUST);
            event.accept(ModItems.FINAL_CRYSTAL_CLUMP);
            event.accept(ModItems.FINAL_CRYSTAL_SHARD);
            event.accept(ModItems.DIRTY_FINAL_CRYSTAL_DUST);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.END_METEORITE_PICKAXE);
            event.accept(ModItems.END_METEORITE_AXE);
            event.accept(ModItems.END_METEORITE_SHOVEL);
            event.accept(ModItems.END_METEORITE_HOE);
            event.accept(ModItems.END_METEORITE_HELMET);
            event.accept(ModItems.END_METEORITE_CHESTPLATE);
            event.accept(ModItems.END_METEORITE_LEGGINGS);
            event.accept(ModItems.END_METEORITE_BOOTS);
            event.accept(ModItems.CHANGE_JADE_PICKAXE);
            event.accept(ModItems.CHANGE_JADE_AXE);
            event.accept(ModItems.CHANGE_JADE_SHOVEL);
            event.accept(ModItems.CHANGE_JADE_HOE);
            event.accept(ModItems.CHANGE_JADE_HELMET);
            event.accept(ModItems.CHANGE_JADE_CHESTPLATE);
            event.accept(ModItems.CHANGE_JADE_LEGGINGS);
            event.accept(ModItems.CHANGE_JADE_BOOTS);
            event.accept(ModItems.FINAL_CRYSTAL_PICKAXE);
            event.accept(ModItems.FINAL_CRYSTAL_AXE);
            event.accept(ModItems.FINAL_CRYSTAL_SHOVEL);
            event.accept(ModItems.FINAL_CRYSTAL_HOE);
            event.accept(ModItems.FINAL_CRYSTAL_HELMET);
            event.accept(ModItems.FINAL_CRYSTAL_CHESTPLATE);
            event.accept(ModItems.FINAL_CRYSTAL_LEGGINGS);
            event.accept(ModItems.FINAL_CRYSTAL_BOOTS);
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.END_METEORITE_SWORD);
            event.accept(ModItems.END_METEORITE_AXE);
            event.accept(ModItems.END_METEORITE_HELMET);
            event.accept(ModItems.END_METEORITE_CHESTPLATE);
            event.accept(ModItems.END_METEORITE_LEGGINGS);
            event.accept(ModItems.END_METEORITE_BOOTS);
            event.accept(ModItems.CHANGE_JADE_SWORD);
            event.accept(ModItems.CHANGE_JADE_AXE);
            event.accept(ModItems.CHANGE_JADE_HELMET);
            event.accept(ModItems.CHANGE_JADE_CHESTPLATE);
            event.accept(ModItems.CHANGE_JADE_LEGGINGS);
            event.accept(ModItems.CHANGE_JADE_BOOTS);
            event.accept(ModItems.FINAL_CRYSTAL_SWORD);
            event.accept(ModItems.FINAL_CRYSTAL_AXE);
            event.accept(ModItems.FINAL_CRYSTAL_HELMET);
            event.accept(ModItems.FINAL_CRYSTAL_CHESTPLATE);
            event.accept(ModItems.FINAL_CRYSTAL_LEGGINGS);
            event.accept(ModItems.FINAL_CRYSTAL_BOOTS);
        }
    }
}
