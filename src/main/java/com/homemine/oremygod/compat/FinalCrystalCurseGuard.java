package com.homemine.oremygod.compat;

import com.homemine.oremygod.registry.ModItems;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * 末世水晶装备的「绑定诅咒免疫」守卫（规格书 R5.1-12：<b>两条途径都要</b>）。
 *
 * <p>途径一：浇筑升级那一刻剥除已有诅咒 —— 见 {@link FinalCrystalCastingHandler#stripBindingCurse}。</p>
 *
 * <p>途径二：此后无法再被附上。附魔进入物品的入口很多（附魔台、铁砧、村民交易、战利品、指令），
 * 逐个拦截需要依赖各入口是否存在可用事件，且容易漏。这里采用<b>兜底清理</b>策略：
 * 铁砧入口直接拦下（即时反馈，且不消耗经验），同时按玩家周期扫描并剥离已经存在的诅咒
 * —— 后者覆盖了所有无法挂钩的入口（附魔台抽取、村民交易、战利品生成、/give 等）。</p>
 */
public final class FinalCrystalCurseGuard {

    private FinalCrystalCurseGuard() {
    }

    /** 每 N tick 扫描一次玩家背包（性能与响应速度的折中）。 */
    private static final int SCAN_INTERVAL = 20;

    private static Set<Item> finalCrystalGear;

    private static Set<Item> gear() {
        if (finalCrystalGear == null) {
            finalCrystalGear = Set.of(
                    ModItems.FINAL_CRYSTAL_SWORD.get(), ModItems.FINAL_CRYSTAL_PICKAXE.get(),
                    ModItems.FINAL_CRYSTAL_AXE.get(), ModItems.FINAL_CRYSTAL_SHOVEL.get(),
                    ModItems.FINAL_CRYSTAL_HOE.get(), ModItems.FINAL_CRYSTAL_HELMET.get(),
                    ModItems.FINAL_CRYSTAL_CHESTPLATE.get(), ModItems.FINAL_CRYSTAL_LEGGINGS.get(),
                    ModItems.FINAL_CRYSTAL_BOOTS.get());
        }
        return finalCrystalGear;
    }

    public static boolean isFinalCrystalGear(ItemStack stack) {
        return !stack.isEmpty() && gear().contains(stack.getItem());
    }

    /** 铁砧入口：左槽是末世水晶装备、右槽（附魔书/物品）带绑定诅咒 → 直接拒绝这次合成。 */
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        if (!isFinalCrystalGear(event.getLeft())) {
            return;
        }
        if (hasBindingCurse(event.getRight()) || hasBindingCurse(event.getLeft())) {
            event.setCanceled(true);
        }
    }

    /** 兜底清理：周期性把玩家身上末世水晶装备的绑定诅咒剥掉（覆盖所有无事件可挂的入口）。 */
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        if (serverPlayer.tickCount % SCAN_INTERVAL != 0) {
            return;
        }
        for (int i = 0; i < serverPlayer.getInventory().getContainerSize(); i++) {
            ItemStack stack = serverPlayer.getInventory().getItem(i);
            if (isFinalCrystalGear(stack) && hasBindingCurse(stack)) {
                FinalCrystalCastingHandler.stripBindingCurse(stack, serverPlayer.registryAccess());
            }
        }
    }

    /** 同时看两处附魔组件：物品本体用 ENCHANTMENTS，附魔书用 STORED_ENCHANTMENTS。 */
    private static boolean hasBindingCurse(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        var registries = stack.getComponents();
        if (registries == null) {
            return false;
        }
        // 附魔书：附魔存在 STORED_ENCHANTMENTS。此处只做「是否包含诅咒」的判断，
        // 真正的剥离在 tick 守卫里对装备本体进行。
        ItemEnchantments stored = stack.get(DataComponents.STORED_ENCHANTMENTS);
        if (stored != null && containsCurse(stored)) {
            return true;
        }
        ItemEnchantments own = stack.get(DataComponents.ENCHANTMENTS);
        return own != null && containsCurse(own);
    }

    private static boolean containsCurse(ItemEnchantments enchantments) {
        for (Holder<Enchantment> holder : enchantments.keySet()) {
            if (holder.is(Enchantments.BINDING_CURSE) && enchantments.getLevel(holder) > 0) {
                return true;
            }
        }
        return false;
    }
}
