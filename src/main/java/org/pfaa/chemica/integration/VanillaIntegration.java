package org.pfaa.chemica.integration;

import java.util.List;

import org.pfaa.chemica.model.Constants;
import org.pfaa.chemica.model.Element;
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
		public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp) {
			if (flux == null && temp != TemperatureLevel.VERY_HIGH) {
				FurnaceRecipes.smelting().func_151394_a(input, output, 0);
			}
		}

		@Override
		public void registerCastingRecipe(ItemStack input, ItemStack output, int temp) {
			int ironFusionTemp = Element.Fe.getFusion().getTemperature();
			if (temp > Constants.STANDARD_TEMPERATURE && temp <= ironFusionTemp) {
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
