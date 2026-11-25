package ttv.migami.jeg.world.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.minecraft.core.registries.BuiltInRegistries;
import org.jetbrains.annotations.NotNull;
import ttv.migami.jeg.util.StackData;

import java.util.Random;
import java.util.function.Supplier;

public class PrimitiveBlowpipeOnJungleTempleModifier extends LootModifier {
    public static final Supplier<MapCodec<PrimitiveBlowpipeOnJungleTempleModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.mapCodec(inst -> codecStart(inst).and(BuiltInRegistries.ITEM.byNameCodec()
            .fieldOf("item").forGetter(m -> m.item)).apply(inst, PrimitiveBlowpipeOnJungleTempleModifier::new)));
    private final Item item;

    protected PrimitiveBlowpipeOnJungleTempleModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if(context.getRandom().nextFloat() > 0.0F) { // 100% chance of the item spawning in.
            int random = new Random().nextInt(1) + 1; //Min 1, Max 1.

            ItemStack itemStack = item.getDefaultInstance();
            StackData.putString(itemStack, "GunId", "jeg_cfg:primitive_blowpipe");

            generatedLoot.add(itemStack);
        }

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
