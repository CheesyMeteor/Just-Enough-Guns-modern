package ttv.migami.jeg.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Replacement for the removed 1.20.1 ItemStack NBT API (getTag / getOrCreateTag / setTag / hasTag).
 * All custom item data lives in the vanilla CUSTOM_DATA data component, so behaviour matches the old
 * "stack tag" exactly. Reads return COPIES; to change data you MUST use {@link #edit} or {@link #set}.
 */
public final class StackData
{
    private StackData() {}

    /** Replacement for {@code stack.hasTag()}. */
    public static boolean has(ItemStack stack)
    {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data != null && !data.isEmpty();
    }

    /** Replacement for the nullable {@code stack.getTag()}. Returns a copy, or null when there is no data. */
    @Nullable
    public static CompoundTag getOrNull(ItemStack stack)
    {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null || data.isEmpty() ? null : data.copyTag();
    }

    /** Read-only replacement for {@code stack.getOrCreateTag()}. Returns a COPY (never null); mutating it does nothing. */
    public static CompoundTag get(ItemStack stack)
    {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null ? new CompoundTag() : data.copyTag();
    }

    /** Write replacement for {@code stack.getOrCreateTag().putX(...)}: mutate the tag inside the consumer. */
    public static void edit(ItemStack stack, Consumer<CompoundTag> editor)
    {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, editor);
    }

    /** Replacement for {@code stack.setTag(tag)}. Null or empty clears the data. */
    public static void set(ItemStack stack, @Nullable CompoundTag tag)
    {
        if(tag == null || tag.isEmpty())
        {
            stack.remove(DataComponents.CUSTOM_DATA);
            return;
        }
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    /** Replacement for {@code stack.removeTagKey(key)}. */
    public static void removeKey(ItemStack stack, String key)
    {
        if(contains(stack, key))
        {
            edit(stack, tag -> tag.remove(key));
        }
    }

    /** Replacement for {@code stack.hasTag() && stack.getTag().contains(key)}. */
    public static boolean contains(ItemStack stack, String key)
    {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data != null && data.contains(key);
    }

    public static int getInt(ItemStack stack, String key)
    {
        return get(stack).getInt(key);
    }

    public static boolean getBoolean(ItemStack stack, String key)
    {
        return get(stack).getBoolean(key);
    }

    public static String getString(ItemStack stack, String key)
    {
        return get(stack).getString(key);
    }

    public static float getFloat(ItemStack stack, String key)
    {
        return get(stack).getFloat(key);
    }

    public static void putInt(ItemStack stack, String key, int value)
    {
        edit(stack, tag -> tag.putInt(key, value));
    }

    public static void putBoolean(ItemStack stack, String key, boolean value)
    {
        edit(stack, tag -> tag.putBoolean(key, value));
    }

    public static void putString(ItemStack stack, String key, String value)
    {
        edit(stack, tag -> tag.putString(key, value));
    }

    public static void putFloat(ItemStack stack, String key, float value)
    {
        edit(stack, tag -> tag.putFloat(key, value));
    }
}
