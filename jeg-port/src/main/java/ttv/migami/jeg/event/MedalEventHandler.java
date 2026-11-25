package ttv.migami.jeg.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import ttv.migami.jeg.Config;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.client.medal.MedalType;
import ttv.migami.jeg.item.GunItem;
import ttv.migami.jeg.network.PacketHandler;
import ttv.migami.jeg.network.message.S2CMessageSendKillMedal;
import ttv.migami.jeg.network.message.S2CMessageSendMedal;
import ttv.migami.jeg.util.StackData;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class MedalEventHandler {

    @SubscribeEvent
    public static void onEntityKilled(LivingDeathEvent event) {
        if(Config.COMMON.gameplay.overrideHideMedals.get())
            return;

        if (event.getEntity().level().isClientSide) {
            return;
        }

        LivingEntity entity = event.getEntity();
        LivingEntity killer = entity.getKillCredit();

        if (killer == null) {
            return;
        }

        if (StackData.has(killer.getMainHandItem()) && !StackData.getBoolean(killer.getMainHandItem(), "MedalsEnabled")) {
            return;
        }

        //if (!(entity instanceof NeutralMob) && !(entity instanceof Enemy) && !(entity instanceof Player)) {
        if (!(entity instanceof Enemy) && !(entity instanceof Player)) {
            return;
        }

        if (!(event.getSource().getEntity() instanceof Player player)) {
            return;
        }

        if (event.getSource().is(DamageTypes.EXPLOSION) || event.getSource().is(DamageTypes.PLAYER_EXPLOSION))
            PacketHandler.getPlayChannel().sendToPlayer(() -> (ServerPlayer) player, new S2CMessageSendMedal(MedalType.GEAR_BOOM.ordinal()));

        if (!(killer.getMainHandItem().getItem() instanceof GunItem)) {
            return;
        }

        player.level().getServer().execute(() -> PacketHandler.getPlayChannel().sendToPlayer(() -> (ServerPlayer) player, new S2CMessageSendKillMedal()));

        if (!StackData.has(player.getMainHandItem())) {
            return;
        }

        if (entity instanceof Creeper creeper && creeper.swell != 0) {
            PacketHandler.getPlayChannel().sendToPlayer(() -> (ServerPlayer) player, new S2CMessageSendMedal(MedalType.COMBAT_HUSH.ordinal()));
        }

        if (entity.isOnFire())
            PacketHandler.getPlayChannel().sendToPlayer(() -> (ServerPlayer) player, new S2CMessageSendMedal(MedalType.GEAR_BBQ.ordinal()));


        if (entity.getTags().contains("EliteGunner"))
            PacketHandler.getPlayChannel().sendToPlayer(() -> (ServerPlayer) player, new S2CMessageSendMedal(MedalType.COMBAT_KINGSLAYER.ordinal()));

        if (StackData.getInt(player.getMainHandItem(), "AmmoCount") < 1 && !player.isCreative())
            PacketHandler.getPlayChannel().sendToPlayer(() -> (ServerPlayer) player, new S2CMessageSendMedal(MedalType.COMBAT_JUST_ENOUGH_AMMO.ordinal()));
    }
}