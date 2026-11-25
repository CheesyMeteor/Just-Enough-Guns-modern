package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import ttv.migami.jeg.common.network.ServerPlayHandler;

public class C2SMessageToggleMedals
{

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageToggleMedals> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), C2SMessageToggleMedals::decode);

    public C2SMessageToggleMedals() {}

    public static void encode(C2SMessageToggleMedals message, RegistryFriendlyByteBuf buffer)
    {
    }

    public static C2SMessageToggleMedals decode(RegistryFriendlyByteBuf buffer)
    {
        return new C2SMessageToggleMedals();
    }

    public static void handle(C2SMessageToggleMedals message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = (ServerPlayer) context.getPlayer().orElse(null);
            if(player != null && !player.isSpectator())
            {
                ServerPlayHandler.toggleMedals(player);
            }
        });
        context.setHandled(true);
    }
}