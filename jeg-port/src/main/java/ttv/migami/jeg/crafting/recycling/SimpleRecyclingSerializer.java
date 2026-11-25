package ttv.migami.jeg.crafting.recycling;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import ttv.migami.jeg.crafting.workbench.AbstractWorkbenchRecipeSerializer;

/**
 * Accepts the ORIGINAL recycling JSON:
 * <pre>
 * { "type": "jeg:recycling", "group": "" (optional),
 *   "ingredient": { "item": "minecraft:hopper" } (object or array of objects, vanilla ingredient syntax),
 *   "result": { "item": "jeg:scrap", "count": 5 }  OR  "jeg:scrap" (plain item id string),
 *   "experience": 0 (optional), "recyclingtime": 50 (optional, default 1200) }
 * </pre>
 */
public class SimpleRecyclingSerializer<T extends AbstractRecyclingRecipe> implements RecipeSerializer<T>
{
   /** Result as an object ({"item","count"} or vanilla {"id","count"}) or as a bare item id string. */
   private static final Codec<ItemStack> RESULT_CODEC = Codec.withAlternative(
           AbstractWorkbenchRecipeSerializer.RESULT_CODEC,
           BuiltInRegistries.ITEM.byNameCodec().xmap(ItemStack::new, ItemStack::getItem));

   private final MapCodec<T> codec;
   private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

   public SimpleRecyclingSerializer(SimpleRecyclingSerializer.CookieBaker<T> tCookieBaker, int i) {
      this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
              Codec.STRING.optionalFieldOf("group", "").forGetter(AbstractRecyclingRecipe::getGroup),
              Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(AbstractRecyclingRecipe::getIngredient),
              RESULT_CODEC.fieldOf("result").forGetter(AbstractRecyclingRecipe::getItem),
              Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(AbstractRecyclingRecipe::getExperience),
              Codec.INT.optionalFieldOf("recyclingtime", i).forGetter(AbstractRecyclingRecipe::getRecyclingTime)
      ).apply(instance, tCookieBaker::create));
      this.streamCodec = StreamCodec.composite(
              ByteBufCodecs.STRING_UTF8, AbstractRecyclingRecipe::getGroup,
              Ingredient.CONTENTS_STREAM_CODEC, AbstractRecyclingRecipe::getIngredient,
              ItemStack.STREAM_CODEC, AbstractRecyclingRecipe::getItem,
              ByteBufCodecs.FLOAT, AbstractRecyclingRecipe::getExperience,
              ByteBufCodecs.VAR_INT, AbstractRecyclingRecipe::getRecyclingTime,
              tCookieBaker::create);
   }

   @Override
   public MapCodec<T> codec() {
      return this.codec;
   }

   @Override
   public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
      return this.streamCodec;
   }

   public interface CookieBaker<T extends AbstractRecyclingRecipe> {
      T create(String string, Ingredient ingredient, ItemStack itemStack, float v, int i);
   }
}
