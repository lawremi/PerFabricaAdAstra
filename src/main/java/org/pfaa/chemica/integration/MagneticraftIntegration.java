package org.pfaa.chemica.integration;

import java.util.List;

import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.RecipeRegistration;
import org.pfaa.chemica.registration.RecipeRegistry;
import org.pfaa.chemica.util.ChanceStack;

import cpw.mods.fml.common.Loader;
import net.minecraft.item.ItemStack;

public class MagneticraftIntegration {
	public static void init() {
		if (Loader.isModLoaded(ModIds.MAGNETICRAFT)) {
			RecipeRegistration.getTarget().addRegistry(new MagneticraftRecipeRegistry());
		}
	}
	
	public static class MagneticraftRecipeRegistry implements RecipeRegistry {

		@Override
		public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries,
				Strength strength) {
			// TODO: grinder
		}

		@Override
		public void registerCrushingRecipe(ItemStack input, ItemStack output, Strength strength) {
			// TODO: crusher and crushing table
		}

		@Override
		public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp) {}

		@Override
		public void registerCastingRecipe(ItemStack input, ItemStack output, int temp) {}

		// Other machines: sifter (separate solids by density), distillery (just one output), refinery (3 outputs)
	}

}
