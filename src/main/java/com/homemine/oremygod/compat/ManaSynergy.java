package com.homemine.oremygod.compat;

import com.homemine.oremygod.OreMyGodMod;
import com.homemine.oremygod.registry.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

/**
 * Soft-compat mana synergy, implemented purely through the vanilla attribute
 * system: no Mixin and no compile dependency on either magic mod.
 * Every equipped End Meteorite Iron piece (4 armor slots + main/off hand, up to 6)
 * grants +10% of base max mana (multiply-base; pieces add up to +60%) to each
 * mod's max-mana attribute. If a mod is absent its attribute simply does not
 * resolve and is skipped.
 */
public final class ManaSynergy {

    private static final double BONUS_PER_PIECE = 0.10D;
    private static final ResourceLocation IRONS_MAX_MANA =
            ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "max_mana");
    private static final ResourceLocation ARS_MAX_MANA =
            ResourceLocation.fromNamespaceAndPath("ars_nouveau", "max_mana");

    private ManaSynergy() {
    }

    public static void onItemAttributeModifiers(ItemAttributeModifierEvent event) {
        EquipmentSlotGroup slot = slotFor(event.getItemStack());
        if (slot == null) {
            return;
        }
        addPercentBonus(event, IRONS_MAX_MANA, slot);
        addPercentBonus(event, ARS_MAX_MANA, slot);
    }

    private static EquipmentSlotGroup slotFor(ItemStack stack) {
        Item item = stack.getItem();
        if (item == ModItems.END_METEORITE_HELMET.get()) {
            return EquipmentSlotGroup.HEAD;
        }
        if (item == ModItems.END_METEORITE_CHESTPLATE.get()) {
            return EquipmentSlotGroup.CHEST;
        }
        if (item == ModItems.END_METEORITE_LEGGINGS.get()) {
            return EquipmentSlotGroup.LEGS;
        }
        if (item == ModItems.END_METEORITE_BOOTS.get()) {
            return EquipmentSlotGroup.FEET;
        }
        if (item == ModItems.END_METEORITE_SWORD.get()
                || item == ModItems.END_METEORITE_PICKAXE.get()
                || item == ModItems.END_METEORITE_AXE.get()
                || item == ModItems.END_METEORITE_SHOVEL.get()
                || item == ModItems.END_METEORITE_HOE.get()) {
            // HAND covers both main hand and off hand, so a second piece held in
            // the off hand counts toward the 6-piece maximum.
            return EquipmentSlotGroup.HAND;
        }
        return null;
    }

    private static void addPercentBonus(ItemAttributeModifierEvent event, ResourceLocation attributeId,
                                        EquipmentSlotGroup slot) {
        BuiltInRegistries.ATTRIBUTE.getOptional(attributeId).ifPresent(attribute -> {
            Holder<Attribute> holder = BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute);
            ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(
                    OreMyGodMod.MODID, "mana_bonus." + attributeId.getNamespace());
            event.addModifier(holder, new AttributeModifier(
                    modifierId, BONUS_PER_PIECE, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), slot);
        });
    }
}
