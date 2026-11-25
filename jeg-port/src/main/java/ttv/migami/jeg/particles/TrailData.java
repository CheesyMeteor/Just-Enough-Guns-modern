package ttv.migami.jeg.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import ttv.migami.jeg.init.ModParticleTypes;

/**
 * Author: MrCrayfish
 * <p>
 * PORT: see BulletHoleData for the 1.21 ParticleOptions codec notes.
 */
public class TrailData implements ParticleOptions
{
    public static final MapCodec<TrailData> CODEC = RecordCodecBuilder.mapCodec((builder) -> {
        return builder.group(Codec.BOOL.fieldOf("enchanted").forGetter((data) -> {
            return data.enchanted;
        })).apply(builder, TrailData::new);
    });

    public static final StreamCodec<RegistryFriendlyByteBuf, TrailData> STREAM_CODEC =
            StreamCodec.of((buf, data) -> buf.writeBoolean(data.enchanted), buf -> new TrailData(buf.readBoolean()));

    private final boolean enchanted;

    public TrailData(boolean enchanted)
    {
        this.enchanted = enchanted;
    }

    public boolean isEnchanted()
    {
        return this.enchanted;
    }

    @Override
    public ParticleType<?> getType()
    {
        return ModParticleTypes.TRAIL.get();
    }
}
