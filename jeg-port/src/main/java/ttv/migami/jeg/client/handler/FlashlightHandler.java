package ttv.migami.jeg.client.handler;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import ttv.migami.jeg.Config;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.client.KeyBinds;
import ttv.migami.jeg.item.FlashlightItem;
import ttv.migami.jeg.network.PacketHandler;
import ttv.migami.jeg.network.message.C2SMessageFlashlight;

@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class FlashlightHandler
{
    private static FlashlightHandler instance;

    public static FlashlightHandler get()
    {
        if(instance == null)
        {
            instance = new FlashlightHandler();
        }
        return instance;
    }

    private FlashlightHandler()
    {
    }

    // PORT: TickEvent.PlayerTickEvent fired in both phases (START and END); Pre and Post both forward to the same logic to keep that cadence
    @SubscribeEvent
    public static void flashlightPre(PlayerTickEvent.Pre event) {
        flashlight(event.getEntity());
    }

    @SubscribeEvent
    public static void flashlightPost(PlayerTickEvent.Post event) {
        flashlight(event.getEntity());
    }

    private static void flashlight(Player player) {
        // PORT: the original also reacted to every OTHER player's tick on the client; only the local player may send the packet
        if(!player.isLocalPlayer())
            return;

        Minecraft mc = Minecraft.getInstance();
        ItemStack heldItem = player.getMainHandItem();
        if(heldItem.getItem() instanceof FlashlightItem)
        {
            if (!Config.COMMON.gameplay.allowFlashlights.get()) {
                Component message = Component.translatable("chat.jeg.disabled_flashlights")
                        .withStyle(ChatFormatting.GRAY);
                player.displayClientMessage(message, true);
                return;
            }

            // PORT-DROPPED: Controllable (ControllerHandler.isShooting)
            boolean charging = KeyBinds.getShootMapping().isDown();
            if(charging)
            {
                PacketHandler.getPlayChannel().sendToServer(new C2SMessageFlashlight());
                mc.options.keyAttack.setDown(false);
                KeyBinds.getShootMapping().setDown(false);
            }
        }
    }
}
