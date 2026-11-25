package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import ttv.migami.jeg.common.network.ServerPlayHandler;

/**
 * Author: MrCrayfish
 */
public class C2SMessageLeftOverAmmo
{

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageLeftOverAmmo> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), C2SMessageLeftOverAmmo::decode);
    public static void encode(C2SMessageLeftOverAmmo message, RegistryFriendlyByteBuf buffer) {}

    public static C2SMessageLeftOverAmmo decode(RegistryFriendlyByteBuf buffer)
    {
        return new C2SMessageLeftOverAmmo();
    }

    public static void handle(C2SMessageLeftOverAmmo message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = (ServerPlayer) context.getPlayer().orElse(null);
            if(player != null && !player.isSpectator())
            {
                ServerPlayHandler.handleExtraAmmo(player);
            }
        });
        context.setHandled(true);
    }
}
