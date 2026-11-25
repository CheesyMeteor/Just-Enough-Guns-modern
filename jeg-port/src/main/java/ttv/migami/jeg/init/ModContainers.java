package ttv.migami.jeg.init;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.blockentity.BlueprintWorkbenchBlockEntity;
import ttv.migami.jeg.blockentity.GunmetalWorkbenchBlockEntity;
import ttv.migami.jeg.blockentity.GunniteWorkbenchBlockEntity;
import ttv.migami.jeg.blockentity.ScrapWorkbenchBlockEntity;
import ttv.migami.jeg.common.container.*;
import ttv.migami.jeg.common.container.recycler.RecyclerMenu;

/**
 * Author: MrCrayfish
 */
public class ModContainers {
    public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(Registries.MENU, Reference.MOD_ID);
    // PORT-VERIFY: IMenuTypeExtension.create factory (int, Inventory, RegistryFriendlyByteBuf) - pattern copied from ref port ModMenuTypes

    public static final DeferredHolder<MenuType<?>, MenuType<ScrapWorkbenchContainer>> SCRAP_WORKBENCH = REGISTER.register("scrap_workbench", () -> IMenuTypeExtension.create((windowId, playerInventory, data) -> {
        ScrapWorkbenchBlockEntity scrap_workbench = (ScrapWorkbenchBlockEntity) playerInventory.player.level().getBlockEntity(data.readBlockPos());
        return new ScrapWorkbenchContainer(windowId, playerInventory, scrap_workbench);
    }));
    public static final DeferredHolder<MenuType<?>, MenuType<GunmetalWorkbenchContainer>> GUNMETAL_WORKBENCH = REGISTER.register("gunmetal_workbench", () -> IMenuTypeExtension.create((windowId, playerInventory, data) -> {
        GunmetalWorkbenchBlockEntity gunmetal_workbench = (GunmetalWorkbenchBlockEntity) playerInventory.player.level().getBlockEntity(data.readBlockPos());
        return new GunmetalWorkbenchContainer(windowId, playerInventory, gunmetal_workbench);
    }));
    public static final DeferredHolder<MenuType<?>, MenuType<GunniteWorkbenchContainer>> GUNNITE_WORKBENCH = REGISTER.register("gunnite_workbench", () -> IMenuTypeExtension.create((windowId, playerInventory, data) -> {
        GunniteWorkbenchBlockEntity gunnite_workbench = (GunniteWorkbenchBlockEntity) playerInventory.player.level().getBlockEntity(data.readBlockPos());
        return new GunniteWorkbenchContainer(windowId, playerInventory, gunnite_workbench);
    }));
    public static final DeferredHolder<MenuType<?>, MenuType<BlueprintWorkbenchContainer>> BLUEPRINT_WORKBENCH = REGISTER.register("blueprint_workbench", () -> IMenuTypeExtension.create((windowId, playerInventory, data) -> {
        BlueprintWorkbenchBlockEntity blueprint_workbench = (BlueprintWorkbenchBlockEntity) playerInventory.player.level().getBlockEntity(data.readBlockPos());
        return new BlueprintWorkbenchContainer(windowId, playerInventory, blueprint_workbench);
    }));

    public static final DeferredHolder<MenuType<?>, MenuType<SchematicStationMenu>> SCHEMATIC_STATION = register("schematic_station", SchematicStationMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<AttachmentContainer>> ATTACHMENTS = register("attachments", AttachmentContainer::new);

    public static final DeferredHolder<MenuType<?>, MenuType<RecyclerMenu>> RECYCLER = register("recycler", RecyclerMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<MagazineLoaderMenu>> MAGAZINE_LOADER = register("magazine_loader", MagazineLoaderMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<PoweredMagazineLoaderMenu>> POWERED_MAGAZINE_LOADER = register("powered_magazine_loader", PoweredMagazineLoaderMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<AmmoBoxMenu>> AMMO_BOX = register("ammo_box", AmmoBoxMenu::new);

    /*public static final DeferredHolder<MenuType<?>, MenuType<BasicTurretContainer>> BASIC_TURRET_CONTAINER =
            REGISTER.register("basic_turret_container", () -> IForgeMenuType.create(BasicTurretContainer::new));*/

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String id, MenuType.MenuSupplier<T> factory) {
        return REGISTER.register(id, () -> new MenuType<>(factory, FeatureFlags.DEFAULT_FLAGS));
    }
}
