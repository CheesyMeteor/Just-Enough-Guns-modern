package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import ttv.migami.jeg.client.network.ClientPlayHandler;

/**
 * Author: MrCrayfish
 */
public class S2CMessageExplosiveAmmo
{

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CMessageExplosiveAmmo> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), S2CMessageExplosiveAmmo::decode);
    private double x;
    private double y;
    private double z;

    public S2CMessageExplosiveAmmo() {}

    public S2CMessageExplosiveAmmo(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void encode(S2CMessageExplosiveAmmo message, RegistryFriendlyByteBuf buffer)
    {
        buffer.writeDouble(message.x);
        buffer.writeDouble(message.y);
        buffer.writeDouble(message.z);
    }

    public static S2CMessageExplosiveAmmo decode(RegistryFriendlyByteBuf buffer)
    {
        return new S2CMessageExplosiveAmmo(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    public static void handle(S2CMessageExplosiveAmmo message, MessageContext context)
    {
        context.execute(() -> ClientPlayHandler.handleExplosiveAmmo(message));
        context.setHandled(true);
    }

    public double getX()
    {
        return this.x;
    }

    public double getY()
    {
        return this.y;
    }

    public double getZ()
    {
        return this.z;
    }
}