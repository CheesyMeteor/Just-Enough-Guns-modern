package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import ttv.migami.jeg.common.network.ServerPlayHandler;

/**
 * Author: MrCrayfish
 */
public class C2SMessagePreFireSound
{

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessagePreFireSound> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), C2SMessagePreFireSound::decode);

    public C2SMessagePreFireSound() {}

    public C2SMessagePreFireSound(Player player)
    {
    }

    public static void encode(C2SMessagePreFireSound message, RegistryFriendlyByteBuf buffer)
    {

    }

    public static C2SMessagePreFireSound decode(RegistryFriendlyByteBuf buffer)
    {
        return new C2SMessagePreFireSound();
    }

    public static void handle(C2SMessagePreFireSound message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = (ServerPlayer) context.getPlayer().orElse(null);
            if(player != null)
            {
                ServerPlayHandler.handlePreFireSound(message, player);
            }
        });
        context.setHandled(true);
    }
}