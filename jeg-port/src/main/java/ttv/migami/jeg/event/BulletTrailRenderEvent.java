package ttv.migami.jeg.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.client.handler.BulletTrailRenderingHandler;

/**
 * PORT: replaces mixin/client/LevelRendererMixin. The 1.20.1 mixin injected into LevelRenderer#renderLevel (right before
 * the first checkPoseStack call, i.e. after entities were rendered) and passed the level PoseStack; renderLevel's
 * signature changed in 1.21 (DeltaTracker + matrices), so the equivalent NeoForge stage event is used instead.
 * NOTE: if the client slice also hooks RenderLevelStageEvent to call BulletTrailRenderingHandler#render, remove one of them.
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class BulletTrailRenderEvent
{
    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event)
    {
        if(event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES)
        {
            return;
        }
        // PORT-VERIFY: PoseStack of the stage event is identity-like in 1.21 (camera rotation lives in the model-view matrix), unlike the 1.20.1 level PoseStack
        BulletTrailRenderingHandler.get().render(event.getPoseStack(), event.getPartialTick().getGameTimeDeltaPartialTick(false));
    }
}
