package ttv.migami.jeg.client;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.common.CustomGun;
import ttv.migami.jeg.init.ModItems;
import ttv.migami.jeg.network.message.S2CMessageUpdateGuns;
import ttv.migami.jeg.util.ItemStackNbt;
import ttv.migami.jeg.util.StackData;

import java.util.Map;

/**
 * Author: MrCrayfish
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class CustomGunManager
{
    private static Map<ResourceLocation, CustomGun> customGunMap;

    public static boolean updateCustomGuns(S2CMessageUpdateGuns message)
    {
        return updateCustomGuns(message.getCustomGuns());
    }

    private static boolean updateCustomGuns(Map<ResourceLocation, CustomGun> customGunMap)
    {
        CustomGunManager.customGunMap = customGunMap;
        return true;
    }

    public static void fill(CreativeModeTab.Output output)
    {
        if(customGunMap != null)
        {
            customGunMap.forEach((id, gun) ->
            {
                ItemStack stack = new ItemStack(ModItems.ASSAULT_RIFLE.get());
                stack.set(DataComponents.CUSTOM_NAME, Component.translatable("item." + id.getNamespace() + "." + id.getPath() + ".name"));
                CompoundTag tag = new CompoundTag();
                tag.put("Model", ItemStackNbt.save(gun.getModel()));
                tag.put("Gun", gun.getGun().serializeNBT());
                tag.putBoolean("Custom", true);
                tag.putInt("AmmoCount", gun.getGun().getReloads().getMaxAmmo());
                StackData.set(stack, tag);
                output.accept(stack);
            });
        }
    }

    @SubscribeEvent
    public static void onClientDisconnect(ClientPlayerNetworkEvent.LoggingOut event)
    {
        customGunMap = null;
    }
    // PORT-DROPPED: LoginData (Framework ILoginData no longer exists); guns are synced via S2CMessageUpdateGuns -> updateCustomGuns
}
