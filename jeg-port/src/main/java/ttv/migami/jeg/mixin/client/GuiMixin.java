package ttv.migami.jeg.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.item.TelescopicScopeItem;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @Inject(method = "renderSpyglassOverlay", at = @At("HEAD"), cancellable = true)
    private void customRenderSpyglassOverlay(GuiGraphics pGuiGraphics, float pScopeScale, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.player.isUsingItem() && minecraft.player.getUseItem().getItem() instanceof TelescopicScopeItem) {
            float f = (float) Math.min(pGuiGraphics.guiWidth(), pGuiGraphics.guiHeight());
            float f1 = Math.min((float) pGuiGraphics.guiWidth() / f, (float) pGuiGraphics.guiHeight() / f) * pScopeScale;
            int i = Mth.floor(f * f1);
            int j = Mth.floor(f * f1);
            int k = (pGuiGraphics.guiWidth() - i) / 2;
            int l = (pGuiGraphics.guiHeight() - j) / 2;
            int i1 = k + i;
            int j1 = l + j;

            ResourceLocation LONG_SCOPE_OVERLAY = Reference.id("textures/scope_long_overlay.png");
            pGuiGraphics.blit(LONG_SCOPE_OVERLAY, k, l, -90, 0.0F, 0.0F, i, j, i, j);
            pGuiGraphics.fill(RenderType.guiOverlay(), 0, j1, pGuiGraphics.guiWidth(), pGuiGraphics.guiHeight(), -90, -16777216);
            pGuiGraphics.fill(RenderType.guiOverlay(), 0, 0, pGuiGraphics.guiWidth(), l, -90, -16777216);
            pGuiGraphics.fill(RenderType.guiOverlay(), 0, l, k, j1, -90, -16777216);
            pGuiGraphics.fill(RenderType.guiOverlay(), i1, l, pGuiGraphics.guiWidth(), j1, -90, -16777216);

            ci.cancel();
        }
    }
}