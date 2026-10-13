package com.homemine.oremygod.registry;

import com.homemine.oremygod.OreMyGodMod;
import com.homemine.oremygod.world.block.BuddingFinalCrystalBlock;
import com.homemine.oremygod.world.block.FinalCrystalClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(OreMyGodMod.MODID);

    public static final DeferredBlock<Block> END_METEORITE =
            BLOCKS.registerSimpleBlock("end_meteorite",
                    BlockBehaviour.Properties.of()
                            .strength(3.0F, 9.0F)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE));

    public static final DeferredBlock<Block> END_METEORITE_BLOCK =
            BLOCKS.registerSimpleBlock("end_meteorite_block",
                    BlockBehaviour.Properties.of()
                            .strength(50.0F, 1200.0F)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL));

    public static final DeferredBlock<Block> CHANGE_JADE_ORE =
            BLOCKS.registerSimpleBlock("change_jade_ore",
                    BlockBehaviour.Properties.of()
                            .strength(4.5F, 9.0F)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE));

    public static final DeferredBlock<Block> CHANGE_JADE_BLOCK =
            BLOCKS.registerSimpleBlock("change_jade_block",
                    BlockBehaviour.Properties.of()
                            .strength(5.0F, 6.0F)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE));

    /**
     * 末世水晶母岩：末世水晶簇的生长基底。不可再生；精准采集可得本体，
     * 非精准采集破坏时原地生成蠹虫（逻辑见 {@link BuddingFinalCrystalBlock}）。
     */
    public static final DeferredBlock<Block> BUDDING_FINAL_CRYSTAL =
            BLOCKS.register("budding_final_crystal",
                    () -> new BuddingFinalCrystalBlock(BlockBehaviour.Properties.of()
                            .strength(1.5F, 1.5F)
                            .sound(SoundType.AMETHYST)));

    /**
     * 末世水晶簇：单方块 + {@code age}(0–100) 五档成熟度，6 面附着。
     * 碰撞箱照原版紫水晶簇（高度 7、底部偏移 3）。挖掉只掉粗末世水晶（战利品表按 age 分档）。
     */
    public static final DeferredBlock<Block> FINAL_CRYSTAL_CLUSTER =
            BLOCKS.register("final_crystal_cluster",
                    () -> new FinalCrystalClusterBlock(7.0F, 3.0F, BlockBehaviour.Properties.of()
                            .strength(1.5F, 1.5F)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
                            .sound(SoundType.AMETHYST)));

    public static final DeferredBlock<Block> FINAL_CRYSTAL_BLOCK =
            BLOCKS.registerSimpleBlock("final_crystal_block",
                    BlockBehaviour.Properties.of()
                            .strength(6.0F, 9.0F)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL));
}
