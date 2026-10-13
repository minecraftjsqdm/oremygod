package com.homemine.oremygod.registry;

import com.homemine.oremygod.OreMyGodMod;
import com.homemine.oremygod.item.ChangeJadeSmithingTemplateItem;
import com.homemine.oremygod.item.ChangeJadeTier;
import com.homemine.oremygod.item.EndMeteoriteSmithingTemplateItem;
import com.homemine.oremygod.item.EndMeteoriteTier;
import com.homemine.oremygod.item.FinalCrystalTier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(OreMyGodMod.MODID);

    // ---- block ----
    public static final DeferredItem<net.minecraft.world.item.BlockItem> END_METEORITE_ORE_ITEM =
            ITEMS.registerSimpleBlockItem(ModBlocks.END_METEORITE);
    public static final DeferredItem<net.minecraft.world.item.BlockItem> END_METEORITE_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem(ModBlocks.END_METEORITE_BLOCK);
    public static final DeferredItem<net.minecraft.world.item.BlockItem> CHANGE_JADE_ORE_ITEM =
            ITEMS.registerSimpleBlockItem(ModBlocks.CHANGE_JADE_ORE);
    public static final DeferredItem<net.minecraft.world.item.BlockItem> CHANGE_JADE_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem(ModBlocks.CHANGE_JADE_BLOCK);
    public static final DeferredItem<net.minecraft.world.item.BlockItem> BUDDING_FINAL_CRYSTAL_ITEM =
            ITEMS.registerSimpleBlockItem(ModBlocks.BUDDING_FINAL_CRYSTAL);
    public static final DeferredItem<net.minecraft.world.item.BlockItem> FINAL_CRYSTAL_CLUSTER_ITEM =
            ITEMS.registerSimpleBlockItem(ModBlocks.FINAL_CRYSTAL_CLUSTER);
    public static final DeferredItem<net.minecraft.world.item.BlockItem> FINAL_CRYSTAL_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem(ModBlocks.FINAL_CRYSTAL_BLOCK);

    // ---- gems ----
    public static final DeferredItem<Item> CHANGE_JADE = ITEMS.registerSimpleItem(
            "change_jade", fireproof());
    public static final DeferredItem<Item> CHANGE_JADE_NUGGET = ITEMS.registerSimpleItem(
            "change_jade_nugget", fireproof());

    // ---- final crystal: 获取与加工链（规格书第 3.2 节 / R8）----
    /** 粗末世水晶：挖晶簇掉落；母岩烧炼产物。 */
    public static final DeferredItem<Item> RAW_FINAL_CRYSTAL = ITEMS.registerSimpleItem(
            "raw_final_crystal", fireproof());
    /** 末世水晶球：由粗末世水晶烧炼得到（唯一来源），兼作 Mekanism 结晶阶段产物。 */
    public static final DeferredItem<Item> FINAL_CRYSTAL_BALL = ITEMS.registerSimpleItem(
            "final_crystal_ball", fireproof());
    /** 锻造助剂锭：Create 工作盆加热搅拌产物（2 下界合金锭 + 2 末地陨铁锭 + 2 地狱疣 → 2 个）。 */
    public static final DeferredItem<Item> FORGING_ADDITIVE_INGOT = ITEMS.registerSimpleItem(
            "forging_additive_ingot", fireproof());
    /**
     * 锻造水晶：熔融锻造水晶的固态形态，<b>唯一</b>可用于寂静装备制作的末世水晶系原料
     * （规格书 2026-10-03 R1/R2）。获取：宝石铸模浇筑 100mB 熔融锻造水晶，或 Create 工作盆
     * 加热搅拌「末世水晶球 + 锻造助剂锭」。
     */
    public static final DeferredItem<Item> FORGED_CRYSTAL = ITEMS.registerSimpleItem(
            "forged_crystal", fireproof());
    /** 末世水晶粉（Mekanism dust 阶段）。 */
    public static final DeferredItem<Item> FINAL_CRYSTAL_DUST = ITEMS.registerSimpleItem(
            "final_crystal_dust", fireproof());
    /** 末世水晶碎块（Mekanism clump 阶段）。 */
    public static final DeferredItem<Item> FINAL_CRYSTAL_CLUMP = ITEMS.registerSimpleItem(
            "final_crystal_clump", fireproof());
    /** 末世水晶碎片（Mekanism shard 阶段）。 */
    public static final DeferredItem<Item> FINAL_CRYSTAL_SHARD = ITEMS.registerSimpleItem(
            "final_crystal_shard", fireproof());
    /** 污浊末世水晶粉（Mekanism dirty dust 阶段）。 */
    public static final DeferredItem<Item> DIRTY_FINAL_CRYSTAL_DUST = ITEMS.registerSimpleItem(
            "dirty_final_crystal_dust", fireproof());

    // ---- processing chain ----
    public static final DeferredItem<Item> END_METEORITE_DEBRIS = ITEMS.registerSimpleItem(
            "end_meteorite_debris", new Item.Properties().fireResistant());
    public static final DeferredItem<Item> END_METEORITE_SCRAP = ITEMS.registerSimpleItem(
            "end_meteorite_scrap", new Item.Properties().fireResistant());
    public static final DeferredItem<Item> END_METEORITE_INGOT = ITEMS.registerSimpleItem(
            "end_meteorite_ingot", new Item.Properties().fireResistant());
    public static final DeferredItem<Item> END_METEORITE_NUGGET = ITEMS.registerSimpleItem(
            "end_meteorite_nugget", new Item.Properties().fireResistant());

    public static final DeferredItem<EndMeteoriteSmithingTemplateItem> END_METEORITE_UPGRADE_TEMPLATE =
            ITEMS.register("end_meteorite_upgrade_smithing_template", EndMeteoriteSmithingTemplateItem::new);

    public static final DeferredItem<ChangeJadeSmithingTemplateItem> CHANGE_JADE_UPGRADE_TEMPLATE =
            ITEMS.register("change_jade_upgrade_smithing_template", ChangeJadeSmithingTemplateItem::new);

    // 末世水晶的锻造模板已删除：升级只走 Productive Metalworks 铸件台浇筑（规格书 R6.1-14 / R8.2）。

    // ---- tools & weapons ----
    // Player base attack is 1; modifier damage = final damage - 1.
    // Final: sword 16, axe 20, pickaxe 12, shovel 13, hoe 2.
    public static final DeferredItem<SwordItem> END_METEORITE_SWORD = ITEMS.register("end_meteorite_sword",
            () -> new SwordItem(EndMeteoriteTier.END_METEORITE,
                    fireproof().attributes(SwordItem.createAttributes(EndMeteoriteTier.END_METEORITE, 15, -2.3F))));
    public static final DeferredItem<PickaxeItem> END_METEORITE_PICKAXE = ITEMS.register("end_meteorite_pickaxe",
            () -> new PickaxeItem(EndMeteoriteTier.END_METEORITE,
                    fireproof().attributes(DiggerItem.createAttributes(EndMeteoriteTier.END_METEORITE, 11.0F, -2.8F))));
    public static final DeferredItem<AxeItem> END_METEORITE_AXE = ITEMS.register("end_meteorite_axe",
            () -> new AxeItem(EndMeteoriteTier.END_METEORITE,
                    fireproof().attributes(DiggerItem.createAttributes(EndMeteoriteTier.END_METEORITE, 19.0F, -3.0F))));
    public static final DeferredItem<ShovelItem> END_METEORITE_SHOVEL = ITEMS.register("end_meteorite_shovel",
            () -> new ShovelItem(EndMeteoriteTier.END_METEORITE,
                    fireproof().attributes(DiggerItem.createAttributes(EndMeteoriteTier.END_METEORITE, 12.0F, -3.0F))));
    public static final DeferredItem<HoeItem> END_METEORITE_HOE = ITEMS.register("end_meteorite_hoe",
            () -> new HoeItem(EndMeteoriteTier.END_METEORITE,
                    fireproof().attributes(DiggerItem.createAttributes(EndMeteoriteTier.END_METEORITE, 1.0F, 0.0F))));

    // ---- armor (durability multiplier 56, applied per type) ----
    public static final DeferredItem<ArmorItem> END_METEORITE_HELMET = ITEMS.register("end_meteorite_helmet",
            () -> new ArmorItem(ModArmorMaterials.END_METEORITE, ArmorItem.Type.HELMET,
                    fireproof().durability(ArmorItem.Type.HELMET.getDurability(56))));
    public static final DeferredItem<ArmorItem> END_METEORITE_CHESTPLATE = ITEMS.register("end_meteorite_chestplate",
            () -> new ArmorItem(ModArmorMaterials.END_METEORITE, ArmorItem.Type.CHESTPLATE,
                    fireproof().durability(ArmorItem.Type.CHESTPLATE.getDurability(56))));
    public static final DeferredItem<ArmorItem> END_METEORITE_LEGGINGS = ITEMS.register("end_meteorite_leggings",
            () -> new ArmorItem(ModArmorMaterials.END_METEORITE, ArmorItem.Type.LEGGINGS,
                    fireproof().durability(ArmorItem.Type.LEGGINGS.getDurability(56))));
    public static final DeferredItem<ArmorItem> END_METEORITE_BOOTS = ITEMS.register("end_meteorite_boots",
            () -> new ArmorItem(ModArmorMaterials.END_METEORITE, ArmorItem.Type.BOOTS,
                    fireproof().durability(ArmorItem.Type.BOOTS.getDurability(56))));

    private static Item.Properties fireproof() {
        return new Item.Properties().fireResistant();
    }

    /**
     * 末世水晶装备的统一属性：防火 + <b>不可破坏</b>（unbreakable 数据组件，规格书 R6.1-11）。
     * 绑定诅咒的免疫由穿戴/升级侧的事件处理（见规格书 1.4）。
     */
    private static Item.Properties finalCrystalGear() {
        return new Item.Properties()
                .fireResistant()
                .component(DataComponents.UNBREAKABLE, new Unbreakable(true));
    }

    // ---- change jade: tools & weapons ----
    // Attack bonuses are the end meteorite ones multiplied by 1.75 and rounded up:
    // sword 15->27, axe 19->34, pickaxe 11->20, shovel 12->21, hoe 1->2.
    public static final DeferredItem<SwordItem> CHANGE_JADE_SWORD = ITEMS.register("change_jade_sword",
            () -> new SwordItem(ChangeJadeTier.CHANGE_JADE,
                    fireproof().attributes(SwordItem.createAttributes(ChangeJadeTier.CHANGE_JADE, 27, -2.3F))));
    public static final DeferredItem<PickaxeItem> CHANGE_JADE_PICKAXE = ITEMS.register("change_jade_pickaxe",
            () -> new PickaxeItem(ChangeJadeTier.CHANGE_JADE,
                    fireproof().attributes(DiggerItem.createAttributes(ChangeJadeTier.CHANGE_JADE, 20.0F, -2.8F))));
    public static final DeferredItem<AxeItem> CHANGE_JADE_AXE = ITEMS.register("change_jade_axe",
            () -> new AxeItem(ChangeJadeTier.CHANGE_JADE,
                    fireproof().attributes(DiggerItem.createAttributes(ChangeJadeTier.CHANGE_JADE, 34.0F, -3.0F))));
    public static final DeferredItem<ShovelItem> CHANGE_JADE_SHOVEL = ITEMS.register("change_jade_shovel",
            () -> new ShovelItem(ChangeJadeTier.CHANGE_JADE,
                    fireproof().attributes(DiggerItem.createAttributes(ChangeJadeTier.CHANGE_JADE, 21.0F, -3.0F))));
    public static final DeferredItem<HoeItem> CHANGE_JADE_HOE = ITEMS.register("change_jade_hoe",
            () -> new HoeItem(ChangeJadeTier.CHANGE_JADE,
                    fireproof().attributes(DiggerItem.createAttributes(ChangeJadeTier.CHANGE_JADE, 2.0F, 0.0F))));

    // ---- change jade: armor (durability multiplier 112, twice the end meteorite 56) ----
    public static final DeferredItem<ArmorItem> CHANGE_JADE_HELMET = ITEMS.register("change_jade_helmet",
            () -> new ArmorItem(ModArmorMaterials.CHANGE_JADE, ArmorItem.Type.HELMET,
                    fireproof().durability(ArmorItem.Type.HELMET.getDurability(112))));
    public static final DeferredItem<ArmorItem> CHANGE_JADE_CHESTPLATE = ITEMS.register("change_jade_chestplate",
            () -> new ArmorItem(ModArmorMaterials.CHANGE_JADE, ArmorItem.Type.CHESTPLATE,
                    fireproof().durability(ArmorItem.Type.CHESTPLATE.getDurability(112))));
    public static final DeferredItem<ArmorItem> CHANGE_JADE_LEGGINGS = ITEMS.register("change_jade_leggings",
            () -> new ArmorItem(ModArmorMaterials.CHANGE_JADE, ArmorItem.Type.LEGGINGS,
                    fireproof().durability(ArmorItem.Type.LEGGINGS.getDurability(112))));
    public static final DeferredItem<ArmorItem> CHANGE_JADE_BOOTS = ITEMS.register("change_jade_boots",
            () -> new ArmorItem(ModArmorMaterials.CHANGE_JADE, ArmorItem.Type.BOOTS,
                    fireproof().durability(ArmorItem.Type.BOOTS.getDurability(112))));

    // ---- final crystal: tools & weapons ----
    // 1.5x the change jade values, rounded up (规格书第 4 章):
    // sword 27→41, axe 34→51, pickaxe 20→30, shovel 21→32, hoe 2→3.
    // 末世水晶装备全部不可破坏（unbreakable 组件）。
    public static final DeferredItem<SwordItem> FINAL_CRYSTAL_SWORD = ITEMS.register("final_crystal_sword",
            () -> new SwordItem(FinalCrystalTier.FINAL_CRYSTAL,
                    finalCrystalGear().attributes(SwordItem.createAttributes(FinalCrystalTier.FINAL_CRYSTAL, 41, -2.3F))));
    public static final DeferredItem<PickaxeItem> FINAL_CRYSTAL_PICKAXE = ITEMS.register("final_crystal_pickaxe",
            () -> new PickaxeItem(FinalCrystalTier.FINAL_CRYSTAL,
                    finalCrystalGear().attributes(DiggerItem.createAttributes(FinalCrystalTier.FINAL_CRYSTAL, 30.0F, -2.8F))));
    public static final DeferredItem<AxeItem> FINAL_CRYSTAL_AXE = ITEMS.register("final_crystal_axe",
            () -> new AxeItem(FinalCrystalTier.FINAL_CRYSTAL,
                    finalCrystalGear().attributes(DiggerItem.createAttributes(FinalCrystalTier.FINAL_CRYSTAL, 51.0F, -3.0F))));
    public static final DeferredItem<ShovelItem> FINAL_CRYSTAL_SHOVEL = ITEMS.register("final_crystal_shovel",
            () -> new ShovelItem(FinalCrystalTier.FINAL_CRYSTAL,
                    finalCrystalGear().attributes(DiggerItem.createAttributes(FinalCrystalTier.FINAL_CRYSTAL, 32.0F, -3.0F))));
    public static final DeferredItem<HoeItem> FINAL_CRYSTAL_HOE = ITEMS.register("final_crystal_hoe",
            () -> new HoeItem(FinalCrystalTier.FINAL_CRYSTAL,
                    finalCrystalGear().attributes(DiggerItem.createAttributes(FinalCrystalTier.FINAL_CRYSTAL, 3.0F, 0.0F))));

    // ---- final crystal: armor (durability multiplier 168, 1.5x the change jade 112) ----
    public static final DeferredItem<ArmorItem> FINAL_CRYSTAL_HELMET = ITEMS.register("final_crystal_helmet",
            () -> new ArmorItem(ModArmorMaterials.FINAL_CRYSTAL, ArmorItem.Type.HELMET,
                    finalCrystalGear().durability(ArmorItem.Type.HELMET.getDurability(168))));
    public static final DeferredItem<ArmorItem> FINAL_CRYSTAL_CHESTPLATE = ITEMS.register("final_crystal_chestplate",
            () -> new ArmorItem(ModArmorMaterials.FINAL_CRYSTAL, ArmorItem.Type.CHESTPLATE,
                    finalCrystalGear().durability(ArmorItem.Type.CHESTPLATE.getDurability(168))));
    public static final DeferredItem<ArmorItem> FINAL_CRYSTAL_LEGGINGS = ITEMS.register("final_crystal_leggings",
            () -> new ArmorItem(ModArmorMaterials.FINAL_CRYSTAL, ArmorItem.Type.LEGGINGS,
                    finalCrystalGear().durability(ArmorItem.Type.LEGGINGS.getDurability(168))));
    public static final DeferredItem<ArmorItem> FINAL_CRYSTAL_BOOTS = ITEMS.register("final_crystal_boots",
            () -> new ArmorItem(ModArmorMaterials.FINAL_CRYSTAL, ArmorItem.Type.BOOTS,
                    finalCrystalGear().durability(ArmorItem.Type.BOOTS.getDurability(168))));

    // ---- 流体桶（流体本体见 ModFluids）----
    public static final DeferredItem<BucketItem> MOLTEN_FINAL_CRYSTAL_BUCKET =
            ITEMS.register("molten_final_crystal_bucket", () -> new BucketItem(
                    ModFluids.MOLTEN_FINAL_CRYSTAL.get(),
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final DeferredItem<BucketItem> IMPURE_MOLTEN_FINAL_CRYSTAL_BUCKET =
            ITEMS.register("impure_molten_final_crystal_bucket", () -> new BucketItem(
                    ModFluids.IMPURE_MOLTEN_FINAL_CRYSTAL.get(),
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final DeferredItem<BucketItem> MOLTEN_FORGING_ADDITIVE_BUCKET =
            ITEMS.register("molten_forging_additive_bucket", () -> new BucketItem(
                    ModFluids.MOLTEN_FORGING_ADDITIVE.get(),
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final DeferredItem<BucketItem> MOLTEN_FORGED_CRYSTAL_BUCKET =
            ITEMS.register("molten_forged_crystal_bucket", () -> new BucketItem(
                    ModFluids.MOLTEN_FORGED_CRYSTAL.get(),
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final DeferredItem<BucketItem> MOLTEN_CHANGE_JADE_BUCKET =
            ITEMS.register("molten_change_jade_bucket", () -> new BucketItem(
                    ModFluids.MOLTEN_CHANGE_JADE.get(),
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final DeferredItem<BucketItem> MOLTEN_END_METEORITE_BUCKET =
            ITEMS.register("molten_end_meteorite_bucket", () -> new BucketItem(
                    ModFluids.MOLTEN_END_METEORITE.get(),
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
}
