package ttv.migami.jeg.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ttv.migami.jeg.item.GunItem;

/**
 * Author: MrCrayfish
 */
@Mixin(Minecraft.class)
public class MinecraftMixin
{
    // PORT: LocalCapture.CAPTURE_FAILHARD with the whole local list (incl. InputEvent) is fragile in 1.21; MixinExtras @Local grabs the ItemStack by type.
    // PORT-VERIFY: ItemStack is the only ItemStack local in Minecraft#startUseItem at this point; itemUsed(InteractionHand) ordinal 0
    @Inject(method = "startUseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;itemUsed(Lnet/minecraft/world/InteractionHand;)V", ordinal = 0), cancellable = true, require = 0)
    private void beforeItemUsed(CallbackInfo ci, @Local ItemStack stack)
    {
        if(stack.getItem() instanceof GunItem)
        {
            ci.cancel();
        }
    }
}
