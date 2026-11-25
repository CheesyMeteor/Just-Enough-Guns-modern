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
import ttv.migami.jeg.Reference;

import java.util.function.Supplier;

public class HollenfireMK2BPOnBarteringModifier extends LootModifier {
    public static final Supplier<MapCodec<HollenfireMK2BPOnBarteringModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.mapCodec(inst -> codecStart(inst).and(BuiltInRegistries.ITEM.byNameCodec()
            .fieldOf("item").forGetter(m -> m.item)).apply(inst, HollenfireMK2BPOnBarteringModifier::new)));
    private final Item item;

    protected HollenfireMK2BPOnBarteringModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if(context.getRandom().nextFloat() > 0.95F) { // 5% chance of the item spawning in.

            ItemStack itemStack = item.getDefaultInstance();
            StackData.putString(itemStack, "Namespace", Reference.MOD_ID);
            StackData.putString(itemStack, "Path", "hollenfire_mk2");

            generatedLoot.add(itemStack);
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
