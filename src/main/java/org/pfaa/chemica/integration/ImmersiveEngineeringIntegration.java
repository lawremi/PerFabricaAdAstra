package org.pfaa.chemica.integration;

import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.RecipeRegistration;
import org.pfaa.chemica.registration.RecipeRegistry;
import org.pfaa.chemica.util.ChanceStack;

import blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import cpw.mods.fml.common.Loader;
import net.minecraft.item.ItemStack;

public class ImmersiveEngineeringIntegration {
	public static void init() {
		if (Loader.isModLoaded(ModIds.IMMERSIVE_ENGINEERING)) {
			RecipeRegistration.getTarget().addRegistry(new ImmersiveEngineeringRecipeRegistry());
		}
	}
	
	public static class ImmersiveEngineeringRecipeRegistry implements RecipeRegistry {

		@Override
		public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries,
				Strength strength) {
			int energy = RecipeCostUtils.grindingEnergyForStrength(strength);
			CrusherRecipe recipe = CrusherRecipe.addRecipe(output, input, energy);
			for (ChanceStack secondary : secondaries) {
				recipe.addToSecondaryOutput(secondary.itemStack, secondary.chance);	
			}
		}

		@Override
		public void registerCrushingRecipe(ItemStack input, ItemStack output, Strength strength) {
			this.registerGrindingRecipe(input, output, Collections.<ChanceStack>emptyList(), strength);
		}

		@Override
		public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp) {
			BlastFurnaceRecipe.addRecipe(output, input, RecipeCostUtils.blastTicksForTemperatureLevel(temp));
		}

		@Override
		public void registerCastingRecipe(ItemStack input, ItemStack output, int temp) {}

		// Other machines: coke oven, arc furnace (alloying)
	}

}
