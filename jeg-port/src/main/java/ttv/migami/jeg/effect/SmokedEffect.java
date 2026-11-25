package ttv.migami.jeg.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import ttv.migami.jeg.Config;
import ttv.migami.jeg.init.ModEffects;

/**
 * Author: An0m3l1
 */
public class SmokedEffect extends IncurableEffect
{
    public SmokedEffect(MobEffectCategory typeIn, int liquidColorIn)
    {
        super(typeIn, liquidColorIn);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier)
    {
        double damage = Config.COMMON.smokeGrenades.smokeGrenadeDamage.get();
        if (!entity.getCommandSenderWorld().isClientSide && entity.hasEffect(ModEffects.SMOKED))
        {
            if(entity.getHealth() > 1.0F)
            {
                entity.hurt(entity.damageSources().magic(), (float) damage);
            }
            if(entity instanceof Mob mob)
            {
                mob.setTarget(null);
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier)
    {
        int i;
        i = 20 >> amplifier;
        if (i > 0)
        {
            return duration % i == 0;
        }
        else
        {
            return true;
        }
    }
}