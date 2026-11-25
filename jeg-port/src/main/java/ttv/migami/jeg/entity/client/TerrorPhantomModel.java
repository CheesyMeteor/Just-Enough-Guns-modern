package ttv.migami.jeg.entity.client;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.entity.monster.phantom.terror.TerrorPhantom;

public class TerrorPhantomModel extends GeoModel<TerrorPhantom> {

    @Override
    public ResourceLocation getModelResource(TerrorPhantom animatable) {
        return Reference.id("geo/entity/terror_phantom.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TerrorPhantom animatable) {
        return Reference.id("textures/entity/terror_phantom/terror_phantom.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TerrorPhantom animatable) {
        return Reference.id("animations/entity/terror_phantom.animation.json");
    }

    @Override
    public void setCustomAnimations(TerrorPhantom animatable, long instanceId, AnimationState<TerrorPhantom> animationState) {
        GeoBone root = getAnimationProcessor().getBone("root");
        if (root != null) {
            // PORT: unused EntityModelData lookup dropped (was only used by the commented-out head rotation below)
            //root.setRotX(root.getRotX() + entityData.headPitch() * Mth.DEG_TO_RAD);
            //root.setRotY(root.getRotY() + entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
