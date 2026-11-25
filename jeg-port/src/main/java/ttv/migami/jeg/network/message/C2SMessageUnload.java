package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import ttv.migami.jeg.common.network.ServerPlayHandler;

/**
 * Author: MrCrayfish
 */
public class C2SMessageUnload
{

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageUnload> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), C2SMessageUnload::decode);
    public static void encode(C2SMessageUnload message, RegistryFriendlyByteBuf buffer) {}

    public static C2SMessageUnload decode(RegistryFriendlyByteBuf buffer)
    {
        return new C2SMessageUnload();
    }

    public static void handle(C2SMessageUnload message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = (ServerPlayer) context.getPlayer().orElse(null);
            if(player != null && !player.isSpectator())
            {
                ServerPlayHandler.handleUnload(player);
            }
        });
        context.setHandled(true);
    }
}
