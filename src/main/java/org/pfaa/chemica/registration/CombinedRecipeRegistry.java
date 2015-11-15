package org.pfaa.chemica.registration;

import java.util.ArrayList;
import java.util.List;

import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.util.ChanceStack;

import net.minecraft.item.ItemStack;

public class CombinedRecipeRegistry implements RecipeRegistry {

	private List<RecipeRegistry> registries = new ArrayList<RecipeRegistry>();
	
	public void addRegistry(RecipeRegistry registry) {
		registries.add(registry);
	}

	@Override
	public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries,
			Strength strength) {
		for (RecipeRegistry registry : registries) {
			registry.registerGrindingRecipe(input, output, secondaries, strength);
		}
	}

	@Override
	public void registerCrushingRecipe(ItemStack input, ItemStack output, Strength strength) {
		for (RecipeRegistry registry : registries) {
			registry.registerCrushingRecipe(input, output, strength);
		}
	}

	@Override
	public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp) {
		for (RecipeRegistry registry : registries) {
			registry.registerSmeltingRecipe(input, output, flux, temp);
		}
	}

	@Override
	public void registerCastingRecipe(ItemStack input, ItemStack output, int temp) {
		for (RecipeRegistry registry : registries) {
			registry.registerCastingRecipe(input, output, temp);
		}
	}

}
