package com.homemine.oremygod.loot;

import com.homemine.oremygod.OreMyGodMod;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModLootModifiers {

    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, OreMyGodMod.MODID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<FortuneDebrisModifier>>
            FORTUNE_DEBRIS = GLOBAL_LOOT_MODIFIERS.register("fortune_debris", () -> FortuneDebrisModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<EndCityTemplateModifier>>
            END_CITY_TEMPLATE = GLOBAL_LOOT_MODIFIERS.register("end_city_template", () -> EndCityTemplateModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<MoonDungeonTemplateModifier>>
            MOON_DUNGEON_TEMPLATE = GLOBAL_LOOT_MODIFIERS.register("moon_dungeon_template",
                    () -> MoonDungeonTemplateModifier.CODEC);
}
