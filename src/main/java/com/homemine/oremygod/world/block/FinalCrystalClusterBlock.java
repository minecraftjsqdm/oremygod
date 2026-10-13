package com.homemine.oremygod.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.util.RandomSource;

/**
 * 末世水晶簇：单个方块 + 整数 {@code age}(0–100) 属性，按区间映射 5 档外观。
 *
 * <p>沿用原版 {@link AmethystClusterBlock} 的 {@code facing} / {@code waterlogged} 语义与碰撞箱，
 * 只额外挂一个连续的 {@code age} 属性 —— 原版把它拆成 4 个独立方块（small/medium/large bud + cluster），
 * 这里按设计稿的「5 档成熟度」做成一个方块的内建属性，采伐掉落直接按 {@code age} 分档。</p>
 *
 * <p>生长不由本方块自己驱动，而是由相邻的 {@link BuddingFinalCrystalBlock} 定时推进。</p>
 */
public class FinalCrystalClusterBlock extends AmethystClusterBlock {

    /** 成熟度 0–100。5 档划分：0–25 / 25–50 / 50–75 / 75–99 / 100。 */
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 100);

    /** 每次推进的步长（4 步到达 100）。 */
    public static final int STEP = 25;

    public FinalCrystalClusterBlock(float height, float aabbOffset, Properties properties) {
        super(height, aabbOffset, properties);
        registerDefaultState(defaultBlockState().setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE);
    }

    /** 未成熟的簇不掉落（见规格书 1.2：前 3 档无产出），成熟档位由战利品表按 age 判定。 */
    public static boolean isMature(BlockState state) {
        return state.getValue(AGE) >= 75;
    }

    /** 放置时把 age 固定为 0（新长出来的簇从第一档开始）。 */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state == null ? null : state.setValue(AGE, 0);
    }

    /** 相邻面被拆除时按原版规则自毁（复用父类的支撑判定）。 */
    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return super.canSurvive(state, level, pos);
    }

    @Override
    protected net.minecraft.world.phys.shapes.VoxelShape getShape(BlockState state, BlockGetter level,
                                                                 BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        return super.getShape(state, level, pos, context);
    }

    /** 供母岩判断「这个位置的簇还能不能继续推进」。 */
    public static boolean canAdvance(BlockState state, Direction face) {
        return state.getBlock() instanceof FinalCrystalClusterBlock
                && state.getValue(FACING) == face
                && state.getValue(AGE) < 100;
    }

    /** 母岩推进一格时调用的音效（轻微，避免刷屏）。 */
    static void playGrowthSound(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.4F, 1.4F);
    }

    /** 空位判定：与原版母岩一致（空气，或满格水）。 */
    public static boolean canClusterGrowAtState(BlockState state) {
        return state.isAir() || (state.getFluidState().getType() == Fluids.WATER
                && state.getFluidState().getAmount() == 8);
    }

    /** 随机刻不驱动生长（生长走母岩的定时刻），保持默认空实现。 */
    @Override
    protected void randomTick(BlockState state, net.minecraft.server.level.ServerLevel level,
                             BlockPos pos, RandomSource random) {
        // no-op
    }
}
