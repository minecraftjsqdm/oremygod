package com.homemine.oremygod.item;

import com.homemine.oremygod.registry.ModBlockTags;
import com.homemine.oremygod.registry.ModItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

/**
 * Change Jade tier, one step above the End Meteorite Iron tier.
 * durability 6094 (end meteorite 3047 * 2), mining speed 15.6 (gold 12.0 * 1.3),
 * attackDamageBonus 0 because per-item final damage is set explicitly.
 * Only blocks tagged needs_end_meteorite_tool are denied, so this tier can mine
 * every block the end meteorite iron tier can, plus the change jade ore.
 */
public enum ChangeJadeTier implements Tier {
    CHANGE_JADE(6094, 15.6F, 0.0F, 15);

    private final int uses;
    private final float speed;
    private final float attackDamageBonus;
    private final int enchantmentValue;

    ChangeJadeTier(int uses, float speed, float attackDamageBonus, int enchantmentValue) {
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
        return ModBlockTags.INCORRECT_FOR_CHANGE_JADE_TOOL;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(ModItems.CHANGE_JADE.get());
    }
}
