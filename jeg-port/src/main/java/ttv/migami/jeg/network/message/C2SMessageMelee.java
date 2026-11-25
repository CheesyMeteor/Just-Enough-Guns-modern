package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import ttv.migami.jeg.common.network.ServerPlayHandler;

/**
 * Author: MrCrayfish
 */
public class C2SMessageMelee
{

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageMelee> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), C2SMessageMelee::decode);
    public static void encode(C2SMessageMelee message, RegistryFriendlyByteBuf buffer) {}

    public static C2SMessageMelee decode(RegistryFriendlyByteBuf buffer)
    {
        return new C2SMessageMelee();
    }

    public static void handle(C2SMessageMelee message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = (ServerPlayer) context.getPlayer().orElse(null);
            if(player != null && !player.isSpectator())
            {
                ServerPlayHandler.handleMelee(player);
            }
        });
        context.setHandled(true);
    }
}
