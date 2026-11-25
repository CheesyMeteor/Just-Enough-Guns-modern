package ttv.migami.jeg.entity.client;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.entity.monster.phantom.gunner.PhantomGunner;

public class PhantomGunnerModel extends GeoModel<PhantomGunner> {

    @Override
    public ResourceLocation getModelResource(PhantomGunner animatable) {
        return Reference.id("geo/entity/phantom_gunner.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PhantomGunner animatable) {
        if (animatable.isPlayerOwned()) {
            return Reference.id("textures/entity/phantom_gunner/phantom_gunner_friendly.png");
        }
        return Reference.id("textures/entity/phantom_gunner/phantom_gunner.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PhantomGunner animatable) {
        return Reference.id("animations/entity/phantom_gunner.animation.json");
    }

    @Override
    public void setCustomAnimations(PhantomGunner animatable, long instanceId, AnimationState<PhantomGunner> animationState) {
        GeoBone root = getAnimationProcessor().getBone("root");
        if (root != null) {
            // PORT: unused EntityModelData lookup dropped (was only used by the commented-out head rotation below)
            //root.setRotX(root.getRotX() + entityData.headPitch() * Mth.DEG_TO_RAD);
            //root.setRotY(root.getRotY() + entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
