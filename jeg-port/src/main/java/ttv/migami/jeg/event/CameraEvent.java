package ttv.migami.jeg.event;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.entity.monster.phantom.terror.TerrorPhantom;
import ttv.migami.jeg.network.PacketHandler;
import ttv.migami.jeg.network.message.C2SMessagePlayerRotation;

@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class CameraEvent {
    @SubscribeEvent
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity cameraEntity = minecraft.getCameraEntity();
        Camera camera = minecraft.gameRenderer.getMainCamera();

        if (cameraEntity != null && cameraEntity.getVehicle() instanceof TerrorPhantom phantom) {
            //camera.setAnglesInternal(phantom.getYRot(), phantom.getXRot());

            float yaw = event.getYaw();
            float pitch = event.getPitch();

            camera.setRotation(yaw, pitch); // made public by accesstransformer.cfg

            if (cameraEntity instanceof Player player)
                PacketHandler.getPlayChannel().sendToServer(new C2SMessagePlayerRotation(player));
        }
    }
}
