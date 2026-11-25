package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import ttv.migami.jeg.client.network.ClientPlayHandler;

/**
 * Author: MrCrayfish
 */
public class S2CMessageLaser
{

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CMessageLaser> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), S2CMessageLaser::decode);
    private double x;
    private double y;
    private double z;
    private BlockPos pos;
    private Direction face;

    public S2CMessageLaser() {}

    public S2CMessageLaser(double x, double y, double z, BlockPos pos, Direction face)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pos = pos;
        this.face = face;
    }

    public static void encode(S2CMessageLaser message, RegistryFriendlyByteBuf buffer)
    {
        buffer.writeDouble(message.x);
        buffer.writeDouble(message.y);
        buffer.writeDouble(message.z);
        buffer.writeBlockPos(message.pos);
        buffer.writeEnum(message.face);
    }

    public static S2CMessageLaser decode(RegistryFriendlyByteBuf buffer)
    {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        BlockPos pos = buffer.readBlockPos();
        Direction face = buffer.readEnum(Direction.class);
        return new S2CMessageLaser(x, y, z, pos, face);
    }

    public static void handle(S2CMessageLaser message, MessageContext context)
    {
        context.execute(() -> ClientPlayHandler.handleLaser(message));
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

    public BlockPos getPos()
    {
        return this.pos;
    }

    public Direction getFace()
    {
        return this.face;
    }
}