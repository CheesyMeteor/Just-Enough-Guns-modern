package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import ttv.migami.jeg.common.network.ServerPlayHandler;

/**
 * Author: MrCrayfish
 */
public class C2SMessageCraft
{

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageCraft> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), C2SMessageCraft::decode);
    private ResourceLocation id;
    private BlockPos pos;

    public C2SMessageCraft() {}

    public C2SMessageCraft(ResourceLocation id, BlockPos pos)
    {
        this.id = id;
        this.pos = pos;
    }

    public static void encode(C2SMessageCraft message, RegistryFriendlyByteBuf buffer)
    {
        buffer.writeResourceLocation(message.id);
        buffer.writeBlockPos(message.pos);
    }

    public static C2SMessageCraft decode(RegistryFriendlyByteBuf buffer)
    {
        return new C2SMessageCraft(buffer.readResourceLocation(), buffer.readBlockPos());
    }

    public static void handle(C2SMessageCraft message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = (ServerPlayer) context.getPlayer().orElse(null);
            if(player != null)
            {
                ServerPlayHandler.handleCraft(player, message.id, message.pos);
            }
        });
        context.setHandled(true);
    }
}
