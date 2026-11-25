package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import ttv.migami.jeg.event.FlashlightEvent;

/**
 * Author: MrCrayfish
 */
public class C2SMessageFlashlight
{

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageFlashlight> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), C2SMessageFlashlight::decode);
    public static void encode(C2SMessageFlashlight message, RegistryFriendlyByteBuf buffer) {}

    public static C2SMessageFlashlight decode(RegistryFriendlyByteBuf buffer)
    {
        return new C2SMessageFlashlight();
    }

    public static void handle(C2SMessageFlashlight message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = (ServerPlayer) context.getPlayer().orElse(null);
            if(player != null && !player.isSpectator())
            {
                FlashlightEvent.chargeFlashlight(player);
            }
        });
        context.setHandled(true);
    }
}
