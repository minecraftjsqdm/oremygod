package com.homemine.oremygod.loot;

import com.homemine.oremygod.registry.ModItems;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;

/**
 * End meteorite ore bonus drops.
 * Base debris drop comes from the block loot table. Fortune gives two independent
 * 50% bonus rolls per level; total debris is capped at 5. Silk Touch is left
 * entirely to the loot table (block drops itself), so this modifier does nothing.
 */
public class FortuneDebrisModifier implements IGlobalLootModifier {

    public static final MapCodec<FortuneDebrisModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    IGlobalLootModifier.LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(m -> m.conditions)
            ).apply(instance, FortuneDebrisModifier::new));

    private static final int MAX_TOTAL = 5;

    private final LootItemCondition[] conditions;

    public FortuneDebrisModifier(LootItemCondition[] conditions) {
        this.conditions = conditions;
    }

    @Override
    public ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (LootItemCondition condition : this.conditions) {
            if (!condition.test(context)) {
                return generatedLoot;
            }
        }
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        if (tool == null) {
            return generatedLoot;
        }
        var lookup = context.getLevel().holderLookup(Registries.ENCHANTMENT);
        var silk = lookup.getOrThrow(Enchantments.SILK_TOUCH);
        if (EnchantmentHelper.getItemEnchantmentLevel(silk, tool) > 0) {
            return generatedLoot;
        }
        int existing = 0;
        for (ItemStack stack : generatedLoot) {
            if (stack.is(ModItems.END_METEORITE_DEBRIS.get())) {
                existing += stack.getCount();
            }
        }
        int room = MAX_TOTAL - existing;
        if (room <= 0) {
            return generatedLoot;
        }
        var fortune = lookup.getOrThrow(Enchantments.FORTUNE);
        int level = EnchantmentHelper.getItemEnchantmentLevel(fortune, tool);
        int bonus = 0;
        for (int i = 0; i < level * 2; i++) {
            if (context.getRandom().nextBoolean()) {
                bonus++;
            }
        }
        bonus = Math.min(bonus, room);
        if (bonus > 0) {
            generatedLoot.add(new ItemStack(ModItems.END_METEORITE_DEBRIS.get(), bonus));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
