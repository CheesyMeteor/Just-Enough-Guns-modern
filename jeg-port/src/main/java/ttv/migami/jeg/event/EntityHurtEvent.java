package ttv.migami.jeg.event;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.entity.monster.phantom.terror.TerrorPhantom;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class EntityHurtEvent {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingAttack(LivingIncomingDamageEvent event) {
        LivingEntity hurtBoi = event.getEntity();

        if (hurtBoi.getRandom().nextFloat() > 0.3F) {
            if (hurtBoi.isPassenger() && hurtBoi.getVehicle() instanceof TerrorPhantom terrorPhantom) {
                terrorPhantom.hurt(event.getSource(), event.getAmount());
                event.setAmount(0);
                event.setCanceled(true);
            }
            if (hurtBoi instanceof TerrorPhantom terrorPhantom && terrorPhantom.getFirstPassenger() != null) {
                terrorPhantom.getFirstPassenger().hurt(event.getSource(), event.getAmount());
                event.setAmount(0);
                event.setCanceled(true);
            }
        }

    }
}
