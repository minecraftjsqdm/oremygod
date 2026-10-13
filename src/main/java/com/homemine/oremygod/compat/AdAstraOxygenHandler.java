package com.homemine.oremygod.compat;

import com.homemine.oremygod.registry.ModItems;
import earth.terrarium.adastra.api.events.AdAstraEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Ad Astra 软依赖：<b>嫦娥玉石 / 末世水晶头盔自带太空呼吸</b>（规格 2026-10-03 R9）。
 *
 * <p><b>为什么必须走 Java 而不是数据包</b>：Ad Astra 的氧气判定有三条路，前两条都走不通——</p>
 * <ol>
 *   <li>{@code SpaceSuitItem.hasFullSet()} 要求<b>四个盔甲槽全部</b>在
 *       {@code ad_astra:space_suit_items} 标签里，只给头盔做不到；</li>
 *   <li>氧气不是药水效果（全包没有 oxygen / space 类 {@code effect.*} 键），挂 trait 也没用；</li>
 *   <li>真正可用的钩子是 {@link AdAstraEvents.EntityOxygenEvent}：一个<b>静态注册</b>的函数式接口
 *       （不是 NeoForge 事件），Ad Astra 每次询问「这个实体现在有氧气吗」时都会遍历调用它，
 *       返回值一旦与传入值不同就立即采用。</li>
 * </ol>
 *
 * <p>因此本类只在 Ad Astra 存在时被加载（注册处有 {@code ModList.isLoaded} 守卫），
 * 缺失时不会触发 {@code NoClassDefFoundError}。</p>
 */
public final class AdAstraOxygenHandler {

    private AdAstraOxygenHandler() {
    }

    /** 由 {@code OreMyGodMod} 在 Ad Astra 存在时调用一次。 */
    public static void register() {
        AdAstraEvents.EntityOxygenEvent.register(AdAstraOxygenHandler::hasOxygen);
    }

    /**
     * @param entity    被询问的实体
     * @param hasOxygen Ad Astra 原本的判定结果（密封基地 / 空间站 / 已充氧的宇航服都会是 true）
     * @return true 表示「有氧气」；返回传入值表示不干预
     */
    private static boolean hasOxygen(Entity entity, boolean hasOxygen) {
        if (hasOxygen) {
            return true; // 本来就能呼吸，不干预
        }
        if (!(entity instanceof Player player)) {
            return false;
        }
        return wearsBreathingHelmet(player);
    }

    /** 嫦娥玉石及以上（嫦娥玉石、末世水晶）的头盔自带太空呼吸；末地陨铁不含在内。 */
    private static boolean wearsBreathingHelmet(Player player) {
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        return helmet.is(ModItems.CHANGE_JADE_HELMET.get())
                || helmet.is(ModItems.FINAL_CRYSTAL_HELMET.get());
    }
}
