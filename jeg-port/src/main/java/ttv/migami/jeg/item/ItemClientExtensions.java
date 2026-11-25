package ttv.migami.jeg.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.client.GunItemStackRenderer;

/**
 * PORT: replaces the per-item {@code Item#initializeClient(Consumer<IClientItemExtensions>)} overrides of the 1.20.1 mod,
 * which no longer exist in NeoForge 1.21. Registers the custom item renderer for every non-animated {@link GunItem}.
 * {@link AnimatedGunItem} (GeckoLib) supplies its own renderer through {@code GeoItem#createGeoRenderer}.
 * <p>
 * NOTE for the client slice: do NOT register extensions for GunItem instances again (duplicate registration).
 */
@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ItemClientExtensions
{
    private ItemClientExtensions() {}

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event)
    {
        IClientItemExtensions gunExtensions = new IClientItemExtensions()
        {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer()
            {
                if(this.renderer == null)
                    this.renderer = new GunItemStackRenderer();
                return this.renderer;
            }
        };

        for(Item item : BuiltInRegistries.ITEM)
        {
            if(item instanceof GunItem && !(item instanceof AnimatedGunItem))
            {
                event.registerItem(gunExtensions, item);
            }
        }
    }
}
