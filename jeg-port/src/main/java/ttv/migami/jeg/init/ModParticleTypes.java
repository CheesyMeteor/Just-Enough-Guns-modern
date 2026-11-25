package ttv.migami.jeg.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.particles.BulletHoleData;
import ttv.migami.jeg.particles.ColoredFlareData;
import ttv.migami.jeg.particles.LaserData;
import ttv.migami.jeg.particles.TrailData;

/**
 * Author: MrCrayfish
 */
public class ModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> REGISTER = DeferredRegister.create(Registries.PARTICLE_TYPE, Reference.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, ParticleType<BulletHoleData>> BULLET_HOLE = REGISTER.register("bullet_hole", () -> createOptions(BulletHoleData.CODEC, false, BulletHoleData.STREAM_CODEC));
    public static final DeferredHolder<ParticleType<?>, ParticleType<LaserData>> LASER = REGISTER.register("laser", () -> createOptions(LaserData.CODEC, false, LaserData.STREAM_CODEC));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLOOD = REGISTER.register("blood", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, ParticleType<TrailData>> TRAIL = REGISTER.register("trail", () -> createOptions(TrailData.CODEC, false, TrailData.STREAM_CODEC));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> CASING_PARTICLE = REGISTER.register("casing", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SHELL_PARTICLE = REGISTER.register("shell", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPECTRE_CASING_PARTICLE = REGISTER.register("spectre_casing", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SCRAP = REGISTER.register("scrap", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HEALING_GLINT = REGISTER.register("healing_glint", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GHOST_FLAME = REGISTER.register("ghost_flame", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GHOST_GLINT = REGISTER.register("ghost_glint", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> TYPHOONEE_BEAM = REGISTER.register("typhoonee_beam", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SOUL_LAVA_PARTICLE = REGISTER.register("soul_lava", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SONIC_RING = REGISTER.register("sonic_ring", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BIG_SONIC_RING = REGISTER.register("big_sonic_ring", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLARE_SMOKE = REGISTER.register("flare_smoke", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, ParticleType<ColoredFlareData>> COLORED_FLARE_SMOKE = REGISTER.register("colored_flare_smoke", () -> createOptions(ColoredFlareData.CODEC, true, ColoredFlareData.STREAM_CODEC));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLARE = REGISTER.register("flare", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLUE_FLARE = REGISTER.register("flare_blue", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLAME = REGISTER.register("flame", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLUE_FLAME = REGISTER.register("blue_flame", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPARK = REGISTER.register("spark", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BIG_EXPLOSION = REGISTER.register("big_explosion", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMALL_EXPLOSION = REGISTER.register("small_explosion", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMOKE = REGISTER.register("smoke", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMOKE_CLOUD = REGISTER.register("smoke_cloud", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMOKE_EFFECT = REGISTER.register("smoke_effect", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FIRE = REGISTER.register("fire", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BUBBLE_AMMO = REGISTER.register("bubble_ammo", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ENTITY_LASER = REGISTER.register("entity_laser_glint", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> CONFETTI = REGISTER.register("confetti", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HIT_MARKER = REGISTER.register("hit_marker", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> POPCORN = REGISTER.register("popcorn", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PHANTOM_GUNNER = REGISTER.register("phantom_gunner", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PHANTOM_GUNNER_SWARM = REGISTER.register("phantom_gunner_swarm", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SECOND_LAYER_PHANTOM_GUNNER_SWARM = REGISTER.register("second_layer_phantom_gunner_swarm", () -> new SimpleParticleType(true));

    public static <T extends ParticleOptions> ParticleType<T> createOptions(MapCodec<T> codec, boolean overrideLimiter, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return new ParticleType<>(overrideLimiter) {
            @Override
            public MapCodec<T> codec() {
                return codec;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodec;
            }
        };
    }
}
