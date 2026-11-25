package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import ttv.migami.jeg.common.network.ServerPlayHandler;

public class C2SMessageBurst {

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageBurst> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), C2SMessageBurst::decode);

    public static void encode(C2SMessageBurst message, RegistryFriendlyByteBuf buffer) {
    }

    public static C2SMessageBurst decode(RegistryFriendlyByteBuf buffer) {
        return new C2SMessageBurst();
    }

    public static void handle(C2SMessageBurst message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = (ServerPlayer) context.getPlayer().orElse(null);
            if(player != null)
            {
                ServerPlayHandler.handleBurst(player);
            }
        });
        context.setHandled(true);
    }

}
