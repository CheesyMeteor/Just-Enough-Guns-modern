package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import ttv.migami.jeg.init.ModSyncedDataKeys;

/**
 * Author: MrCrayfish
 */
public class C2SMessageShooting
{

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageShooting> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), C2SMessageShooting::decode);
    private boolean shooting;

    public C2SMessageShooting() {}

    public C2SMessageShooting(boolean shooting)
    {
        this.shooting = shooting;
    }

    public static void encode(C2SMessageShooting message, RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBoolean(message.shooting);
    }

    public static C2SMessageShooting decode(RegistryFriendlyByteBuf buffer)
    {
        return new C2SMessageShooting(buffer.readBoolean());
    }

    public static void handle(C2SMessageShooting message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = (ServerPlayer) context.getPlayer().orElse(null);
            if(player != null)
            {
                ModSyncedDataKeys.SHOOTING.setValue(player, message.shooting);
            }
        });
        context.setHandled(true);
    }
}