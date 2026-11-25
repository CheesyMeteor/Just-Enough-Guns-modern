package ttv.migami.jeg.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import ttv.migami.jeg.event.GunReloadEvent;
import ttv.migami.jeg.init.ModSyncedDataKeys;
import ttv.migami.jeg.item.AnimatedGunItem;
import ttv.migami.jeg.util.StackData;

/**
 * Author: MrCrayfish
 */
public class C2SMessageReload
{
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageReload> STREAM_CODEC = StreamCodec.of((buf, msg) -> encode(msg, buf), C2SMessageReload::decode);
    private boolean reload;

    public C2SMessageReload() {}

    public C2SMessageReload(boolean reload)
    {
        this.reload = reload;
    }

    public static void encode(C2SMessageReload message, RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBoolean(message.reload);
    }

    public static C2SMessageReload decode(RegistryFriendlyByteBuf buffer)
    {
        return new C2SMessageReload(buffer.readBoolean());
    }

    public static void handle(C2SMessageReload message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = (ServerPlayer) context.getPlayer().orElse(null);
            if(player != null && !player.isSpectator())
            {
                ModSyncedDataKeys.RELOADING.setValue(player, message.reload); // This has to be set in order to verify the packet is sent if the event is cancelled
                if(!message.reload)
                    return;

                ItemStack gun = player.getMainHandItem();
                if(NeoForge.EVENT_BUS.post(new GunReloadEvent.Pre(player, gun)).isCanceled())
                {
                    ModSyncedDataKeys.RELOADING.setValue(player, false);
                    return;
                }
                if (gun.getItem() instanceof AnimatedGunItem && StackData.has(gun)) {
                    StackData.putBoolean(gun, "IsReloading", true);
                }
                NeoForge.EVENT_BUS.post(new GunReloadEvent.Post(player, gun));
            }
        });
        context.setHandled(true);
    }
}
