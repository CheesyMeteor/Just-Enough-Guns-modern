package ttv.migami.jeg.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.entity.Bubble;

public class BubbleRenderer extends EntityRenderer<Bubble> {
    private static final ResourceLocation TEXTURE_LOCATION = Reference.id("textures/entity/bubble.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE_LOCATION);

   public BubbleRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    protected int getBlockLightLevel(Bubble pEntity, BlockPos pPos) {
        return 15;
    }

    public void render(Bubble pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.scale(6.0F, 6.0F, 6.0F);
        pPoseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        PoseStack.Pose posestack$pose = pPoseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RENDER_TYPE);
        vertex(vertexconsumer, posestack$pose, pPackedLight, 0.0F, 0, 0, 1);
        vertex(vertexconsumer, posestack$pose, pPackedLight, 1.0F, 0, 1, 1);
        vertex(vertexconsumer, posestack$pose, pPackedLight, 1.0F, 1, 1, 0);
        vertex(vertexconsumer, posestack$pose, pPackedLight, 0.0F, 1, 0, 0);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    private static void vertex(VertexConsumer pConsumer, PoseStack.Pose pPose, int pLightmapUV, float pX, int pY, int pU, int pV) {
        // PORT: 1.21 vertex API (addVertex/setColor/setUv/setOverlay/setLight/setNormal)
        pConsumer.addVertex(pPose, pX - 0.5F, (float)pY - 0.25F, 0.0F).setColor(255, 255, 255, 255).setUv((float)pU, (float)pV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(pLightmapUV).setNormal(pPose, 0.0F, 1.0F, 0.0F);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(Bubble pEntity) {
        return TEXTURE_LOCATION;
    }
}
