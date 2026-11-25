package ttv.migami.jeg.client.handler;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import ttv.migami.jeg.Config;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.client.render.crosshair.*;
import ttv.migami.jeg.compat.ShoulderSurfingHelper;
import ttv.migami.jeg.event.GunFireEvent;
import ttv.migami.jeg.item.GunItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class CrosshairHandler
{
    private static CrosshairHandler instance;

    public static CrosshairHandler get()
    {
        if(instance == null)
        {
            instance = new CrosshairHandler();
        }
        return instance;
    }

    private final Map<ResourceLocation, Crosshair> idToCrosshair = new HashMap<>();
    private final List<Crosshair> registeredCrosshairs = new ArrayList<>();
    private Crosshair currentCrosshair = null;

    private CrosshairHandler()
    {
        this.register(new TexturedCrosshair(Reference.id("better_default")));
        this.register(new TexturedCrosshair(Reference.id("circle")));
        this.register(new TexturedCrosshair(Reference.id("filled_circle"), false));
        this.register(new TexturedCrosshair(Reference.id("square")));
        this.register(new TexturedCrosshair(Reference.id("round")));
        this.register(new TexturedCrosshair(Reference.id("arrow")));
        this.register(new TexturedCrosshair(Reference.id("dot")));
        this.register(new TexturedCrosshair(Reference.id("box")));
        this.register(new TexturedCrosshair(Reference.id("hit_marker")));
        this.register(new TexturedCrosshair(Reference.id("line")));
        this.register(new TexturedCrosshair(Reference.id("t")));
        this.register(new TexturedCrosshair(Reference.id("smiley")));
        this.register(new TechCrosshair());
        this.register(new DynamicCrosshair());}

    /**
     * Registers a new crosshair. If the crosshair has already been registered, it will be ignored.
     */
    public void register(Crosshair crosshair)
    {
        if(!this.idToCrosshair.containsKey(crosshair.getLocation()))
        {
            this.idToCrosshair.put(crosshair.getLocation(), crosshair);
            this.registeredCrosshairs.add(crosshair);
        }
    }

    /**
     * Sets the crosshair using the given id. The crosshair with the associated id must be registered
     * or the default crosshair will be used.
     *
     * @param id the id of the crosshair
     */
    public void setCrosshair(ResourceLocation id)
    {
        this.currentCrosshair = this.idToCrosshair.getOrDefault(id, Crosshair.DEFAULT);
    }

    /**
     * Gets the current crosshair
     */
    @Nullable
    public Crosshair getCurrentCrosshair()
    {
        if(this.currentCrosshair == null && this.registeredCrosshairs.size() > 0)
        {
            ResourceLocation id = ResourceLocation.tryParse(Config.CLIENT.display.crosshair.get());
            this.currentCrosshair = id != null ? this.idToCrosshair.getOrDefault(id, Crosshair.DEFAULT) : Crosshair.DEFAULT;
        }
        return this.currentCrosshair;
    }

    /**
     * Gets a list of registered crosshairs. Please note that this list is immutable.
     */
    public List<Crosshair> getRegisteredCrosshairs()
    {
        return ImmutableList.copyOf(this.registeredCrosshairs);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGuiLayerEvent.Pre event)
    {
        if(!event.getName().equals(VanillaGuiLayers.CROSSHAIR))
            return;

        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null)
            return;

        Crosshair crosshair = this.getCurrentCrosshair();
        PoseStack stack = event.getGuiGraphics().pose();
        stack.pushPose();
        int scaledWidth = mc.getWindow().getGuiScaledWidth();
        int scaledHeight = mc.getWindow().getGuiScaledHeight();
        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);

        if (GunRenderingHandler.get().isRenderingHitMarker())
        {
            Crosshair hitMarker = new SpecialHitMarker();
            hitMarker.render(mc, stack, scaledWidth, scaledHeight, partialTick);
        }

        if(AimingHandler.get().getNormalisedAdsProgress() > 0.5 && (mc.options.getCameraType().isFirstPerson()))
        {
            event.setCanceled(true);
            stack.popPose(); // PORT: original returned without popping the pose pushed above
            return;
        }

        if(crosshair == null || crosshair.isDefault())
        {
            stack.popPose(); // PORT: see above
            return;
        }

        ItemStack heldItem = mc.player.getMainHandItem();
        if(!(heldItem.getItem() instanceof GunItem))
        {
            stack.popPose(); // PORT: see above
            return;
        }

        event.setCanceled(true);

        //if(!mc.options.getCameraType().isFirstPerson())
        //    return;

        if(mc.options.getCameraType() != CameraType.FIRST_PERSON && !ShoulderSurfingHelper.isShoulderSurfing())
        {
            stack.popPose(); // PORT: see above
            return;
        }

        if(mc.player.getUseItem().getItem() instanceof ShieldItem)
        {
            stack.popPose(); // PORT: see above
            return;
        }

        crosshair.render(mc, stack, scaledWidth, scaledHeight, partialTick);
        stack.popPose();
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event)
    {
        Crosshair crosshair = this.getCurrentCrosshair();
        if(crosshair == null || crosshair.isDefault())
            return;

        crosshair.tick();
    }

    @SubscribeEvent
    public void onGunFired(GunFireEvent.Post event)
    {
        Crosshair crosshair = this.getCurrentCrosshair();
        if(crosshair == null || crosshair.isDefault())
            return;

        crosshair.onGunFired();
    }

    /* Updates the crosshair if the config is reloaded. */
    public static void onConfigReload(ModConfigEvent.Reloading event)
    {
        ModConfig config = event.getConfig();
        if(config.getType() == ModConfig.Type.CLIENT && config.getModId().equals(Reference.MOD_ID))
        {
            ResourceLocation id = ResourceLocation.tryParse(Config.CLIENT.display.crosshair.get());
            if(id != null)
            {
                CrosshairHandler.get().setCrosshair(id);
            }
        }
    }
}
