package com.homemine.oremygod.registry;

import com.homemine.oremygod.OreMyGodMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/**
 * Block tags backing the mining tier chain above netherite.
 * Vanilla has no tier above netherite, so these tags are folded into the
 * vanilla incorrect_for_*_tool tags by datapack files under data/minecraft.
 */
public class ModBlockTags {

    /** Blocks that need a netherite-or-better tool: the end meteorite ore and block. */
    public static final TagKey<Block> NEEDS_NETHERITE_TOOL = create("needs_netherite_tool");

    /** Blocks that need an end-meteorite-or-better tool: the change jade ore and block. */
    public static final TagKey<Block> NEEDS_END_METEORITE_TOOL = create("needs_end_meteorite_tool");

    /** Denied blocks for the end meteorite iron tier: everything needing end meteorite or better. */
    public static final TagKey<Block> INCORRECT_FOR_END_METEORITE_TOOL = create("incorrect_for_end_meteorite_tool");

    /** Denied blocks for the change jade tier: the final crystal ore and block. */
    public static final TagKey<Block> INCORRECT_FOR_CHANGE_JADE_TOOL = create("incorrect_for_change_jade_tool");

    /** Denied blocks for the final crystal tier: nothing, it is the top tier. */
    public static final TagKey<Block> INCORRECT_FOR_FINAL_CRYSTAL_TOOL = create("incorrect_for_final_crystal_tool");

    private static TagKey<Block> create(String name) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(OreMyGodMod.MODID, name));
    }
}
