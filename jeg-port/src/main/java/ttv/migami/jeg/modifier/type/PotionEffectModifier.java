package ttv.migami.jeg.modifier.type;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

public class PotionEffectModifier implements IModifierEffect {
    private final Holder<MobEffect> effect;
    private final int duration; // in ticks
    private final int amplifier;

    public PotionEffectModifier(Holder<MobEffect> effect, int duration, int amplifier) {
        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public Holder<MobEffect> getEffect() {
        return effect;
    }

    public int getDuration() {
        return duration;
    }

    public int getAmplifier() {
        return amplifier;
    }
}