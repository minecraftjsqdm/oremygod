package com.homemine.oremygod.loot;

import com.homemine.oremygod.registry.ModItems;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;

/**
 * End City treasure chests: the chest inside an End Ship (recognised by a dragon
 * head within 8 blocks) always yields one upgrade template; ordinary End City
 * chests have a 10% chance.
 */
public class EndCityTemplateModifier implements IGlobalLootModifier {

    public static final MapCodec<EndCityTemplateModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    IGlobalLootModifier.LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(m -> m.conditions)
            ).apply(instance, EndCityTemplateModifier::new));

    private static final double CITY_CHANCE = 0.1;
    private static final int SEARCH_RADIUS = 8;

    private final LootItemCondition[] conditions;

    public EndCityTemplateModifier(LootItemCondition[] conditions) {
        this.conditions = conditions;
    }

    @Override
    public ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (LootItemCondition condition : this.conditions) {
            if (!condition.test(context)) {
                return generatedLoot;
            }
        }
        boolean ship = false;
        var originVec = context.getParamOrNull(LootContextParams.ORIGIN);
        if (originVec != null) {
            BlockPos origin = BlockPos.containing(originVec);
            ship = BlockPos.betweenClosedStream(
                            origin.offset(-SEARCH_RADIUS, -SEARCH_RADIUS, -SEARCH_RADIUS),
                            origin.offset(SEARCH_RADIUS, SEARCH_RADIUS, SEARCH_RADIUS))
                    .anyMatch(pos -> {
                        var state = context.getLevel().getBlockState(pos);
                        return state.is(Blocks.DRAGON_HEAD) || state.is(Blocks.DRAGON_WALL_HEAD);
                    });
        }
        if (ship || context.getRandom().nextDouble() < CITY_CHANCE) {
            generatedLoot.add(new ItemStack(ModItems.END_METEORITE_UPGRADE_TEMPLATE.get()));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
