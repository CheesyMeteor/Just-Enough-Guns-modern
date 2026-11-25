package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import ttv.migami.jeg.common.network.ServerPlayHandler;

/**
 * Author: MrCrayfish
 */
public class C2SMessageInspectGun
{

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageInspectGun> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), C2SMessageInspectGun::decode);
    public static void encode(C2SMessageInspectGun message, RegistryFriendlyByteBuf buffer) {}

    public static C2SMessageInspectGun decode(RegistryFriendlyByteBuf buffer)
    {
        return new C2SMessageInspectGun();
    }

    public static void handle(C2SMessageInspectGun message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = (ServerPlayer) context.getPlayer().orElse(null);
            if(player != null && !player.isSpectator())
            {
                ServerPlayHandler.handleInspectGun(player);
            }
        });
        context.setHandled(true);
    }
}
