package org.pfaa.fabrica.integration;

import java.util.List;

import org.pfaa.chemica.integration.AbstractRecipeRegistry;
import org.pfaa.fabrica.recipe.HoodRecipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class FabricaRecipeRegistry extends AbstractRecipeRegistry {

	@Override
	public void registerRoastingRecipe(List<ItemStack> inputs, ItemStack output, FluidStack gas, int temp) {
		if (inputs.size() == 1 && gas != null) {
			HoodRecipes.addRecipe(inputs.get(0), gas);
		}
	}
	
}
