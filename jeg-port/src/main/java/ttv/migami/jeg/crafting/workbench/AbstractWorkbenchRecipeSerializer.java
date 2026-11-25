package ttv.migami.jeg.crafting.workbench;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

/**
 * Parses the ORIGINAL workbench recipe JSON:
 * <pre>
 * { "type": "jeg:scrap_workbench",
 *   "materials": [ { "item": "jeg:scrap", "count": 50 }, { "tag": "minecraft:planks", "count": 16 } ],
 *   "result": { "item": "jeg:assault_rifle", "count": 1 },   // count optional; vanilla-style {"id": ...} also accepted
 *   "time": 30 }                                              // ignored (as before)
 * </pre>
 */
public abstract class AbstractWorkbenchRecipeSerializer<T extends AbstractWorkbenchRecipe<?>> implements RecipeSerializer<T> {
    /** Result stack in the old format: {"item": "...", "count": n}. */
    private static final Codec<ItemStack> LEGACY_RESULT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(ItemStack::getItem),
            Codec.INT.optionalFieldOf("count", 1).forGetter(ItemStack::getCount)
    ).apply(instance, (item, count) -> new ItemStack(item, count)));
    public static final Codec<ItemStack> RESULT_CODEC = Codec.withAlternative(LEGACY_RESULT_CODEC, ItemStack.CODEC);

    private final MapCodec<T> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

    protected AbstractWorkbenchRecipeSerializer() {
        this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                RESULT_CODEC.fieldOf("result").forGetter(AbstractWorkbenchRecipe::getItem),
                WorkbenchIngredient.CODEC.listOf().fieldOf("materials").forGetter(recipe -> (List<WorkbenchIngredient>) recipe.getMaterials())
        ).apply(instance, (result, materials) -> this.createRecipe(result, ImmutableList.copyOf(materials))));
        this.streamCodec = StreamCodec.composite(
                ItemStack.STREAM_CODEC, AbstractWorkbenchRecipe::getItem,
                WorkbenchIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), recipe -> (List<WorkbenchIngredient>) recipe.getMaterials(),
                (result, materials) -> this.createRecipe(result, ImmutableList.copyOf(materials)));
    }

    @Override
    public MapCodec<T> codec() {
        return this.codec;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
        return this.streamCodec;
    }

    protected abstract T createRecipe(ItemStack result, ImmutableList<WorkbenchIngredient> materials);
}
