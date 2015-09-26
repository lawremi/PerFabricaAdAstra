package org.pfaa.chemica.integration;

import java.util.List;

import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.RecipeRegistration;
import org.pfaa.chemica.registration.RecipeRegistry;
import org.pfaa.chemica.util.ChanceStack;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class VanillaIntegration {
	public static void init() {
		RecipeRegistration.getTarget().addRegistry(new VanillaRecipeRegistry());
	}
	
	public static class VanillaRecipeRegistry implements RecipeRegistry {

		@Override
		public void registerSmeltingRecipe(ItemStack input, ItemStack output, TemperatureLevel temp) {
			if (temp != TemperatureLevel.VERY_HIGH) {
				FurnaceRecipes.smelting().func_151394_a(input, output, 0);
			}
		}

		@Override
		public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries,
				Strength strength) {
		}

		@Override
		public void registerCrushingRecipe(ItemStack input, ItemStack output, Strength strength) {
		}
		
	}
}
