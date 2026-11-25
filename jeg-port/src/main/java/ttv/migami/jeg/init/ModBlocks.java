package ttv.migami.jeg.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.block.*;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks {

    private static final ToIntFunction<BlockState> light_Level_3 = BlockState -> 3;
    private static final ToIntFunction<BlockState> light_Level_7 = BlockState -> 7;

    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(Registries.BLOCK, Reference.MOD_ID);

    public static final DeferredHolder<Block, Block> AMMO_BOX = register("ammo_box",
            () -> new AmmoBoxBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHEST)
                    .strength(0.5F)));

    /*public static final DeferredHolder<Block, Block> BASIC_TURRET = register("basic_turret",
            () -> new BasicTurretBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_BLOCK)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()
                    .strength(5.0F)));*/

    public static final DeferredHolder<Block, Block> SCRAP_WORKBENCH = register("scrap_workbench",
            () -> new ScrapWorkbenchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .strength(1.5F)));
    public static final DeferredHolder<Block, Block> GUNMETAL_WORKBENCH = register("gunmetal_workbench",
            () -> new GunmetalWorkbenchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion()
                    .strength(3.0F)));
    public static final DeferredHolder<Block, Block> GUNNITE_WORKBENCH = register("gunnite_workbench",
            () -> new GunniteWorkbenchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .strength(5.0F)));
    public static final DeferredHolder<Block, Block> RECYCLER = register("recycler",
            () -> new RecyclerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()
                    .strength(2.0F)));
    public static final DeferredHolder<Block, Block> MAGAZINE_LOADER = register("magazine_loader",
            () -> new MagazineLoaderBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FLETCHING_TABLE)
                    .sound(net.minecraft.world.level.block.SoundType.METAL)));
    public static final DeferredHolder<Block, Block> POWERED_MAGAZINE_LOADER = register("powered_magazine_loader",
            () -> new PoweredMagazineLoaderBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .strength(3.5F)
                    .sound(net.minecraft.world.level.block.SoundType.METAL)
                    .lightLevel(state -> state.getValue(PoweredMagazineLoaderBlock.LIT) ? 8 : 0)));
    public static final DeferredHolder<Block, Block> SCRAP_BIN = register("scrap_bin",
            () -> new ScrapBinBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()
                    .strength(2.0F)));
    public static final DeferredHolder<Block, Block> SCHEMATIC_STATION = register("schematic_station",
            () -> new SchematicStationBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()
                    .strength(5.0F)));
    public static final DeferredHolder<Block, Block> BLUEPRINT_WORKBENCH = register("blueprint_workbench",
            () -> new BlueprintWorkbenchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()
                    .strength(5.0F)));

    public static final DeferredHolder<Block, Block> SCRAP_ORE = register("scrap_ore",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F)));
    public static final DeferredHolder<Block, Block> DEEPSLATE_SCRAP_ORE = register("deepslate_scrap_ore",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)
                    .requiresCorrectToolForDrops()
                    .strength(4.5F)));
    public static final DeferredHolder<Block, Block> SCRAP_BLOCK = register("scrap_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F)));
    public static final DeferredHolder<Block, Block> GUNMETAL_BLOCK = register("gunmetal_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK)
                    .requiresCorrectToolForDrops()
                    .strength(4.0F)));
    public static final DeferredHolder<Block, Block> GUNNITE_BLOCK = register("gunnite_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F)));
    public static final DeferredHolder<Block, Block> BRIMSTONE_ORE = register("brimstone_ore",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DRIPSTONE_BLOCK)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F)
                    .lightLevel(light_Level_3)));
    public static final DeferredHolder<Block, Block> BLACKSTONE_BRIMSTONE_ORE = register("blackstone_brimstone_ore",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACKSTONE)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F)
                    .lightLevel(light_Level_3)));
    public static final DeferredHolder<Block, Block> BASALT_BRIMSTONE_ORE = register("basalt_brimstone_ore",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BASALT)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F)
                    .lightLevel(light_Level_3)));

    public static final DeferredHolder<Block, Block> BOO_NEST = register("boo_nest",
            () -> new BooNest(Block.Properties.ofFullCopy(Blocks.BEE_NEST)
                    .lightLevel(light_Level_7)));
    public static final DeferredHolder<Block, Block> BOOHIVE = register("boohive",
            () -> new BooNest(Block.Properties.ofFullCopy(Blocks.BEEHIVE)
                    .lightLevel(light_Level_7)));

    public static final DeferredHolder<Block, Block> DYNAMIC_LIGHT = register("dynamic_light",
            DynamicLightBlock::new);
    public static final DeferredHolder<Block, Block> BRIGHT_DYNAMIC_LIGHT = register("bright_dynamic_light",
            BrightDynamicLightBlock::new);

    private static <T extends Block> DeferredHolder<Block, T> register(String id, Supplier<T> blockSupplier) {
        return register(id, blockSupplier, block1 -> new BlockItem(block1, new Item.Properties()));
    }

    private static <T extends Block> DeferredHolder<Block, T> register(String id, Supplier<T> blockSupplier, @Nullable Function<T, BlockItem> supplier) {
        DeferredHolder<Block, T> registryObject = REGISTER.register(id, blockSupplier);
        if (supplier != null) {
            ModItems.REGISTER.register(id, () -> supplier.apply(registryObject.get()));
        }
        return registryObject;
    }
}
