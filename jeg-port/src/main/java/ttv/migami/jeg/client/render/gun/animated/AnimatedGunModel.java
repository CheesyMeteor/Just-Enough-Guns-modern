package ttv.migami.jeg.client.render.gun.animated;

import ttv.migami.jeg.util.ItemNames;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.init.ModItems;
import ttv.migami.jeg.item.AnimatedGunItem;
import ttv.migami.jeg.util.StackData;

public class AnimatedGunModel extends DefaultedItemGeoModel<AnimatedGunItem> {
    private ResourceLocation currentTexture;
    private ResourceLocation currentModel;
    private ResourceLocation currentAnimation;

    public AnimatedGunModel(ResourceLocation path) {
        super(path);
    }

    public ResourceLocation getModelResource(AnimatedGunItem gunItem) {
        return currentModel != null ? currentModel : ResourceLocation.fromNamespaceAndPath(gunItem.getModID(), "geo/item/gun/" + ItemNames.path(gunItem) + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AnimatedGunItem gunItem) {
        return currentTexture != null ? currentTexture : ResourceLocation.fromNamespaceAndPath(gunItem.getModID(), "textures/animated/gun/" + ItemNames.path(gunItem) + ".png");
    }

    /*@Override
    public ResourceLocation getTextureResource(AnimatedGunItem gunItem) {
        return Reference.id("textures/animated/gun/" + ItemNames.path(gunItem) + ".png");
    }*/

    /*@Override
    public ResourceLocation getModelResource(AnimatedGunItem gunItem) {
        return Reference.id("geo/item/gun/" + ItemNames.path(gunItem) + ".geo.json");
    }*/

    @Override
    public ResourceLocation getAnimationResource(AnimatedGunItem gunItem) {
        Player player = Minecraft.getInstance().player;
        ItemStack gunStack = player != null ? player.getMainHandItem() : ItemStack.EMPTY;

        if (gunStack.is(ModItems.ABSTRACT_GUN.get())) {
            if (StackData.contains(gunStack, "GunId")) {
                ResourceLocation id = ResourceLocation.parse(StackData.getString(gunStack, "GunId"));
                ResourceLocation anim = Reference.id("animations/" + id.getPath() + ".animation.json");

                if (Minecraft.getInstance().getResourceManager().getResource(anim).isPresent()) {
                    return anim;
                } else {
                    return Reference.id("animations/item/" + "abstract_gun" + ".animation.json");
                }
            }
        }

        return currentAnimation != null ? currentAnimation : ResourceLocation.fromNamespaceAndPath(gunItem.getModID(), "animations/item/" + ItemNames.path(gunItem) + ".animation.json");
    }

    public void setCurrentTexture(ResourceLocation texture) {
        this.currentTexture = texture;
    }

    public void setCurrentModel(ResourceLocation model) {
        this.currentModel = model;
    }

    public void setCurrentAnimation(ResourceLocation animation) {
        this.currentAnimation = animation;
    }
}