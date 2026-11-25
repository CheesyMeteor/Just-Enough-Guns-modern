package ttv.migami.jeg.init;

import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ttv.migami.jeg.Reference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public final class ModPointOfInterestTypes
{
    public static final DeferredRegister<PoiType> REGISTER = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, Reference.MOD_ID);

    public static final DeferredHolder<PoiType, PoiType> BOOHIVE = register("boohive", ModBlocks.BOOHIVE, 1);

    public static final DeferredHolder<PoiType, PoiType> BOO_NEST = register("boo_nest", ModBlocks.BOO_NEST, 1);

    private static DeferredHolder<PoiType, PoiType> register(String name, Supplier<Block> block, int maxFreeTickets) {
        List<Supplier<Block>> blocks = new ArrayList<>();
        blocks.add(block);
        return register(name, blocks, maxFreeTickets);
    }

    private static DeferredHolder<PoiType, PoiType> register(String name, List<Supplier<Block>> blocks, int maxFreeTickets) {
        return REGISTER.register(name, () -> {
            Set<BlockState> blockStates = new HashSet<>();
            for (Supplier<Block> block : blocks) {
                blockStates.addAll(block.get().getStateDefinition().getPossibleStates());
            }
            return new PoiType(blockStates, maxFreeTickets, 1);
        });
    }
}
