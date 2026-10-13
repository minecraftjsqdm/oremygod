package com.homemine.oremygod.registry;

import com.homemine.oremygod.OreMyGodMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * 末世水晶加工链的 4 种流体（规格书第 3.3 节）：
 * <ul>
 *   <li>{@code molten_final_crystal} —— 熔融末世水晶（球熔化 / 污浊液净化后的产物）</li>
 *   <li>{@code impure_molten_final_crystal} —— 污浊熔融末世水晶（粗水晶熔化）</li>
 *   <li>{@code molten_forging_additive} —— 锻造助剂（锻造助剂锭熔化）</li>
 *   <li>{@code molten_forged_crystal} —— 熔融锻造水晶（熔融末世水晶 + 锻造助剂 合金，刻意减半）</li>
 * </ul>
 *
 * <p>每种都是完整的 NeoForge 流体：FluidType + 源流体 + 流动流体 + 液体方块 + 桶（桶在 {@link ModItems}）。</p>
 *
 * <p><b>实现注意</b>：{@code BaseFlowingFluid.Properties} 需要在构造时拿到「源/流动/方块/桶」四者的
 * Supplier。这些 holder 就声明在本类里，若把它们当<b>参数</b>写进字段初始化式会触发 Java 的
 * 「非法前向引用 / 初始化程序中存在自引用」编译错误；因此这里把参数求值全部放进
 * {@code xxxProps()} <b>方法体</b>内 —— 方法体里引用静态字段是合法的，且方法只在注册表求值
 * （supplier 执行）时被调用，那时所有静态字段都已初始化完毕。</p>
 */
