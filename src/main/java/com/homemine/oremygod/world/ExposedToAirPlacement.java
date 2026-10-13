package com.homemine.oremygod.world;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.stream.Stream;

/**
 * Passes a candidate ore position only when at least one of its 6 neighbours
 * is air (cave / ravine / cliff-face exposure).
 */
public class ExposedToAirPlacement extends PlacementModifier {

    public static final ExposedToAirPlacement INSTANCE = new ExposedToAirPlacement();
    public static final MapCodec<ExposedToAirPlacement> CODEC = MapCodec.unit(INSTANCE);

    private ExposedToAirPlacement() {
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (context.getLevel().getBlockState(pos.relative(direction)).isAir()) {
                return Stream.of(pos);
            }
        }
        return Stream.empty();
    }

    @Override
    public PlacementModifierType<?> type() {
        return ModPlacementModifierTypes.EXPOSED_TO_AIR.get();
    }
}
