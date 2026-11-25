package ttv.migami.jeg.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DyedItemColor;
import ttv.migami.jeg.entity.DynamicHelmet;

import java.util.Optional;

public class DynamicHelmetRenderer extends EntityRenderer<DynamicHelmet> {

    private final HumanoidModel<?> defaultModel;

    public DynamicHelmetRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.defaultModel = new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
    }

    @Override
    public void render(DynamicHelmet entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int light) {
        ItemStack stack = entity.getStoredStackData();
        Item item = stack.getItem();

        poseStack.pushPose();
        poseStack.translate(0, 0, 0);
        poseStack.popPose();

        poseStack.pushPose();
        float rotation = (entity.tickCount + partialTick) * 20f;
        poseStack.mulPose(Axis.YP.rotationDegrees((float) (45 + rotation * 0.5)));
        poseStack.mulPose(Axis.XP.rotationDegrees((float) (rotation * 1.5)));
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));

        if (!(item instanceof ArmorItem)) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();

            poseStack.pushPose();

             poseStack.scale(0.65F, 0.65F, 0.65F);
             itemRenderer.renderStatic(
                    stack,
                    ItemDisplayContext.HEAD,
                    light,
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    buffer,
                    entity.level(),
                    entity.getId()
            );

            poseStack.popPose();
        }
        else if (item instanceof ArmorItem armorItem && armorItem.getEquipmentSlot() == EquipmentSlot.HEAD) {
            poseStack.scale(1.5F, 1.5F, 1.5F);
            ModelPart model = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR);
            defaultModel.getHead().setPos(0, -10F, 0);

            defaultModel.head.visible = true;
            defaultModel.hat.visible = true;

            defaultModel.body.visible = false;
            defaultModel.leftArm.visible = false;
            defaultModel.rightArm.visible = false;
            defaultModel.leftLeg.visible = false;
            defaultModel.rightLeg.visible = false;

            ResourceLocation texture = getArmorTexture(armorItem);

            if (armorItem.getMaterial() == ArmorMaterials.LEATHER /* PORT-VERIFY: Holder identity compare of ArmorMaterial */) {
                int color = 0xA06540;
                color = DyedItemColor.getOrDefault(stack, color);

                defaultModel.renderToBuffer(
                        poseStack,
                        buffer.getBuffer(RenderType.armorCutoutNoCull(texture)),
                        light,
                        OverlayTexture.NO_OVERLAY,
                        0xFF000000 | color
                );

                // Optionally render overlay (gray stitching)
                ResourceLocation overlay = ResourceLocation.withDefaultNamespace("textures/models/armor/leather_layer_1_overlay.png");
                defaultModel.renderToBuffer(
                        poseStack,
                        buffer.getBuffer(RenderType.armorCutoutNoCull(overlay)),
                        light,
                        OverlayTexture.NO_OVERLAY,
                        -1
                );
            } else {
                defaultModel.renderToBuffer(
                        poseStack,
                        buffer.getBuffer(RenderType.armorCutoutNoCull(texture)),
                        light,
                        OverlayTexture.NO_OVERLAY,
                        -1
                );
            }
        }
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(DynamicHelmet entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    private ResourceLocation getArmorTexture(ArmorItem armorItem) {
        // PORT: ArmorMaterial is a data holder in 1.21 (no getName()); the first layer gives the outer-layer texture ("layer_1").
        ResourceLocation texture;
        var layers = armorItem.getMaterial().value().layers();
        if (!layers.isEmpty()) {
            texture = layers.get(0).texture(false); // PORT-VERIFY: ArmorMaterial.Layer#texture(boolean innerLayer)
        } else {
            ResourceLocation itemKey = BuiltInRegistries.ITEM.getKey(armorItem);
            texture = ResourceLocation.fromNamespaceAndPath(itemKey.getNamespace(), "textures/models/armor/" + itemKey.getPath() + "_layer_1.png");
        }

        Minecraft client = Minecraft.getInstance();
        Optional<Resource> resource = client.getResourceManager().getResource(texture);

        if (resource.isPresent()) {
            return texture;
        } else {
            return ResourceLocation.withDefaultNamespace("textures/models/armor/iron_layer_1.png");
        }
    }
}
