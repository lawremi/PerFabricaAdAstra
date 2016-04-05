package org.pfaa.chemica.integration;

import java.util.List;

import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.registration.RecipeRegistration;
import org.pfaa.chemica.util.ChanceStack;

import buildcraftAdditions.api.recipe.BCARecipeManager;
import cpw.mods.fml.common.Loader;
import net.minecraft.item.ItemStack;

public class BuildcraftAdditionsIntegration {
	public static void init() {
		if (Loader.isModLoaded(ModIds.BUILDCRAFT_ADDITIONS)) {
			RecipeRegistration.addRegistry(ModIds.BUILDCRAFT_ADDITIONS,
					new BuildcraftAdditionsRecipeRegistry());
		}
	}
	
	public static class BuildcraftAdditionsRecipeRegistry extends AbstractRecipeRegistry {
		@Override
		public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries,
				Strength strength) {
			BCARecipeManager.duster.addRecipe(input, output);
		}

		@Override
		public void registerPhysicalSeparationRecipe(ItemStack input, List<ChanceStack> outputs) {}

		// Other machines: cooling tower (condenser), refinery (essentially a boiler producing one gas output)
	}
}
