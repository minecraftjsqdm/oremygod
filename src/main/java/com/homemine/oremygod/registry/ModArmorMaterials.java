package com.homemine.oremygod.registry;

import com.homemine.oremygod.OreMyGodMod;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * End meteorite defense is 5/10/8/5 (netherite +2 each); change jade is 1.5x that
 * (7.5/15/12/7.5 rounded half up to 8/15/12/8). Enchantability 15, toughness 3,
 * knockback resistance 0.1. Durability multipliers (56 and 112) are applied per
 * item via ArmorItem.Type#getDurability(int), since 1.21.1 materials no longer
 * carry it.
 */
public class ModArmorMaterials {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, OreMyGodMod.MODID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> END_METEORITE =
            ARMOR_MATERIALS.register("end_meteorite", () -> new ArmorMaterial(
                    defenseMap(5, 10, 8, 5),
                    15,
                    SoundEvents.ARMOR_EQUIP_NETHERITE,
                    () -> net.minecraft.world.item.crafting.Ingredient.of(ModItems.END_METEORITE_INGOT.get()),
                    List.of(new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(OreMyGodMod.MODID, "end_meteorite"))),
                    3.0F,
                    0.1F));

    /** 1.5x the end meteorite protection (8/15/12/8); durability comes from the item (multiplier 112). */
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> CHANGE_JADE =
            ARMOR_MATERIALS.register("change_jade", () -> new ArmorMaterial(
                    defenseMap(8, 15, 12, 8),
                    15,
                    SoundEvents.ARMOR_EQUIP_NETHERITE,
                    () -> net.minecraft.world.item.crafting.Ingredient.of(ModItems.CHANGE_JADE.get()),
                    List.of(new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(OreMyGodMod.MODID, "change_jade"))),
                    3.0F,
                    0.1F));

    /** 1.5x the change jade protection (12/23/18/12, 65 total); durability multiplier 168. */
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> FINAL_CRYSTAL =
            ARMOR_MATERIALS.register("final_crystal", () -> new ArmorMaterial(
                    defenseMap(12, 23, 18, 12),
                    23,
                    SoundEvents.ARMOR_EQUIP_NETHERITE,
                    () -> net.minecraft.world.item.crafting.Ingredient.of(ModItems.FINAL_CRYSTAL_BALL.get()),
                    List.of(new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(OreMyGodMod.MODID, "final_crystal"))),
                    4.5F,
                    0.15F));

    private static Map<ArmorItem.Type, Integer> defenseMap(int helmet, int chestplate, int leggings, int boots) {
        return Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, boots);
            map.put(ArmorItem.Type.LEGGINGS, leggings);
            map.put(ArmorItem.Type.CHESTPLATE, chestplate);
            map.put(ArmorItem.Type.HELMET, helmet);
            map.put(ArmorItem.Type.BODY, 0);
        });
    }
}
