package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import ttv.migami.jeg.common.network.ServerPlayHandler;

/**
 * Author: MrCrayfish
 */
public class C2SMessageAttachments
{

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageAttachments> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), C2SMessageAttachments::decode);
    public C2SMessageAttachments() {}

    public static void encode(C2SMessageAttachments message, RegistryFriendlyByteBuf buffer) {}

    public static C2SMessageAttachments decode(RegistryFriendlyByteBuf buffer)
    {
        return new C2SMessageAttachments();
    }

    public static void handle(C2SMessageAttachments message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = (ServerPlayer) context.getPlayer().orElse(null);
            if(player != null)
            {
                ServerPlayHandler.handleAttachments(player);
            }
        });
        context.setHandled(true);
    }
}