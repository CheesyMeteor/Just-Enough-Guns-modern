package ttv.migami.jeg.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.init.ModCommands;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class ModCommandsRegister {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }
}