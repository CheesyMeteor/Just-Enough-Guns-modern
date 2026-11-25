package ttv.migami.jeg.util;

import net.minecraft.resources.ResourceLocation;
import ttv.migami.jeg.Reference;

import java.util.Set;

/** JEG: Modern - the bullet items that magazines can hold. */
public final class MagazineAmmo
{
    public static final Set<ResourceLocation> BULLETS = Set.of(
            Reference.id("pistol_ammo"),
            Reference.id("rifle_ammo"),
            Reference.id("spectre_round"),
            Reference.id("blaze_round"));

    private MagazineAmmo() {}
}
