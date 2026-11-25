package ttv.migami.jeg.client.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import ttv.migami.jeg.block.PoweredMagazineLoaderBlock;
import ttv.migami.jeg.init.ModBlocks;
import ttv.migami.jeg.init.ModSounds;

import java.util.HashMap;
import java.util.Map;

/** Looping hum for an active Powered Magazine Loader. Started from the block's animateTick, ends itself when it stops. */
public class LoaderHumSound extends AbstractTickableSoundInstance
{
    private static final Map<BlockPos, LoaderHumSound> PLAYING = new HashMap<>();

    private final BlockPos pos;

    private LoaderHumSound(BlockPos pos)
    {
        super(ModSounds.POWERED_LOADER_HUM.get(), SoundSource.BLOCKS, SoundInstance.createUnseededRandom());
        this.pos = pos;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.5F;
        this.pitch = 1.0F;
        this.x = pos.getX() + 0.5;
        this.y = pos.getY() + 0.5;
        this.z = pos.getZ() + 0.5;
    }

    public static void ensurePlaying(BlockPos pos)
    {
        Minecraft minecraft = Minecraft.getInstance();
        LoaderHumSound existing = PLAYING.get(pos);
        if(existing != null && !existing.isStopped() && minecraft.getSoundManager().isActive(existing))
        {
            return;
        }
        BlockPos key = pos.immutable();
        LoaderHumSound sound = new LoaderHumSound(key);
        PLAYING.put(key, sound);
        minecraft.getSoundManager().play(sound);
    }

    @Override
    public void tick()
    {
        Level level = Minecraft.getInstance().level;
        if(level == null)
        {
            this.stop();
            PLAYING.remove(this.pos);
            return;
        }
        BlockState state = level.getBlockState(this.pos);
        if(!state.is(ModBlocks.POWERED_MAGAZINE_LOADER.get()) || !state.getValue(PoweredMagazineLoaderBlock.ACTIVE))
        {
            this.stop();
            PLAYING.remove(this.pos);
        }
    }
}
