package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import ttv.migami.jeg.common.network.ServerPlayHandler;

/**
 * Author: MrCrayfish
 */
public class C2SMessageFirstPersonReload
{

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageFirstPersonReload> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), C2SMessageFirstPersonReload::decode);
    private boolean firstPerson;

    public C2SMessageFirstPersonReload() {}

    public C2SMessageFirstPersonReload(boolean firstPerson)
    {
        this.firstPerson = firstPerson;
    }

    public static void encode(C2SMessageFirstPersonReload message, RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBoolean(message.firstPerson);
    }

    public static C2SMessageFirstPersonReload decode(RegistryFriendlyByteBuf buffer)
    {
        return new C2SMessageFirstPersonReload(buffer.readBoolean());
    }

    public static void handle(C2SMessageFirstPersonReload message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = (ServerPlayer) context.getPlayer().orElse(null);
            if(player != null)
            {
                ServerPlayHandler.handleReloadPerspective(player, message.firstPerson);
            }
        });
        context.setHandled(true);
    }
}