public class ModFluids {

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, OreMyGodMod.MODID);

    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(Registries.FLUID, OreMyGodMod.MODID);

    /** 液体方块（方块注册表）。 */
    public static final DeferredRegister.Blocks FLUID_BLOCKS =
            DeferredRegister.createBlocks(OreMyGodMod.MODID);

    // ============================================================ 熔融末世水晶
    public static final DeferredHolder<FluidType, FluidType> MOLTEN_FINAL_CRYSTAL_TYPE =
            FLUID_TYPES.register("molten_final_crystal", ModFluids::crystalType);
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> MOLTEN_FINAL_CRYSTAL =
            FLUIDS.register("molten_final_crystal", () -> new BaseFlowingFluid.Source(moltenFinalCrystalProps()));
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> MOLTEN_FINAL_CRYSTAL_FLOWING =
            FLUIDS.register("flowing_molten_final_crystal", () -> new BaseFlowingFluid.Flowing(moltenFinalCrystalProps()));
    public static final DeferredHolder<Block, LiquidBlock> MOLTEN_FINAL_CRYSTAL_BLOCK =
            FLUID_BLOCKS.register("molten_final_crystal", () -> liquidBlock(MOLTEN_FINAL_CRYSTAL.get()));

    private static BaseFlowingFluid.Properties moltenFinalCrystalProps() {
        return new BaseFlowingFluid.Properties(MOLTEN_FINAL_CRYSTAL_TYPE, MOLTEN_FINAL_CRYSTAL,
                MOLTEN_FINAL_CRYSTAL_FLOWING)
                .bucket(ModItems.MOLTEN_FINAL_CRYSTAL_BUCKET)
                .block(MOLTEN_FINAL_CRYSTAL_BLOCK)
                .slopeFindDistance(2).levelDecreasePerBlock(3).tickRate(20).explosionResistance(100.0F);
    }

    // ============================================================ 污浊熔融末世水晶
    public static final DeferredHolder<FluidType, FluidType> IMPURE_MOLTEN_FINAL_CRYSTAL_TYPE =
            FLUID_TYPES.register("impure_molten_final_crystal", ModFluids::crystalType);
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> IMPURE_MOLTEN_FINAL_CRYSTAL =
            FLUIDS.register("impure_molten_final_crystal",
                    () -> new BaseFlowingFluid.Source(impureMoltenFinalCrystalProps()));
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> IMPURE_MOLTEN_FINAL_CRYSTAL_FLOWING =
            FLUIDS.register("flowing_impure_molten_final_crystal",
                    () -> new BaseFlowingFluid.Flowing(impureMoltenFinalCrystalProps()));
    public static final DeferredHolder<Block, LiquidBlock> IMPURE_MOLTEN_FINAL_CRYSTAL_BLOCK =
            FLUID_BLOCKS.register("impure_molten_final_crystal",
                    () -> liquidBlock(IMPURE_MOLTEN_FINAL_CRYSTAL.get()));

    private static BaseFlowingFluid.Properties impureMoltenFinalCrystalProps() {
        return new BaseFlowingFluid.Properties(IMPURE_MOLTEN_FINAL_CRYSTAL_TYPE, IMPURE_MOLTEN_FINAL_CRYSTAL,
                IMPURE_MOLTEN_FINAL_CRYSTAL_FLOWING)
                .bucket(ModItems.IMPURE_MOLTEN_FINAL_CRYSTAL_BUCKET)
                .block(IMPURE_MOLTEN_FINAL_CRYSTAL_BLOCK)
                .slopeFindDistance(2).levelDecreasePerBlock(3).tickRate(20).explosionResistance(100.0F);
    }

    // ============================================================ 锻造助剂
    public static final DeferredHolder<FluidType, FluidType> MOLTEN_FORGING_ADDITIVE_TYPE =
            FLUID_TYPES.register("molten_forging_additive", ModFluids::additiveType);
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> MOLTEN_FORGING_ADDITIVE =
            FLUIDS.register("molten_forging_additive",
                    () -> new BaseFlowingFluid.Source(moltenForgingAdditiveProps()));
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> MOLTEN_FORGING_ADDITIVE_FLOWING =
            FLUIDS.register("flowing_molten_forging_additive",
                    () -> new BaseFlowingFluid.Flowing(moltenForgingAdditiveProps()));
    public static final DeferredHolder<Block, LiquidBlock> MOLTEN_FORGING_ADDITIVE_BLOCK =
            FLUID_BLOCKS.register("molten_forging_additive",
                    () -> liquidBlock(MOLTEN_FORGING_ADDITIVE.get()));

    private static BaseFlowingFluid.Properties moltenForgingAdditiveProps() {
        return new BaseFlowingFluid.Properties(MOLTEN_FORGING_ADDITIVE_TYPE, MOLTEN_FORGING_ADDITIVE,
                MOLTEN_FORGING_ADDITIVE_FLOWING)
                .bucket(ModItems.MOLTEN_FORGING_ADDITIVE_BUCKET)
                .block(MOLTEN_FORGING_ADDITIVE_BLOCK)
                .slopeFindDistance(2).levelDecreasePerBlock(3).tickRate(20).explosionResistance(100.0F);
    }

    // ============================================================ 熔融锻造水晶
    public static final DeferredHolder<FluidType, FluidType> MOLTEN_FORGED_CRYSTAL_TYPE =
            FLUID_TYPES.register("molten_forged_crystal", ModFluids::forgedType);
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> MOLTEN_FORGED_CRYSTAL =
            FLUIDS.register("molten_forged_crystal",
                    () -> new BaseFlowingFluid.Source(moltenForgedCrystalProps()));
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> MOLTEN_FORGED_CRYSTAL_FLOWING =
            FLUIDS.register("flowing_molten_forged_crystal",
                    () -> new BaseFlowingFluid.Flowing(moltenForgedCrystalProps()));
    public static final DeferredHolder<Block, LiquidBlock> MOLTEN_FORGED_CRYSTAL_BLOCK =
            FLUID_BLOCKS.register("molten_forged_crystal",
                    () -> liquidBlock(MOLTEN_FORGED_CRYSTAL.get()));

    private static BaseFlowingFluid.Properties moltenForgedCrystalProps() {
        return new BaseFlowingFluid.Properties(MOLTEN_FORGED_CRYSTAL_TYPE, MOLTEN_FORGED_CRYSTAL,
                MOLTEN_FORGED_CRYSTAL_FLOWING)
                .bucket(ModItems.MOLTEN_FORGED_CRYSTAL_BUCKET)
                .block(MOLTEN_FORGED_CRYSTAL_BLOCK)
                .slopeFindDistance(2).levelDecreasePerBlock(3).tickRate(20).explosionResistance(100.0F);
    }

    // ============================================================ 熔融嫦娥玉石
    public static final DeferredHolder<FluidType, FluidType> MOLTEN_CHANGE_JADE_TYPE =
            FLUID_TYPES.register("molten_change_jade", ModFluids::jadeType);
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> MOLTEN_CHANGE_JADE =
            FLUIDS.register("molten_change_jade",
                    () -> new BaseFlowingFluid.Source(moltenChangeJadeProps()));
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> MOLTEN_CHANGE_JADE_FLOWING =
            FLUIDS.register("flowing_molten_change_jade",
                    () -> new BaseFlowingFluid.Flowing(moltenChangeJadeProps()));
    public static final DeferredHolder<Block, LiquidBlock> MOLTEN_CHANGE_JADE_BLOCK =
            FLUID_BLOCKS.register("molten_change_jade",
                    () -> liquidBlock(MOLTEN_CHANGE_JADE.get()));

    private static BaseFlowingFluid.Properties moltenChangeJadeProps() {
        return new BaseFlowingFluid.Properties(MOLTEN_CHANGE_JADE_TYPE, MOLTEN_CHANGE_JADE,
                MOLTEN_CHANGE_JADE_FLOWING)
                .bucket(ModItems.MOLTEN_CHANGE_JADE_BUCKET)
                .block(MOLTEN_CHANGE_JADE_BLOCK)
                .slopeFindDistance(2).levelDecreasePerBlock(3).tickRate(20).explosionResistance(100.0F);
    }

    // ============================================================ 熔融末地陨铁
    public static final DeferredHolder<FluidType, FluidType> MOLTEN_END_METEORITE_TYPE =
            FLUID_TYPES.register("molten_end_meteorite", ModFluids::meteoriteType);
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> MOLTEN_END_METEORITE =
            FLUIDS.register("molten_end_meteorite",
                    () -> new BaseFlowingFluid.Source(moltenEndMeteoriteProps()));
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> MOLTEN_END_METEORITE_FLOWING =
            FLUIDS.register("flowing_molten_end_meteorite",
                    () -> new BaseFlowingFluid.Flowing(moltenEndMeteoriteProps()));
    public static final DeferredHolder<Block, LiquidBlock> MOLTEN_END_METEORITE_BLOCK =
            FLUID_BLOCKS.register("molten_end_meteorite",
                    () -> liquidBlock(MOLTEN_END_METEORITE.get()));

    private static BaseFlowingFluid.Properties moltenEndMeteoriteProps() {
        return new BaseFlowingFluid.Properties(MOLTEN_END_METEORITE_TYPE, MOLTEN_END_METEORITE,
                MOLTEN_END_METEORITE_FLOWING)
                .bucket(ModItems.MOLTEN_END_METEORITE_BUCKET)
                .block(MOLTEN_END_METEORITE_BLOCK)
                .slopeFindDistance(2).levelDecreasePerBlock(3).tickRate(20).explosionResistance(100.0F);
    }

    // ============================================================ 辅助

    private static FluidType jadeType() {
        return new FluidType(FluidType.Properties.create()
                .density(2600).viscosity(5000).temperature(1300).lightLevel(8).canDrown(false));
    }

    private static FluidType meteoriteType() {
        return new FluidType(FluidType.Properties.create()
                .density(2800).viscosity(6000).temperature(1500).lightLevel(9).canDrown(false));
    }

    private static FluidType crystalType() {
        return new FluidType(FluidType.Properties.create()
                .density(3200).viscosity(7000).temperature(1400).lightLevel(10).canDrown(false));
    }

    private static FluidType additiveType() {
        return new FluidType(FluidType.Properties.create()
                .density(2600).viscosity(5000).temperature(1200).lightLevel(6).canDrown(false));
    }

    private static FluidType forgedType() {
        return new FluidType(FluidType.Properties.create()
                .density(3600).viscosity(8000).temperature(1600).lightLevel(13).canDrown(false));
    }

    private static LiquidBlock liquidBlock(Fluid source) {
        return new LiquidBlock((FlowingFluid) source, BlockBehaviour.Properties.of()
                .liquid()
                .noCollission()
                .strength(100.0F)
                .noLootTable()
                .replaceable()
                .pushReaction(PushReaction.DESTROY));
    }
}
