package ttv.migami.jeg.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import ttv.migami.jeg.init.ModTileEntities;

public class DynamicLightBlockEntity extends BlockEntity {
    private double delay = 5.0;

    public DynamicLightBlockEntity(BlockPos position, BlockState state) {
        super(ModTileEntities.DYNAMIC_LIGHT.get(), position, state);
    }

    /** PORT: replaces BlockEntity#getPersistentData("Delay") usages in other slices. */
    public double getDelay() {
        return this.delay;
    }

    public void setDelay(double delay) {
        this.delay = delay;
        this.setChanged();
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        if (compound.contains("Delay")) {
            this.delay = compound.getDouble("Delay");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putDouble("Delay", this.delay);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DynamicLightBlockEntity blockEntity) {
        if (!level.isClientSide) {
            if (blockEntity.delay > 0) {
                blockEntity.delay -= 1;
            } else {
                level.setBlock(pos, state.getFluidState().isSource() ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithFullMetadata(registries);
    }
}
