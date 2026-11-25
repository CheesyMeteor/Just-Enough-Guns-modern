package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import ttv.migami.jeg.client.network.ClientPlayHandler;

public class S2CMessageSmokeGrenade
{

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CMessageSmokeGrenade> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), S2CMessageSmokeGrenade::decode);
    private double x, y, z;

    public S2CMessageSmokeGrenade() {}

    public S2CMessageSmokeGrenade(double x, double y, double z)
    {
        this.z = z;
        this.y = y;
        this.x = x;
    }

    public static void encode(S2CMessageSmokeGrenade message, RegistryFriendlyByteBuf buffer)
    {
        buffer.writeDouble(message.x);
        buffer.writeDouble(message.y);
        buffer.writeDouble(message.z);
    }

    public static S2CMessageSmokeGrenade decode(RegistryFriendlyByteBuf buffer)
    {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        return new S2CMessageSmokeGrenade(x, y, z);
    }

    public static void handle(S2CMessageSmokeGrenade message, MessageContext context)
    {
        context.execute(() -> ClientPlayHandler.handleExplosionSmokeGrenade(message));
        context.setHandled(true);
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getZ()
    {
        return z;
    }
}