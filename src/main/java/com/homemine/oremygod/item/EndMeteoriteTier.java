package com.homemine.oremygod.item;

import com.homemine.oremygod.registry.ModBlockTags;
import com.homemine.oremygod.registry.ModItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

/**
 * End Meteorite Iron tool tier.
 * durability 3047 (netherite 2031 * 1.5), mining speed 11.88 (gold 12.0 * 0.99),
 * attackDamageBonus 0 because per-item final damage is set explicitly.
 * Mining level sits one step above netherite: blocks tagged
 * needs_netherite_tool (the end meteorite ore itself) are denied to netherite
 * and below, while only blocks tagged needs_end_meteorite_tool are denied here.
 */
public enum EndMeteoriteTier implements Tier {
    END_METEORITE(3047, 11.88F, 0.0F, 15);

    private final int uses;
    private final float speed;
    private final float attackDamageBonus;
    private final int enchantmentValue;

    EndMeteoriteTier(int uses, float speed, float attackDamageBonus, int enchantmentValue) {
        this.uses = uses;
        this.speed = speed;
        this.attackDamageBonus = attackDamageBonus;
        this.enchantmentValue = enchantmentValue;
    }

    @Override
    public int getUses() {
        return uses;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamageBonus;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return ModBlockTags.INCORRECT_FOR_END_METEORITE_TOOL;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(ModItems.END_METEORITE_INGOT.get());
    }
}
