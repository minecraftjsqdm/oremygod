package com.homemine.oremygod.client;

import com.homemine.oremygod.OreMyGodMod;
import com.homemine.oremygod.registry.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

/**
 * 三套盔甲胸甲的<b>客户端几何</b>微调：把手臂 ModelPart 在 x/z 方向压窄。
 *
 * <p>原版盔甲轮廓由 {@code HumanoidArmorLayer} 里的 {@code CubeDeformation(1.0)} 决定，
 * 改贴图只能改明暗、改不了宽度。这里借 NeoForge 的
 * {@link IClientItemExtensions#getHumanoidArmorModel} 钩子，只对本模组的胸甲返回一个
 * 独立的 {@link HumanoidModel} 实例，手臂缩到 {@link #ARM_SCALE}，不影响原版与其他模组盔甲。</p>
 *
 * <p>该模型只用于胸甲（手臂只在 CHEST 部位渲染），腿部/头盔走原版模型。</p>
 */
@EventBusSubscriber(modid = OreMyGodMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ModArmorRenderExtensions {

    /** 手臂宽度收缩比例：0.78 → 外甲 6px 宽的手臂收成约 4.7px。 */
    private static final float ARM_SCALE_X = 0.78F;

    /** 手臂厚度收缩比例：0.92 → 袖子仍贴在躯干前后面上，避免看起来像披风。 */
    private static final float ARM_SCALE_Z = 0.92F;

    /** 与胸甲外层一致的网格（CubeDeformation 1.0），烘焙一次后常驻复用。 */
    private static final ModelLayerLocation NARROW_ARMOR_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(OreMyGodMod.MODID, "narrow_arm_armor"), "main");

    private static HumanoidModel<LivingEntity> narrowChestModel;

    private ModArmorRenderExtensions() {
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(NARROW_ARMOR_LAYER,
                () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(1.0F), 0.0F), 64, 32));
    }

    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot,
                    HumanoidModel<?> original) {
                return slot == EquipmentSlot.CHEST ? narrowChestModel() : original;
            }
        },
                ModItems.END_METEORITE_CHESTPLATE.get(),
                ModItems.CHANGE_JADE_CHESTPLATE.get(),
                ModItems.FINAL_CRYSTAL_CHESTPLATE.get());
    }

    private static HumanoidModel<LivingEntity> narrowChestModel() {
        if (narrowChestModel == null) {
            ModelPart root = Minecraft.getInstance().getEntityModels().bakeLayer(NARROW_ARMOR_LAYER);
            narrowChestModel = new NarrowArmsModel(root);
        }
        return narrowChestModel;
    }

    /** 手臂压窄版人形模型；可见部位与姿态由每帧的 copyFrom 覆盖，缩放则在渲染前重新施加。 */
    private static final class NarrowArmsModel extends HumanoidModel<LivingEntity> {

        private NarrowArmsModel(ModelPart root) {
            super(root);
            this.setAllVisible(false);
            this.body.visible = true;
            this.rightArm.visible = true;
            this.leftArm.visible = true;
        }

        @Override
        public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                int color) {
            // copyPropertiesTo 会把 xScale/zScale 从原版模型抄回 1.0，所以每次渲染前重新压窄。
            this.rightArm.xScale = ARM_SCALE_X;
            this.rightArm.zScale = ARM_SCALE_Z;
            this.leftArm.xScale = ARM_SCALE_X;
            this.leftArm.zScale = ARM_SCALE_Z;
            super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, color);
        }
    }
}
