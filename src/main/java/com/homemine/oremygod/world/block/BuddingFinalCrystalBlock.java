package com.homemine.oremygod.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

/**
 * 末世水晶母岩：末世水晶簇的生长基底。
 *
 * <p>生长用<b>定时刻</b>而非随机刻。原因：随机刻命中率仅 3/4096/刻，2 游戏小时（2000 刻）内
 * 每块母岩平均只轮得到约 1.5 次随机刻，物理上无法完成「6 面从空长到满成熟」；
 * 因此改为放下后每 {@link #STAGE_TICKS} 刻推进一次，每次把 6 个面各推进
 * {@link FinalCrystalClusterBlock#STEP} 点成熟度 —— 4 次推进后 6 面同时到达 age=100，
 * 总耗时正好 4 × 500 = 2000 刻 = <b>2 游戏小时</b>（规格书 R6.1 / R7）。</p>
 *
 * <p>不可再生（无任何合成/复制途径）；非精准采集破坏时原地生成一只蠹虫、且不产出本体
 * （掉落由战利品表处理，此处只负责生成蠹虫）。</p>
 */
public class BuddingFinalCrystalBlock extends Block {

    /** 每次推进的间隔：4 × 500 = 2000 刻 = 2 游戏小时。 */
    public static final int STAGE_TICKS = 500;

    public BuddingFinalCrystalBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide()) {
            level.scheduleTick(pos, this, STAGE_TICKS);
        }
    }

    /** 每 STAGE_TICKS 刻推进所有可推进的面，然后继续排下一个定时刻。 */
    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        boolean grew = false;
        for (Direction face : Direction.values()) {
            BlockPos target = pos.relative(face);
            BlockState targetState = level.getBlockState(target);

            if (FinalCrystalClusterBlock.canClusterGrowAtState(targetState)) {
                // 空位（空气或满格水）：长出一档 25 的新簇
                BlockState grown = com.homemine.oremygod.registry.ModBlocks.FINAL_CRYSTAL_CLUSTER.get()
                        .defaultBlockState()
                        .setValue(FinalCrystalClusterBlock.FACING, face)
                        .setValue(FinalCrystalClusterBlock.AGE, FinalCrystalClusterBlock.STEP)
                        .setValue(FinalCrystalClusterBlock.WATERLOGGED,
                                targetState.getFluidState().getType() == Fluids.WATER);
                level.setBlockAndUpdate(target, grown);
                grew = true;
            } else if (FinalCrystalClusterBlock.canAdvance(targetState, face)) {
                // 已有簇且朝向本面：推进一档
                int next = Math.min(100, targetState.getValue(FinalCrystalClusterBlock.AGE)
                        + FinalCrystalClusterBlock.STEP);
                level.setBlockAndUpdate(target, targetState.setValue(FinalCrystalClusterBlock.AGE, next));
                grew = true;
            }
        }

        if (grew) {
            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_CHIME,
                    net.minecraft.sounds.SoundSource.BLOCKS, 0.4F, 1.4F);
        }
        // 继续排程：母岩永久保持生长能力（重放后同样生效）
        level.scheduleTick(pos, this, STAGE_TICKS);
    }

    /**
     * 非精准采集破坏时生成蠹虫（不产出母岩本体由战利品表决定，这里只补实体）。
     * 精准采集则不生成 —— 玩家是来搬母岩的，不该被咬。
     */
    @Override
    protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool,
                                   boolean dropExperience) {
        super.spawnAfterBreak(state, level, pos, tool, dropExperience);

        boolean silkTouch = EnchantmentHelper.getItemEnchantmentLevel(
                level.registryAccess().holderOrThrow(Enchantments.SILK_TOUCH), tool) > 0;
        if (silkTouch) {
            return;
        }

        Silverfish silverfish = EntityType.SILVERFISH.create(level);
        if (silverfish != null) {
            silverfish.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
            level.addFreshEntity(silverfish);
            silverfish.spawnAnim();
        }
    }
}
