package ttv.migami.jeg.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.portal.DimensionTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ttv.migami.jeg.item.GunItem;
import ttv.migami.jeg.util.StackData;

/**
 * Author: MrCrayfish
 */
@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin
{
    /*
     * PORT: 1.20.1 injected before Entity#changeDimension(ServerLevel) inside entityInside. In 1.21 the portal block
     * implements Portal and the transfer target is computed by getPortalDestination(ServerLevel, Entity, BlockPos),
     * which is only reached when the entity is actually about to change dimension.
     * PORT-VERIFY: EndPortalBlock#getPortalDestination signature/return type (DimensionTransition) in 1.21.1; require = 0 so a wrong target only disables the effect instead of crashing at startup
     */
    @Inject(method = "getPortalDestination", at = @At("HEAD"), require = 0)
    private void beforeChangeDimension(ServerLevel level, Entity entityIn, BlockPos pos, CallbackInfoReturnable<DimensionTransition> cir)
    {
        if(level.dimension() == Level.END && entityIn instanceof ItemEntity)
        {
            ItemStack stack = ((ItemEntity) entityIn).getItem();
            if(stack.getItem() instanceof GunItem)
            {
                ItemStack gun = stack.copy();
                StackData.putFloat(gun, "Scale", 2.0F);
                ((ItemEntity) entityIn).setItem(gun);
            }
        }
    }
}
