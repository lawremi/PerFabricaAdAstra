package org.pfaa.chemica.integration;

import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.RecipeRegistration;
import org.pfaa.chemica.registration.RecipeRegistry;
import org.pfaa.chemica.util.ChanceStack;

import buildcraftAdditions.api.recipe.BCARecipeManager;
import cpw.mods.fml.common.Loader;
import net.minecraft.item.ItemStack;

public class BuildcraftAdditionsIntegration {
	public static void init() {
		if (Loader.isModLoaded(ModIds.BUILDCRAFT_ADDITIONS)) {
			RecipeRegistration.getTarget().addRegistry(new BuildcraftAdditionsRecipeRegistry());
		}
	}
	
	public static class BuildcraftAdditionsRecipeRegistry implements RecipeRegistry {
		@Override
		public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries,
				Strength strength) {
			BCARecipeManager.duster.addRecipe(input, output);
		}

		@Override
		public void registerCrushingRecipe(ItemStack input, ItemStack output, Strength strength) {
			this.registerGrindingRecipe(input, output, Collections.<ChanceStack>emptyList(), strength);
		}

		@Override
		public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp) {}

		@Override
		public void registerCastingRecipe(ItemStack input, ItemStack output, int temp) {}

		// Other machines: cooling tower (condenser), refinery (simple one output)
	}
}
