package ttv.migami.jeg.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.EffectCure;
import net.minecraft.world.level.Level;
import ttv.migami.jeg.init.ModEffects;

import java.util.Set;

import static ttv.migami.jeg.common.network.ServerPlayHandler.sendParticlesToAll;

/**
 * Author: MrCrayfish
 */
public class IncurableEffect extends MobEffect
{
    public IncurableEffect(MobEffectCategory typeIn, int liquidColorIn)
    {
        super(typeIn, liquidColorIn);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        Level level = entity.level();
        if (level instanceof ServerLevel serverLevel && !entity.isDeadOrDying()) {
            if (entity.hasEffect(ModEffects.RESONANCE)) {
                sendParticlesToAll(
                        serverLevel,
                        ParticleTypes.ELECTRIC_SPARK,
                        true,
                        entity.getX(),
                        entity.getY() + entity.getEyeHeight(),
                        entity.getZ(),
                        1 + (1 * entity.getEffect(ModEffects.RESONANCE).getAmplifier()),
                        entity.getBbWidth() / 2, entity.getBbHeight() / 2, entity.getBbWidth() / 2,
                        0
                );
                if (level.random.nextFloat() < 0.2F) {
                    serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                            SoundEvents.NOTE_BLOCK_CHIME.value(), SoundSource.PLAYERS, 2.0F, 0.7F + level.random.nextFloat());
                }
            }
        }
        return true;
    }

    // PORT-VERIFY: NeoForge IMobEffectExtension#fillEffectCures(Set<EffectCure>, MobEffectInstance) replaces getCurativeItems (empty set = incurable)
    @Override
    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance)
    {
        cures.clear();
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
