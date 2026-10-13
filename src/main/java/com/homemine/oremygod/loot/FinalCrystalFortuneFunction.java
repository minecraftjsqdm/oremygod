package com.homemine.oremygod.loot;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

/**
 * 末世水晶簇的精确时运函数（规格书 R6.1-4 批准的方案）。
 *
 * <p>原版能用的 {@code minecraft:apply_bonus} 表达不了「I=+1、II=+1、III=+2」这种阶梯
 * （{@code uniform_bonus_count} 是均匀随机、{@code ore_drops} 是乘法），所以自建一个：
 * 按工具上的时运等级直接加固定数量，<b>上限 +2</b>。</p>
 *
 * <table>
 *   <tr><th>时运等级</th><th>0</th><th>I</th><th>II</th><th>III+</th></tr>
 *   <tr><td>额外掉落</td><td>+0</td><td>+1</td><td>+1</td><td>+2</td></tr>
 * </table>
 *
 * <p>用无参 {@link MapCodec#unit} 序列化 —— 数值写在代码里，数据包只需引用函数名。</p>
 */
public class FinalCrystalFortuneFunction implements LootItemFunction {

    public static final MapCodec<FinalCrystalFortuneFunction> CODEC =
            MapCodec.unit(FinalCrystalFortuneFunction::new);

    /** 按等级取额外数量。抽成静态方法便于单元核对。 */
    public static int bonusFor(int fortuneLevel) {
        if (fortuneLevel <= 0) {
            return 0;
        }
        return fortuneLevel >= 3 ? 2 : 1;
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext context) {
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        if (tool == null || tool.isEmpty()) {
            return stack;
        }
        int fortune = EnchantmentHelper.getItemEnchantmentLevel(
                context.getLevel().registryAccess().holderOrThrow(Enchantments.FORTUNE), tool);
        int bonus = bonusFor(fortune);
        if (bonus <= 0) {
            return stack;
        }
        ItemStack result = stack.copy();
        result.grow(bonus);
        return result;
    }

    /** 1.21.1 的 {@link LootItemFunction} 要求实现方回报自己的注册类型。 */
    @Override
    public LootItemFunctionType<? extends LootItemFunction> getType() {
        return ModLootFunctions.FINAL_CRYSTAL_FORTUNE.get();
    }
}
