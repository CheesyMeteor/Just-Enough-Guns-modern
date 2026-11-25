package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import ttv.migami.jeg.client.medal.MedalManager;

/**
 * Author: MrCrayfish
 */
public class S2CMessageSendMedal
{

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CMessageSendMedal> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), S2CMessageSendMedal::decode);
    private int medal;

    public S2CMessageSendMedal() {}

    public S2CMessageSendMedal(int medal)
    {
        this.medal = medal;
    }

    public static void encode(S2CMessageSendMedal message, RegistryFriendlyByteBuf buffer)
    {
        buffer.writeInt(message.medal);
    }

    public static S2CMessageSendMedal decode(RegistryFriendlyByteBuf buffer)
    {
        return new S2CMessageSendMedal(buffer.readInt());
    }

    public static void handle(S2CMessageSendMedal message, MessageContext context)
    {
        context.execute(() -> MedalManager.addEnumMedal(message.medal));
        context.setHandled(true);
    }
}
