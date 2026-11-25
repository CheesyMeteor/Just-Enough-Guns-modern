package ttv.migami.jeg.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import ttv.migami.jeg.entity.monster.phantom.PhantomSwarmSpawner;
import ttv.migami.jeg.faction.patrol.GunnerPatrolSpawner;
import ttv.migami.jeg.faction.raid.GunnerRaidSpawner;

public class ServerTickHandler {
    private static final GunnerPatrolSpawner spawner = new GunnerPatrolSpawner();
    private static final GunnerRaidSpawner raid = new GunnerRaidSpawner();
    private static final PhantomSwarmSpawner swarm = new PhantomSwarmSpawner();

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Pre event) {
        spawner.tick(event.getServer().overworld(), true, false);
        raid.tick(event.getServer().overworld(), true, false);
        swarm.tick(event.getServer().overworld(), true, false);
    }
}