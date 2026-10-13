package com.homemine.oremygod.world;

import com.homemine.oremygod.OreMyGodMod;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModBiomeModifierSerializers {

    public static final DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, OreMyGodMod.MODID);

    public static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<OuterEndOreBiomeModifier>>
            OUTER_END_ORES = BIOME_MODIFIER_SERIALIZERS.register(
                    "outer_end_ores", () -> OuterEndOreBiomeModifier.CODEC);
}
