package com.homemine.oremygod.world;

import com.homemine.oremygod.OreMyGodMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModPlacementModifierTypes {

    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS =
            DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, OreMyGodMod.MODID);

    public static final Supplier<PlacementModifierType<ExposedToAirPlacement>> EXPOSED_TO_AIR =
            PLACEMENT_MODIFIERS.register("exposed_to_air", () -> () -> ExposedToAirPlacement.CODEC);
}
