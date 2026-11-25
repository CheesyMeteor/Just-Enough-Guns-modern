package ttv.migami.jeg.event;

import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.entity.Bubble;
import ttv.migami.jeg.entity.animal.Boo;
import ttv.migami.jeg.entity.monster.Ghoul;
import ttv.migami.jeg.entity.monster.phantom.gunner.PhantomGunner;
import ttv.migami.jeg.entity.monster.phantom.terror.TerrorPhantom;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import ttv.migami.jeg.init.ModEntities;
import ttv.migami.jeg.init.ModTileEntities;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModCommonEventBus {

    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.GHOUL.get(), Ghoul.createAttributes().build());
        event.put(ModEntities.BOO.get(), Bee.createAttributes().build());
        event.put(ModEntities.TERROR_PHANMTOM.get(), TerrorPhantom.createAttributes().build());
        event.put(ModEntities.PHANTOM_GUNNER.get(), PhantomGunner.createAttributes().build());
        event.put(ModEntities.BUBBLE.get(), Bubble.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(
                ModEntities.GHOUL.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.WORLD_SURFACE,
                Ghoul::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.BOO.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.WORLD_SURFACE,
                Boo::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
    }

    // PORT: replaces the Forge getCapability(ITEM_HANDLER)/createUnSidedHandler of the Recycler and Ammo Box block entities
    // (both implement WorldlyContainer), so pipes / non-vanilla automation keep working.
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.RECYCLER.get(), (be, side) -> new SidedInvWrapper(be, side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.AMMO_BOX.get(), (be, side) -> new SidedInvWrapper(be, side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.MAGAZINE_LOADER.get(), (be, side) -> new SidedInvWrapper(be, side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.POWERED_MAGAZINE_LOADER.get(), (be, side) -> new SidedInvWrapper(be, side));
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModTileEntities.POWERED_MAGAZINE_LOADER.get(), (be, side) -> be.getEnergyStorage());
    }
}
