package com.homemine.oremygod.item;

import com.homemine.oremygod.registry.ModBlockTags;
import com.homemine.oremygod.registry.ModItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

/**
 * Final Crystal tier, one step above the Change Jade tier and the top of the
 * chain added by this mod. Durability 9141 (change jade 6094 * 1.5), mining
 * speed 18.0 (change jade 15.6 * 1.15), attackDamageBonus 0 because per-item
 * final damage is set explicitly. Its denied-block tag is empty, so it can mine
 * every block the change jade tier can.
 */
public enum FinalCrystalTier implements Tier {
    FINAL_CRYSTAL(9141, 24.0F, 0.0F, 23);

    private final int uses;
    private final float speed;
    private final float attackDamageBonus;
    private final int enchantmentValue;

    FinalCrystalTier(int uses, float speed, float attackDamageBonus, int enchantmentValue) {
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
        return ModBlockTags.INCORRECT_FOR_FINAL_CRYSTAL_TOOL;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(ModItems.FINAL_CRYSTAL_BALL.get());
    }
}
