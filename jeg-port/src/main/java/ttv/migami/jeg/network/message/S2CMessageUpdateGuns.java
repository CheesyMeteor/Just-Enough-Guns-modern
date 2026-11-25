package ttv.migami.jeg.network.message;

import com.google.common.collect.ImmutableMap;
import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.Validate;
import ttv.migami.jeg.client.network.ClientPlayHandler;
import ttv.migami.jeg.common.CustomGun;
import ttv.migami.jeg.common.CustomGunLoader;
import ttv.migami.jeg.common.Gun;
import ttv.migami.jeg.common.NetworkGunManager;

/**
 * Author: MrCrayfish
 */
public class S2CMessageUpdateGuns
{

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CMessageUpdateGuns> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), S2CMessageUpdateGuns::decode);
    private ImmutableMap<ResourceLocation, Gun> registeredGuns;
    private ImmutableMap<ResourceLocation, CustomGun> customGuns;

    public S2CMessageUpdateGuns() {}

    public static void encode(S2CMessageUpdateGuns message, RegistryFriendlyByteBuf buffer)
    {
        Validate.notNull(NetworkGunManager.get());
        Validate.notNull(CustomGunLoader.get());
        NetworkGunManager.get().writeRegisteredGuns(buffer);
        CustomGunLoader.get().writeCustomGuns(buffer);
    }

    public static S2CMessageUpdateGuns decode(RegistryFriendlyByteBuf buffer)
    {
        S2CMessageUpdateGuns message = new S2CMessageUpdateGuns();
        message.registeredGuns = NetworkGunManager.readRegisteredGuns(buffer);
        message.customGuns = CustomGunLoader.readCustomGuns(buffer);
        return message;
    }

    public static void handle(S2CMessageUpdateGuns message, MessageContext context)
    {
        context.execute(() -> ClientPlayHandler.handleUpdateGuns(message));
        context.setHandled(true);
    }

    public ImmutableMap<ResourceLocation, Gun> getRegisteredGuns()
    {
        return this.registeredGuns;
    }

    public ImmutableMap<ResourceLocation, CustomGun> getCustomGuns()
    {
        return this.customGuns;
    }
}