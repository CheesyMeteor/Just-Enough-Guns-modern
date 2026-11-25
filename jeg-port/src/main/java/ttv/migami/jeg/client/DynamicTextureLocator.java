package ttv.migami.jeg.client;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import ttv.migami.jeg.Reference;

public class DynamicTextureLocator {

    public static ResourceLocation getItemTexture(Item item) {
        ResourceLocation registryName = BuiltInRegistries.ITEM.getKey(item); // never null in 1.21 (defaults to minecraft:air)

        if (registryName != null) {
            String modId = registryName.getNamespace();
            String itemName = registryName.getPath();
            return ResourceLocation.fromNamespaceAndPath(modId, "textures/item/" + itemName + ".png");
        }
        return Reference.id("textures/animated/attachment/bayonet.png");
    }
}