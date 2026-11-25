package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.client.medal.MedalManager;

/**
 * Author: MrCrayfish
 */
public class S2CMessageSendKillMedal
{
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CMessageSendKillMedal> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), S2CMessageSendKillMedal::decode);
    private static final ResourceLocation KILL_SINGLE = Reference.id("textures/gui/medal/multikill_kill_single.png");

    public static void encode(S2CMessageSendKillMedal message, RegistryFriendlyByteBuf buffer) {}

    public static S2CMessageSendKillMedal decode(RegistryFriendlyByteBuf buffer)
    {
        return new S2CMessageSendKillMedal();
    }

    public static void handle(S2CMessageSendKillMedal message, MessageContext context)
    {
        context.execute(() -> MedalManager.addKillMedal(KILL_SINGLE, Component.translatable("medal.jeg.multikill_kill_single")));
        context.setHandled(true);
    }
}
