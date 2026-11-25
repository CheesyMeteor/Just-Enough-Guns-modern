package ttv.migami.jeg.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;
import ttv.migami.jeg.client.KeyBinds;
import ttv.migami.jeg.init.ModItems;
import ttv.migami.jeg.item.AnimatedBowItem;
import ttv.migami.jeg.network.PacketHandler;
import ttv.migami.jeg.network.message.C2SMessageMelee;
import ttv.migami.jeg.util.StackData;

/**
 * Author: MrCrayfish
 */
public class MeleeHandler
{
    private static MeleeHandler instance;

    public static MeleeHandler get()
    {
        if(instance == null)
        {
            instance = new MeleeHandler();
        }
        return instance;
    }

    private MeleeHandler()
    {
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.Key event)
    {
        Player player = Minecraft.getInstance().player;
        if(player == null)
            return;

        if(KeyBinds.KEY_MELEE.isDown() && event.getAction() == GLFW.GLFW_PRESS)
        {
            if (StackData.getString(player.getMainHandItem(), "GunId").endsWith("blowpipe")) {
                return;
            }
            if (player.getMainHandItem().getItem() instanceof AnimatedBowItem || player.getMainHandItem().is(ModItems.MINIGUN.get())) {
                return;
            }

            PacketHandler.getPlayChannel().sendToServer(new C2SMessageMelee());
        }
    }
}
