package ttv.migami.jeg.util.bee;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import ttv.migami.jeg.blockentity.BeehiveBlockEntityAbstract;

import javax.annotation.Nonnull;
import java.util.List;

public class InhabitantStorage implements IInhabitantStorage
{
    private List<BeehiveBlockEntityAbstract.Inhabitant> inhabitantList = Lists.newArrayList();

    public InhabitantStorage() {
    }

    @Nonnull
    @Override
    public List<BeehiveBlockEntityAbstract.Inhabitant> getInhabitants() {
        return this.inhabitantList;
    }

    @Override
    public int countInhabitants() {
        return this.inhabitantList.size();
    }

    @Override
    public void setInhabitants(List<BeehiveBlockEntityAbstract.Inhabitant> inhabitantList) {
        this.inhabitantList = inhabitantList;
    }

    @Override
    public void addInhabitant(BeehiveBlockEntityAbstract.Inhabitant inhabitant) {
        this.inhabitantList.add(inhabitant);
    }

    @Override
    public void clearInhabitants() {
        this.inhabitantList.clear();
    }

    @Nonnull
    @Override
    public ListTag getInhabitantListAsListNBT() {
        ListTag listNBT = new ListTag();

        for (BeehiveBlockEntityAbstract.Inhabitant inhabitant : this.getInhabitants()) {
            CompoundTag copyNbt = inhabitant.nbt.copy();

            CompoundTag tag = new CompoundTag();
            tag.put("EntityData", copyNbt);
            tag.putInt("TicksInHive", inhabitant.ticksInHive);
            if (inhabitant.flowerPos != null) {
                tag.putIntArray("FlowerPos", new int[]{inhabitant.flowerPos.getX(), inhabitant.flowerPos.getY(), inhabitant.flowerPos.getZ()}); // PORT: NbtUtils BlockPos helpers changed in 1.21; plain int array
            }
            tag.putInt("MinOccupationTicks", inhabitant.minOccupationTicks);
            tag.putString("Name", inhabitant.localizedName);
            listNBT.add(tag);
        }

        return listNBT;
    }

    @Override
    public void setInhabitantsFromListNBT(ListTag list) {
        clearInhabitants();
        for (int i = 0; i < list.size(); ++i) {
            CompoundTag tag = list.getCompound(i);
            BlockPos flowerPos = null;
            if (tag.contains("FlowerPos", 11)) {
                int[] fp = tag.getIntArray("FlowerPos");
                if (fp.length == 3) flowerPos = new BlockPos(fp[0], fp[1], fp[2]);
            }
            BeehiveBlockEntityAbstract.Inhabitant inhabitant = new BeehiveBlockEntityAbstract.Inhabitant(tag.getCompound("EntityData"), tag.getInt("TicksInHive"), tag.getInt("MinOccupationTicks"), flowerPos, tag.getString("Name"));
            this.addInhabitant(inhabitant);
        }
    }

    public CompoundTag serializeNBT() {
        ListTag listNBT = getInhabitantListAsListNBT();
        CompoundTag nbt = new CompoundTag();
        nbt.put("Inhabitants", listNBT);
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        ListTag list = nbt.getList("Inhabitants", 10);
        setInhabitantsFromListNBT(list);
    }

    @Override
    public void onContentsChanged() {

    }
}
