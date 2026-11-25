package ttv.migami.jeg.network;

import com.mrcrayfish.framework.api.FrameworkAPI;
import com.mrcrayfish.framework.api.network.FrameworkNetwork;
import net.minecraft.network.protocol.PacketFlow;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.network.message.*;

public class PacketHandler
{
    private static FrameworkNetwork playChannel;

    public static void init()
    {
        playChannel = FrameworkAPI.createNetworkBuilder(Reference.id("play"), 1)
                .registerPlayMessage("aim", C2SMessageAim.class, C2SMessageAim.STREAM_CODEC, C2SMessageAim::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("reload", C2SMessageReload.class, C2SMessageReload.STREAM_CODEC, C2SMessageReload::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("shoot", C2SMessageShoot.class, C2SMessageShoot.STREAM_CODEC, C2SMessageShoot::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("pre_fire_sound", C2SMessagePreFireSound.class, C2SMessagePreFireSound.STREAM_CODEC, C2SMessagePreFireSound::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("burst", C2SMessageBurst.class, C2SMessageBurst.STREAM_CODEC, C2SMessageBurst::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("unload", C2SMessageUnload.class, C2SMessageUnload.STREAM_CODEC, C2SMessageUnload::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("stun_grenade", S2CMessageStunGrenade.class, S2CMessageStunGrenade.STREAM_CODEC, S2CMessageStunGrenade::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("smoke_grenade", S2CMessageSmokeGrenade.class, S2CMessageSmokeGrenade.STREAM_CODEC, S2CMessageSmokeGrenade::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("craft", C2SMessageCraft.class, C2SMessageCraft.STREAM_CODEC, C2SMessageCraft::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("bullet_trail", S2CMessageBulletTrail.class, S2CMessageBulletTrail.STREAM_CODEC, S2CMessageBulletTrail::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("attachments", C2SMessageAttachments.class, C2SMessageAttachments.STREAM_CODEC, C2SMessageAttachments::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("update_guns", S2CMessageUpdateGuns.class, S2CMessageUpdateGuns.STREAM_CODEC, S2CMessageUpdateGuns::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("blood", S2CMessageBlood.class, S2CMessageBlood.STREAM_CODEC, S2CMessageBlood::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("explosive_ammo", S2CMessageExplosiveAmmo.class, S2CMessageExplosiveAmmo.STREAM_CODEC, S2CMessageExplosiveAmmo::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("shooting", C2SMessageShooting.class, C2SMessageShooting.STREAM_CODEC, C2SMessageShooting::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("first_person_reload", C2SMessageFirstPersonReload.class, C2SMessageFirstPersonReload.STREAM_CODEC, C2SMessageFirstPersonReload::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("gun_sound", S2CMessageGunSound.class, S2CMessageGunSound.STREAM_CODEC, S2CMessageGunSound::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("projectile_hit_block", S2CMessageProjectileHitBlock.class, S2CMessageProjectileHitBlock.STREAM_CODEC, S2CMessageProjectileHitBlock::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("laser", S2CMessageLaser.class, S2CMessageLaser.STREAM_CODEC, S2CMessageLaser::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("projectile_hit_entity", S2CMessageProjectileHitEntity.class, S2CMessageProjectileHitEntity.STREAM_CODEC, S2CMessageProjectileHitEntity::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("remove_projectile", S2CMessageRemoveProjectile.class, S2CMessageRemoveProjectile.STREAM_CODEC, S2CMessageRemoveProjectile::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("left_over_ammo", C2SMessageLeftOverAmmo.class, C2SMessageLeftOverAmmo.STREAM_CODEC, C2SMessageLeftOverAmmo::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("melee", C2SMessageMelee.class, C2SMessageMelee.STREAM_CODEC, C2SMessageMelee::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("inspect_gun", C2SMessageInspectGun.class, C2SMessageInspectGun.STREAM_CODEC, C2SMessageInspectGun::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("casing", C2SMessageCasing.class, C2SMessageCasing.STREAM_CODEC, C2SMessageCasing::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("gun_unjammed", C2SMessageGunUnjammed.class, C2SMessageGunUnjammed.STREAM_CODEC, C2SMessageGunUnjammed::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("flashlight", C2SMessageFlashlight.class, C2SMessageFlashlight.STREAM_CODEC, C2SMessageFlashlight::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("send_medal", S2CMessageSendMedal.class, S2CMessageSendMedal.STREAM_CODEC, S2CMessageSendMedal::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("send_kill_medal", S2CMessageSendKillMedal.class, S2CMessageSendKillMedal.STREAM_CODEC, S2CMessageSendKillMedal::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("send_headshot", S2CMessageSendHeadshot.class, S2CMessageSendHeadshot.STREAM_CODEC, S2CMessageSendHeadshot::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("toggle_medals", C2SMessageToggleMedals.class, C2SMessageToggleMedals.STREAM_CODEC, C2SMessageToggleMedals::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("charge_sync", C2SMessageChargeSync.class, C2SMessageChargeSync.STREAM_CODEC, C2SMessageChargeSync::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("overheat", C2SMessageOverheat.class, C2SMessageOverheat.STREAM_CODEC, C2SMessageOverheat::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("burn_player", C2SMessageBurnPlayer.class, C2SMessageBurnPlayer.STREAM_CODEC, C2SMessageBurnPlayer::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("player_rotation", C2SMessagePlayerRotation.class, C2SMessagePlayerRotation.STREAM_CODEC, C2SMessagePlayerRotation::handle, PacketFlow.SERVERBOUND)
                .build();
    }

    public static FrameworkNetwork getPlayChannel()
    {
        return playChannel;
    }
}
