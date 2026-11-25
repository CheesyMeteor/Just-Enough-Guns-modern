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
public class C2SMessagePlayerRotation
{

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessagePlayerRotation> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), C2SMessagePlayerRotation::decode);
    private float rotationYaw;
    private float rotationPitch;

    public C2SMessagePlayerRotation() {}

    public C2SMessagePlayerRotation(Player player)
    {
        this.rotationYaw = player.getYRot();
        this.rotationPitch = player.getXRot();
    }

    public C2SMessagePlayerRotation(float rotationYaw, float rotationPitch)
    {
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
    }

    public static void encode(C2SMessagePlayerRotation message, RegistryFriendlyByteBuf buffer)
    {
        buffer.writeFloat(message.rotationYaw);
        buffer.writeFloat(message.rotationPitch);
    }

    public static C2SMessagePlayerRotation decode(RegistryFriendlyByteBuf buffer)
    {
        float rotationYaw = buffer.readFloat();
        float rotationPitch = buffer.readFloat();
        return new C2SMessagePlayerRotation(rotationYaw, rotationPitch);
    }

    public static void handle(C2SMessagePlayerRotation message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = (ServerPlayer) context.getPlayer().orElse(null);
            if(player != null)
            {
                ServerPlayHandler.rotatePhantom(message, player);
            }
        });
        context.setHandled(true);
    }

    public float getRotationYaw()
    {
        return this.rotationYaw;
    }

    public float getRotationPitch()
    {
        return this.rotationPitch;
    }
}