package ttv.migami.jeg.mixin.client;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import ttv.migami.jeg.Config;
import ttv.migami.jeg.client.handler.AimingHandler;
import ttv.migami.jeg.common.Gun;
import ttv.migami.jeg.init.ModSyncedDataKeys;
import ttv.migami.jeg.item.GunItem;

/**
 * Author: MrCrayfish
 */
@Mixin(MouseHandler.class)
public class MouseHandlerMixin
{
    /*
     * PORT: the 1.20.1 version used @ModifyVariable on the sensitivity local of turnPlayer() (ordinal 2), which is not
     * stable across the 1.21 rewrite (turnPlayer now takes a double). Both axes passed to LocalPlayer#turn are scaled
     * instead, which has the same effect on the final look delta.
     * PORT-VERIFY: MouseHandler#turnPlayer(double) still calls LocalPlayer#turn(DD)V exactly once.
     */
    @ModifyArg(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"), index = 0, require = 0)
    private double jeg$sensitivityX(double original)
    {
        return jeg$sensitivity(original);
    }

    @ModifyArg(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"), index = 1, require = 0)
    private double jeg$sensitivityY(double original)
    {
        return jeg$sensitivity(original);
    }

    @Unique
    private static double jeg$sensitivity(double original)
    {
        float additionalAdsSensitivity = 1.0F;
        Minecraft mc = Minecraft.getInstance();
        if(mc.player != null && !mc.player.getMainHandItem().isEmpty() && mc.options.getCameraType() == CameraType.FIRST_PERSON)
        {
            ItemStack heldItem = mc.player.getMainHandItem();
            if(heldItem.getItem() instanceof GunItem gunItem)
            {
                if(AimingHandler.get().isAiming() && !ModSyncedDataKeys.RELOADING.getValue(mc.player))
                {
                    Gun modifiedGun = gunItem.getModifiedGun(heldItem);
                    if(modifiedGun.getModules().getZoom() != null)
                    {
                        float modifier = Gun.getFovModifier(heldItem, modifiedGun);
                        additionalAdsSensitivity = Mth.clamp(1.0F - (1.0F / modifier) / 10F, 0.0F, 1.0F);
                    }
                }
            }
        }
        double adsSensitivity = Config.CLIENT.controls.aimDownSightSensitivity.get();
        return original * (1.0 - (1.0 - adsSensitivity) * AimingHandler.get().getNormalisedAdsProgress()) * additionalAdsSensitivity;
    }
}
