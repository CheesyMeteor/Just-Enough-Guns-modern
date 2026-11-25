package ttv.migami.jeg;

import net.minecraft.resources.ResourceLocation;

public class Reference
{
	public static final String MOD_ID = "jeg";

	/** Shorthand for {@code ResourceLocation.fromNamespaceAndPath(MOD_ID, path)} (the ResourceLocation constructors are private in 1.21). */
	public static ResourceLocation id(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
