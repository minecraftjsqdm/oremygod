package com.homemine.oremygod.loot;

import com.homemine.oremygod.OreMyGodMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 本模组自定义的战利品函数注册表。
 *
 * <p>{@code LOOT_FUNCTION_TYPE} 是原版注册表（非数据包注册表），因此需要 Java 注册；
 * 注册后在战利品表里按 id 引用即可：{@code {"function": "oremygod:final_crystal_fortune"}}。</p>
 */
public final class ModLootFunctions {

    private ModLootFunctions() {
    }

    public static final DeferredRegister<LootItemFunctionType<?>> LOOT_FUNCTIONS =
            DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, OreMyGodMod.MODID);

    public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<FinalCrystalFortuneFunction>>
            FINAL_CRYSTAL_FORTUNE = LOOT_FUNCTIONS.register("final_crystal_fortune",
                    () -> new LootItemFunctionType<>(FinalCrystalFortuneFunction.CODEC));
}
