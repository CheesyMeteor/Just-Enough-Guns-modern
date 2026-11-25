package ttv.migami.jeg.crafting.workbench;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;
import ttv.migami.jeg.Reference;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Author: MrCrayfish
 * <p>
 * An ingredient (vanilla item / tag ingredient) with an amount. Workbench recipe JSON is unchanged:
 * <pre>{ "item": "jeg:scrap", "count": 50 }   or   { "tag": "minecraft:planks", "count": 16 }</pre>
 * <p>
 * PORT: CraftingHelper / IIngredientSerializer no longer exist. This class is now a plain value class (the workbench
 * recipes hold it directly, it does not have to be a vanilla Ingredient anymore) with a {@link #CODEC} and
 * {@link #STREAM_CODEC}. For the (unused by the shipped JSON) "jeg:workbench_ingredient" ingredient type the nested
 * {@link Custom} {@link ICustomIngredient} + {@link IngredientType} is registered by the nested {@link Registration} subscriber.
 */
public class WorkbenchIngredient
{
    // PORT-VERIFY: NeoForgeRegistries.Keys.INGREDIENT_TYPES registry key name, IngredientType(MapCodec) ctor
    /** The "jeg:workbench_ingredient" ingredient type. Registered by {@link Registration} (no call from the mod constructor needed). */
    public static final IngredientType<Custom> WORKBENCH_INGREDIENT_TYPE = new IngredientType<>(Custom.CODEC);

    @EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static final class Registration
    {
        @SubscribeEvent
        public static void onRegister(RegisterEvent event)
        {
            event.register(NeoForgeRegistries.Keys.INGREDIENT_TYPES, Reference.id("workbench_ingredient"), () -> WORKBENCH_INGREDIENT_TYPE);
        }
    }

    /** Reads a vanilla ingredient object ("item" / "tag") from the SAME object that carries the optional "count". */
    public static final Codec<WorkbenchIngredient> CODEC = new Codec<>()
    {
        @Override
        public <T> DataResult<Pair<WorkbenchIngredient, T>> decode(DynamicOps<T> ops, T input)
        {
            // PORT-VERIFY: Ingredient.CODEC_NONEMPTY exists in 1.21.1 and ignores the extra "count" key
            return Ingredient.CODEC_NONEMPTY.decode(ops, input).map(pair -> {
                int count = ops.getMap(input).result()
                        .map(map -> map.get("count"))
                        .flatMap(value -> value == null ? Optional.<Number>empty() : ops.getNumberValue(value).result())
                        .map(Number::intValue)
                        .orElse(1);
                return Pair.of(new WorkbenchIngredient(pair.getFirst(), count), pair.getSecond());
            });
        }

        @Override
        public <T> DataResult<T> encode(WorkbenchIngredient input, DynamicOps<T> ops, T prefix)
        {
            return Ingredient.CODEC.encode(input.ingredient, ops, prefix)
                    .flatMap(encoded -> ops.mergeToMap(encoded, ops.createString("count"), ops.createInt(input.count)));
        }
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, WorkbenchIngredient> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, WorkbenchIngredient::getIngredient, // PORT-VERIFY: Ingredient.CONTENTS_STREAM_CODEC exists in 1.21.1
            ByteBufCodecs.VAR_INT, WorkbenchIngredient::getCount,
            WorkbenchIngredient::new);

    private final Ingredient ingredient;
    private final int count;

    public WorkbenchIngredient(Ingredient ingredient, int count)
    {
        this.ingredient = ingredient;
        this.count = count;
    }

    public Ingredient getIngredient()
    {
        return this.ingredient;
    }

    public int getCount()
    {
        return this.count;
    }

    public boolean test(ItemStack stack)
    {
        return this.ingredient.test(stack);
    }

    public ItemStack[] getItems()
    {
        return this.ingredient.getItems();
    }

    public boolean isEmpty()
    {
        return this.ingredient.isEmpty();
    }

    public static WorkbenchIngredient of(ItemLike provider, int count)
    {
        return new WorkbenchIngredient(Ingredient.of(provider), count);
    }

    public static WorkbenchIngredient of(ItemStack stack, int count)
    {
        return new WorkbenchIngredient(Ingredient.of(stack), count);
    }

    public static WorkbenchIngredient of(TagKey<Item> tag, int count)
    {
        return new WorkbenchIngredient(Ingredient.of(tag), count);
    }

    public static WorkbenchIngredient of(ResourceLocation id, int count)
    {
        return new WorkbenchIngredient(Ingredient.of(BuiltInRegistries.ITEM.get(id)), count);
    }

    /**
     * NeoForge custom ingredient form: {@code {"type": "jeg:workbench_ingredient", "item": "jeg:scrap", "count": 50}} (or "tag"),
     * usable anywhere a vanilla ingredient is accepted. Matches like the wrapped item/tag ingredient; the count is only
     * informative outside of workbenches (use {@link #toWorkbenchIngredient()}).
     * <p>
     * NOTE: this has its own MapCodec on purpose - decoding through {@link Ingredient#CODEC_NONEMPTY} would see the
     * "type" key again and recurse forever.
     */
    public record Custom(Optional<Item> item, Optional<TagKey<Item>> tag, int count) implements ICustomIngredient
    {
        public static final MapCodec<Custom> CODEC = RecordCodecBuilder.<Custom>mapCodec(instance -> instance.group(
                BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("item").forGetter(Custom::item),
                TagKey.codec(Registries.ITEM).optionalFieldOf("tag").forGetter(Custom::tag),
                Codec.INT.optionalFieldOf("count", 1).forGetter(Custom::count)
        ).apply(instance, Custom::new)).validate(custom -> custom.item.isPresent() != custom.tag.isPresent()
                ? DataResult.success(custom)
                : DataResult.error(() -> "workbench_ingredient needs exactly one of \"item\" or \"tag\""));

        public WorkbenchIngredient toWorkbenchIngredient()
        {
            Ingredient ingredient = this.item.map(i -> Ingredient.of(i)).orElseGet(() -> this.tag.map(t -> Ingredient.of(t)).orElse(Ingredient.EMPTY));
            return new WorkbenchIngredient(ingredient, this.count);
        }

        @Override
        public boolean test(ItemStack stack)
        {
            return this.toWorkbenchIngredient().test(stack);
        }

        @Override
        public Stream<ItemStack> getItems()
        {
            return Stream.of(this.toWorkbenchIngredient().getItems());
        }

        @Override
        public boolean isSimple()
        {
            return false;
        }

        @Override
        public IngredientType<?> getType()
        {
            return WORKBENCH_INGREDIENT_TYPE;
        }
    }
}
