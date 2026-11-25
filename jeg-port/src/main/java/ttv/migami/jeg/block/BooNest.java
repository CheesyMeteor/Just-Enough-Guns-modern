package ttv.migami.jeg.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import ttv.migami.jeg.blockentity.BooNestBlockEntity;
import ttv.migami.jeg.init.ModItems;
import ttv.migami.jeg.init.ModTileEntities;

import javax.annotation.Nullable;

public class BooNest extends BeehiveBlock
{
    // BeehiveBlock#codec() is typed MapCodec<BeehiveBlock>, so the codec is declared with that type.
    public static final MapCodec<BeehiveBlock> CODEC = simpleCodec(BooNest::new);

    public BooNest(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<BeehiveBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, ModTileEntities.BOO_NEST.get(), BooNestBlockEntity::tick);
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BooNestBlockEntity(pos, state);
    }

    private boolean tryHarvestEctoplasm(BlockState state, Level worldIn, BlockPos pos, Player player) {
        int i = state.getValue(HONEY_LEVEL);
        if (i >= 5) {
            worldIn.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BEEHIVE_ENTER, SoundSource.NEUTRAL, 1.0F, 1.0F);
            popResource(worldIn, pos, new ItemStack(ModItems.ECTOPLASM.get(), 5));
            this.resetHoneyLevel(worldIn, state, pos);
            return true;
        }
        return false;
    }

    // PORT: Beehive interaction is split in 1.21: shears arrive here, empty hand in useWithoutItem.
    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemstack, BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (itemstack.getItem() == Items.SHEARS && tryHarvestEctoplasm(state, worldIn, pos, player)) {
            return ItemInteractionResult.SUCCESS;
        }
        return super.useItemOn(itemstack, state, worldIn, pos, player, handIn, hit);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player player, BlockHitResult hit) {
        // PORT-VERIFY: original only harvested with an empty hand or shears in the used hand; main-hand-empty is checked here.
        if (player.getMainHandItem().isEmpty() && tryHarvestEctoplasm(state, worldIn, pos, player)) {
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, worldIn, pos, player, hit);
    }
}
