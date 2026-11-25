package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import ttv.migami.jeg.event.GunEventBus;

/**
 * Author: MrCrayfish
 */
public class C2SMessageCasing
{

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageCasing> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), C2SMessageCasing::decode);
    public static void encode(C2SMessageCasing message, RegistryFriendlyByteBuf buffer) {}

    public static C2SMessageCasing decode(RegistryFriendlyByteBuf buffer)
    {
        return new C2SMessageCasing();
    }

    public static void handle(C2SMessageCasing message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = (ServerPlayer) context.getPlayer().orElse(null);
            if(player != null && !player.isSpectator())
            {
                GunEventBus.ejectCasing(player.level(), player);
            }
        });
        context.setHandled(true);
    }
}
