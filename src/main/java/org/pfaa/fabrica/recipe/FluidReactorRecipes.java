package org.pfaa.fabrica.recipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pfaa.fabrica.util.UnorderedPair;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidReactorRecipes {
	private static final Map<UnorderedPair<Fluid>, FluidReactorRecipe> recipes = 
			new HashMap<UnorderedPair<Fluid>, FluidReactorRecipe>();
	
	public static void addRecipe(FluidStack inputA, FluidStack inputB, int energy, ItemStack catalyst, 
			FluidStack liquidOutput, FluidStack gasOutput) {
		FluidReactorRecipe recipe = new FluidReactorRecipe(inputA, inputB, energy, catalyst, liquidOutput, gasOutput);
		UnorderedPair<Fluid> key = UnorderedPair.of(inputA.getFluid(), inputB.getFluid());
		recipes.put(key, recipe);
	}
	
	private static FluidReactorRecipe getRecipe(UnorderedPair<Fluid> inputs) {
		return recipes.get(inputs);
	}
	
	private static FluidReactorRecipe getRecipe(Fluid inputA, Fluid inputB) {
		return getRecipe(UnorderedPair.of(inputA, inputB));
	}
	
	public static FluidReactorRecipe getRecipeForResources(FluidStack inputA, FluidStack inputB, List<ItemStack> catalysts, int energy) {
		FluidReactorRecipe recipe = getRecipe(inputA.getFluid(), inputB.getFluid());
		if (recipe == null) {
			return null;
		}
		if (energy < recipe.energy) {
			return null;
		}
		if (recipe.catalyst != null) {
			boolean catalyzed = false;
			for (ItemStack catalyst : catalysts) {
				if (recipe.catalyst.isItemEqual(catalyst)) {
					catalyzed = true;
					break;
				}
			}
			if (!catalyzed) {
				return null;
			}
		}
		if (inputB.containsFluid(recipe.inputA)) {
			recipe = recipe.reverseInputs();
		}
		if (inputA.amount < recipe.inputA.amount || inputB.amount < recipe.inputB.amount) {
			return null;
		}
		
		return recipe;
	}
}
