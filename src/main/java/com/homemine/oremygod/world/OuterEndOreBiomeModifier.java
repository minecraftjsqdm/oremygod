package com.homemine.oremygod.world;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

/**
 * Adds the End Meteorite ore feature to every End biome except minecraft:the_end.
 * Tags.Biomes.IS_END (#c:is_end) is the conventional end-biome tag shared by
 * modded End biomes.
 */
public record OuterEndOreBiomeModifier(HolderSet<PlacedFeature> features) implements BiomeModifier {

    public static final MapCodec<OuterEndOreBiomeModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    PlacedFeature.LIST_CODEC.fieldOf("features")
                            .forGetter(OuterEndOreBiomeModifier::features)
            ).apply(instance, OuterEndOreBiomeModifier::new));

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.ADD) {
            return;
        }
        boolean mainEndIsland = biome.unwrapKey()
                .map(key -> key.location().equals(ResourceLocation.withDefaultNamespace("the_end")))
                .orElse(false);
        if (mainEndIsland || !biome.is(Tags.Biomes.IS_END)) {
            return;
        }
        this.features.forEach(feature -> builder.getGenerationSettings()
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, feature));
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return CODEC;
    }
}
