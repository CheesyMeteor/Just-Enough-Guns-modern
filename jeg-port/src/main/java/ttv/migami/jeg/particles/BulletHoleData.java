package ttv.migami.jeg.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import ttv.migami.jeg.init.ModParticleTypes;

/**
 * Author: MrCrayfish
 * <p>
 * PORT: ParticleOptions in 1.21 has no Deserializer/writeToNetwork/writeToString; the owning ParticleType
 * supplies a MapCodec + StreamCodec (see ModParticleTypes, createOptions(CODEC, flag, STREAM_CODEC)).
 */
public class BulletHoleData implements ParticleOptions
{
    public static final MapCodec<BulletHoleData> CODEC = RecordCodecBuilder.mapCodec((builder) -> {
        return builder.group(Codec.INT.fieldOf("dir").forGetter((data) -> {
            return data.direction.ordinal();
        }), Codec.LONG.fieldOf("pos").forGetter((data) -> {
            return data.pos.asLong();
        })).apply(builder, BulletHoleData::new);
    });

    public static final StreamCodec<RegistryFriendlyByteBuf, BulletHoleData> STREAM_CODEC = StreamCodec.of(
            (buf, data) -> {
                buf.writeEnum(data.direction);
                buf.writeBlockPos(data.pos);
            },
            buf -> new BulletHoleData(buf.readEnum(Direction.class), buf.readBlockPos())
    );

    private final Direction direction;
    private final BlockPos pos;

    public BulletHoleData(int dir, long pos)
    {
        this.direction = Direction.values()[dir];
        this.pos = BlockPos.of(pos);
    }

    public BulletHoleData(Direction dir, BlockPos pos)
    {
        this.direction = dir;
        this.pos = pos;
    }

    public Direction getDirection()
    {
        return this.direction;
    }

    public BlockPos getPos()
    {
        return this.pos;
    }

    @Override
    public ParticleType<?> getType()
    {
        return ModParticleTypes.BULLET_HOLE.get();
    }
}
