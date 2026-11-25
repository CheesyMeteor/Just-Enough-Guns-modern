package ttv.migami.jeg.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import ttv.migami.jeg.blockentity.PoweredMagazineLoaderBlockEntity;
import ttv.migami.jeg.init.ModTileEntities;

/** Powered Magazine Loader: the upgrade of the Magazine Loader. LIT while it is burning fuel. */
public class PoweredMagazineLoaderBlock extends BaseEntityBlock
{
    public static final MapCodec<PoweredMagazineLoaderBlock> CODEC = simpleCodec(PoweredMagazineLoaderBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public PoweredMagazineLoaderBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.FALSE).setValue(ACTIVE, Boolean.FALSE));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec()
    {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, LIT, ACTIVE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation)
    {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, net.minecraft.util.RandomSource random)
    {
        if(state.getValue(ACTIVE))
        {
            ttv.migami.jeg.client.sound.LoaderHumSound.ensurePlaying(pos);
        }
    }

    @Override
    protected RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new PoweredMagazineLoaderBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        return level.isClientSide ? null : createTickerHelper(type, ModTileEntities.POWERED_MAGAZINE_LOADER.get(), PoweredMagazineLoaderBlockEntity::serverTick);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if(level.isClientSide)
        {
            return InteractionResult.SUCCESS;
        }
        if(level.getBlockEntity(pos) instanceof PoweredMagazineLoaderBlockEntity loader)
        {
            player.openMenu(loader);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston)
    {
        if(!state.is(newState.getBlock()))
        {
            if(level.getBlockEntity(pos) instanceof PoweredMagazineLoaderBlockEntity loader)
            {
                Containers.dropContents(level, pos, loader);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }
}
