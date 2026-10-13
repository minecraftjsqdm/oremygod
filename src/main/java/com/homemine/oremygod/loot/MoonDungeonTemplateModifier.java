package com.homemine.oremygod.loot;

import com.homemine.oremygod.registry.ModItems;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;

/**
 * Ad Astra moon dungeon treasure chests: chance-based Change Jade upgrade
 * template. The loot table id is supplied by the conditions in the data file,
 * so the modifier stays inert when Ad Astra is not installed.
 */
public class MoonDungeonTemplateModifier implements IGlobalLootModifier {

    public static final MapCodec<MoonDungeonTemplateModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    IGlobalLootModifier.LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(m -> m.conditions),
                    Codec.DOUBLE.optionalFieldOf("chance", 0.125D).forGetter(m -> m.chance)
            ).apply(instance, MoonDungeonTemplateModifier::new));

    private final LootItemCondition[] conditions;
    private final double chance;

    public MoonDungeonTemplateModifier(LootItemCondition[] conditions, double chance) {
        this.conditions = conditions;
        this.chance = chance;
    }

    @Override
    public ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (LootItemCondition condition : this.conditions) {
            if (!condition.test(context)) {
                return generatedLoot;
            }
        }
        if (context.getRandom().nextDouble() < this.chance) {
            generatedLoot.add(new ItemStack(ModItems.CHANGE_JADE_UPGRADE_TEMPLATE.get()));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
