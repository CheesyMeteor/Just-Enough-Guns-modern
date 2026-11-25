package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import ttv.migami.jeg.common.ReloadTracker;
import ttv.migami.jeg.common.network.ServerPlayHandler;

/**
 * Author: MrCrayfish
 */
public class C2SMessageGunUnjammed
{

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageGunUnjammed> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), C2SMessageGunUnjammed::decode);
    public static void encode(C2SMessageGunUnjammed message, RegistryFriendlyByteBuf buffer) {}

    public static C2SMessageGunUnjammed decode(RegistryFriendlyByteBuf buffer)
    {
        return new C2SMessageGunUnjammed();
    }

    public static void handle(C2SMessageGunUnjammed message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = (ServerPlayer) context.getPlayer().orElse(null);
            if(player != null && !player.isSpectator())
            {
                ServerPlayHandler.unjam(player);
            }
        });
        context.setHandled(true);
    }
}
