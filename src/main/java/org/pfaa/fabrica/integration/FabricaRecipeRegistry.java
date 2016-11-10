package org.pfaa.fabrica.integration;

import java.util.List;

import org.pfaa.chemica.integration.AbstractRecipeRegistry;
import org.pfaa.chemica.integration.RecipeCostUtils;
import org.pfaa.chemica.model.Condition;
import org.pfaa.fabrica.recipe.FluidReactorRecipes;
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

	@Override
	public void registerMixingRecipe(List<ItemStack> solidInputs, FluidStack fluidInput, FluidStack fluidInput2,
			ItemStack solidOutput, FluidStack liquidOutput, FluidStack gasOutput, Condition condition, ItemStack catalyst) {
		if (solidInputs.size() == 0 && solidOutput == null) {
			int energy = RecipeCostUtils.rfFromCondition(condition);
			FluidReactorRecipes.addRecipe(fluidInput, fluidInput2, energy, catalyst, liquidOutput, gasOutput);
		}
	}
	
}
