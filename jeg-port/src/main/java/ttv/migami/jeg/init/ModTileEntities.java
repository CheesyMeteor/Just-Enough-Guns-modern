package ttv.migami.jeg.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.blockentity.*;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class ModTileEntities
{
    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Reference.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ScrapWorkbenchBlockEntity>> SCRAP_WORKBENCH = register("scrap_workbench", ScrapWorkbenchBlockEntity::new,
            () -> new Block[]{ModBlocks.SCRAP_WORKBENCH.get()});
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GunmetalWorkbenchBlockEntity>> GUNMETAL_WORKBENCH = register("gunmetal_workbench", GunmetalWorkbenchBlockEntity::new,
            () -> new Block[]{ModBlocks.GUNMETAL_WORKBENCH.get()});
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GunniteWorkbenchBlockEntity>> GUNNITE_WORKBENCH = register("gunnite_workbench", GunniteWorkbenchBlockEntity::new,
            () -> new Block[]{ModBlocks.GUNNITE_WORKBENCH.get()});
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlueprintWorkbenchBlockEntity>> BLUEPRINT_WORKBENCH = register("blueprint_workbench", BlueprintWorkbenchBlockEntity::new,
            () -> new Block[]{ModBlocks.BLUEPRINT_WORKBENCH.get()});
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RecyclerBlockEntity>> RECYCLER = register("recycler", RecyclerBlockEntity::new,
            () -> new Block[]{ModBlocks.RECYCLER.get()});

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MagazineLoaderBlockEntity>> MAGAZINE_LOADER = register("magazine_loader", MagazineLoaderBlockEntity::new,
            () -> new Block[]{ModBlocks.MAGAZINE_LOADER.get()});

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PoweredMagazineLoaderBlockEntity>> POWERED_MAGAZINE_LOADER = register("powered_magazine_loader", PoweredMagazineLoaderBlockEntity::new,
            () -> new Block[]{ModBlocks.POWERED_MAGAZINE_LOADER.get()});

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BooNestBlockEntity>> BOO_NEST = REGISTER.register("boo_nest", () ->
            BlockEntityType.Builder.of(BooNestBlockEntity::new,
                    ModBlocks.BOO_NEST.get(),
                    ModBlocks.BOOHIVE.get()
            ).build(null)
    );

    /*public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BasicTurretBlockEntity>> BASIC_TURRET = register("basic_turret", BasicTurretBlockEntity::new,
            () -> new Block[]{ModBlocks.BASIC_TURRET.get()});*/

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AmmoBoxBlockEntity>> AMMO_BOX = register("ammo_box", AmmoBoxBlockEntity::new,
            () -> new Block[]{ModBlocks.AMMO_BOX.get()});

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DynamicLightBlockEntity>> DYNAMIC_LIGHT = register("dynamic_light", DynamicLightBlockEntity::new,
            () -> new Block[]{ModBlocks.DYNAMIC_LIGHT.get(), ModBlocks.BRIGHT_DYNAMIC_LIGHT.get()});

    private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String id, BlockEntityType.BlockEntitySupplier<T> factoryIn, Supplier<Block[]> validBlocksSupplier)
    {
        return REGISTER.register(id, () -> BlockEntityType.Builder.of(factoryIn, validBlocksSupplier.get()).build(null));
    }

}
