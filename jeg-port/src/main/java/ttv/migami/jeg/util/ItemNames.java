package ttv.migami.jeg.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

/**
 * In 1.20.1 Item#toString() returned the registry path (e.g. "assault_rifle"). In 1.21 it returns the full
 * registry name ("jeg:assault_rifle"), which is not valid inside a resource path. Use this instead.
 */
public final class ItemNames {
    private ItemNames() {}

    public static String path(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }
}
