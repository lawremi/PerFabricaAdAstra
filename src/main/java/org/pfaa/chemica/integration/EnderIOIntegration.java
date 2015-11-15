package org.pfaa.chemica.integration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.RecipeRegistration;
import org.pfaa.chemica.registration.RecipeRegistry;
import org.pfaa.chemica.util.ChanceStack;

import cpw.mods.fml.common.Loader;
import crazypants.enderio.machine.crusher.CrusherRecipeManager;
import crazypants.enderio.machine.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;

public class EnderIOIntegration {
	public static void init() {
		if (Loader.isModLoaded(ModIds.ENDER_IO)) {
			RecipeRegistration.getTarget().addRegistry(new EnderIORecipeRegistry());
		}
	}
	
	public static class EnderIORecipeRegistry implements RecipeRegistry {

		@Override
		public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries,
				Strength strength) {
			List<RecipeOutput> outputs = new ArrayList<RecipeOutput>(secondaries.size() + 1);
			outputs.add(new RecipeOutput(output));
			for (ChanceStack secondary : secondaries) {
				outputs.add(new RecipeOutput(secondary.itemStack, secondary.chance));
			}
			int energy = RecipeCostUtils.grindingEnergyForStrength(strength);
			CrusherRecipeManager.getInstance().addRecipe(input, energy, outputs.toArray(new RecipeOutput[0]));
		}

		@Override
		public void registerCrushingRecipe(ItemStack input, ItemStack output, Strength strength) {
			this.registerGrindingRecipe(input, output, Collections.<ChanceStack>emptyList(), strength);
		}

		@Override
		public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp) {}

		@Override
		public void registerCastingRecipe(ItemStack input, ItemStack output, int temp) {}

		// Other machines: alloy smelter, vat (reactor)
	}

}
