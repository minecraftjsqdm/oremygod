package com.homemine.oremygod.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

/**
 * Change Jade upgrade template, built exactly like the vanilla netherite
 * upgrade template (custom material, custom addition-slot icon).
 */
public class ChangeJadeSmithingTemplateItem extends SmithingTemplateItem {

    private static final String BASE = "item.oremygod.change_jade_upgrade_smithing_template";

    public ChangeJadeSmithingTemplateItem() {
        super(
                Component.translatable(BASE + ".applies_to").withStyle(ChatFormatting.BLUE),
                Component.translatable(BASE + ".ingredients").withStyle(ChatFormatting.BLUE),
                Component.translatable(BASE + ".upgrade_description").withStyle(ChatFormatting.GRAY),
                Component.translatable(BASE + ".base_slot_description"),
                Component.translatable(BASE + ".additions_slot_description"),
                List.of(
                        mc("item/empty_armor_slot_helmet"),
                        mc("item/empty_slot_sword"),
                        mc("item/empty_armor_slot_chestplate"),
                        mc("item/empty_slot_pickaxe"),
                        mc("item/empty_armor_slot_leggings"),
                        mc("item/empty_slot_axe"),
                        mc("item/empty_armor_slot_boots"),
                        mc("item/empty_slot_hoe"),
                        mc("item/empty_slot_shovel")
                ),
                List.of(ResourceLocation.fromNamespaceAndPath("oremygod",
                        "item/empty_slot_change_jade"))
        );
    }

    private static ResourceLocation mc(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }
}
