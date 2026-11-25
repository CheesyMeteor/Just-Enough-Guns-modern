package ttv.migami.jeg.common;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import ttv.migami.jeg.client.util.Easings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public class JsonDeserializers
{
    public static final JsonDeserializer<ItemStack> ITEM_STACK = (json, typeOfT, context) ->
    {
        // 1.21 item stack JSON uses "id" instead of "item"; keep accepting the old key
        JsonObject object = json.getAsJsonObject().deepCopy();
        if(object.has("item") && !object.has("id"))
        {
            object.add("id", object.remove("item"));
        }
        return ItemStack.CODEC.parse(JsonOps.INSTANCE, object).getOrThrow(JsonParseException::new); // PORT-VERIFY: ItemStack.CODEC with plain JsonOps (no RegistryOps) resolves item ids
    };
    public static final JsonDeserializer<ResourceLocation> RESOURCE_LOCATION = (json, typeOfT, context) -> ResourceLocation.parse(json.getAsString());
    public static final JsonDeserializer<FireMode> FIRE_MODE = (json, typeOfT, context) -> FireMode.getType(ResourceLocation.tryParse(json.getAsString()));
    public static final JsonDeserializer<ReloadType> RELOAD_TYPE = (json, typeOfT, context) -> ReloadType.getType(ResourceLocation.tryParse(json.getAsString()));
    public static final JsonDeserializer<GripType> GRIP_TYPE = (json, typeOfT, context) -> GripType.getType(ResourceLocation.tryParse(json.getAsString()));
    public static final JsonDeserializer<Easings> EASING = (json, typeOfT, context) -> Easings.byName(json.getAsString());
}
