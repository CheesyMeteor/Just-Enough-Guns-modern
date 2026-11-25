package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import ttv.migami.jeg.client.medal.MedalManager;

/**
 * Author: MrCrayfish
 */
public class S2CMessageSendHeadshot
{

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CMessageSendHeadshot> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), S2CMessageSendHeadshot::decode);
    private boolean headshot;

    public S2CMessageSendHeadshot() {}

    public S2CMessageSendHeadshot(boolean headshot)
    {
        this.headshot = headshot;
    }

    public static void encode(S2CMessageSendHeadshot message, RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBoolean(message.headshot);
    }

    public static S2CMessageSendHeadshot decode(RegistryFriendlyByteBuf buffer)
    {
        return new S2CMessageSendHeadshot(buffer.readBoolean());
    }

    public static void handle(S2CMessageSendHeadshot message, MessageContext context)
    {
        //context.execute(() -> MedalManager.setHeadshot(message.headshot));
        context.execute(() -> MedalManager.setHeadshot(true));
        context.setHandled(true);
    }
}
