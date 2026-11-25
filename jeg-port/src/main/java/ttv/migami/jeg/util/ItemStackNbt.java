package ttv.migami.jeg.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

/**
 * PORT: 1.20.1 {@code ItemStack.of(tag)} / {@code stack.save(tag)} need no registry access; 1.21.1 does.
 * Static legacy code (Gun attachment helpers etc.) has no level at hand, so this resolves a provider from the
 * running server, then the client level, then the vanilla built-in lookup as a last resort.
 */
public final class ItemStackNbt
{
    private ItemStackNbt() {}

    public static HolderLookup.Provider provider()
    {
        var server = ServerLifecycleHooks.getCurrentServer();
        if(server != null)
        {
            return server.registryAccess();
        }
        if(FMLEnvironment.dist == Dist.CLIENT)
        {
            HolderLookup.Provider p = ClientAccess.get();
            if(p != null)
            {
                return p;
            }
        }
        return VanillaRegistries.createLookup(); // PORT-VERIFY: fallback only; enchantment components need a real RegistryAccess
    }

    /** Replacement for {@code ItemStack.of(tag)}. Returns EMPTY for empty/invalid data. */
    public static ItemStack parse(CompoundTag tag)
    {
        if(tag == null || tag.isEmpty())
        {
            return ItemStack.EMPTY;
        }
        return ItemStack.parse(provider(), tag).orElse(ItemStack.EMPTY);
    }

    /** Replacement for {@code stack.save(new CompoundTag())}. */
    public static CompoundTag save(ItemStack stack)
    {
        if(stack.isEmpty())
        {
            return new CompoundTag();
        }
        Tag saved = stack.save(provider());
        return saved instanceof CompoundTag c ? c : new CompoundTag();
    }

    private static final class ClientAccess
    {
        static HolderLookup.Provider get()
        {
            Minecraft mc = Minecraft.getInstance();
            return mc.level != null ? mc.level.registryAccess() : null;
        }
    }
}
