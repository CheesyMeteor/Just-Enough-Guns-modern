package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import ttv.migami.jeg.common.network.ServerPlayHandler;

/**
 * Author: MrCrayfish
 */
public class C2SMessageBurnPlayer
{

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageBurnPlayer> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), C2SMessageBurnPlayer::decode);
    public static void encode(C2SMessageBurnPlayer message, RegistryFriendlyByteBuf buffer) {}

    public static C2SMessageBurnPlayer decode(RegistryFriendlyByteBuf buffer)
    {
        return new C2SMessageBurnPlayer();
    }

    public static void handle(C2SMessageBurnPlayer message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = (ServerPlayer) context.getPlayer().orElse(null);
            if(player != null && !player.isSpectator())
            {
                ServerPlayHandler.burnPlayer(player);
            }
        });
        context.setHandled(true);
    }
}
