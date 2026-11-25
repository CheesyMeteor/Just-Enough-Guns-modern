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
 * PORT: see BulletHoleData for the 1.21 ParticleOptions codec notes.
 */
public class ColoredFlareData implements ParticleOptions {
    public static final MapCodec<ColoredFlareData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("r").forGetter(d -> d.r),
            Codec.INT.fieldOf("g").forGetter(d -> d.g),
            Codec.INT.fieldOf("b").forGetter(d -> d.b)
    ).apply(instance, ColoredFlareData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ColoredFlareData> STREAM_CODEC = StreamCodec.of(
            (buf, d) -> {
                buf.writeInt(d.r);
                buf.writeInt(d.g);
                buf.writeInt(d.b);
            },
            buf -> new ColoredFlareData(buf.readInt(), buf.readInt(), buf.readInt())
    );

    private final int r, g, b;

    public ColoredFlareData(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public ParticleType<?> getType() {
        return ModParticleTypes.COLORED_FLARE_SMOKE.get();
    }

    public int getR() { return r; }
    public int getG() { return g; }
    public int getB() { return b; }
}
