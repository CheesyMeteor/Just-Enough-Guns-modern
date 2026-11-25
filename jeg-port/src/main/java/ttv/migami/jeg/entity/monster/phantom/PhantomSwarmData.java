package ttv.migami.jeg.entity.monster.phantom;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class PhantomSwarmData extends SavedData {
    private static final String DATA_NAME = "PhantomSwarmData";
    private boolean phantomSwarm = false;
    private int nextTick = 0;

    public static PhantomSwarmData load(CompoundTag tag, HolderLookup.Provider registries) {
        PhantomSwarmData data = new PhantomSwarmData();
        data.phantomSwarm = tag.getBoolean("PhantomSwarm");
        data.nextTick = tag.getInt("NextRaidTick");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putBoolean("PhantomSwarm", this.phantomSwarm);
        tag.putInt("NextRaidTick", this.nextTick);
        return tag;
    }

    public static PhantomSwarmData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new SavedData.Factory<>(PhantomSwarmData::new, PhantomSwarmData::load, null), DATA_NAME);
    }

    public boolean hasPhantomSwarm() {
        return this.phantomSwarm;
    }

    public void setPhantomSwarm(boolean value) {
        this.phantomSwarm = value;
        this.setDirty();
    }

    public int getNextTick() {
        return this.nextTick;
    }

    public void setNextTick(int nextTick) {
        this.nextTick = nextTick;
        this.setDirty();
    }
}