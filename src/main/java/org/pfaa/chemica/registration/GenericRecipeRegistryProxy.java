package org.pfaa.chemica.registration;

import java.util.List;

import org.pfaa.chemica.item.IngredientStack;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.util.ChanceStack;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GenericRecipeRegistryProxy implements GenericRecipeRegistry {

	private RecipeRegistry delegate;
	
	public GenericRecipeRegistryProxy(RecipeRegistry delegate) {
		this.delegate = delegate;
	}

	@Override
	public void registerCastingRecipe(ItemStack input, ItemStack output, IngredientStack flux, int temp) {
		for (ItemStack itemStack : flux.getItemStacks()) {
			delegate.registerCastingRecipe(input, output, itemStack, temp);
		}
	}

	@Override
	public void registerRoastingRecipe(IngredientList inputs, ItemStack output, FluidStack gas, int temp) {
		for (List<ItemStack> itemStacks : inputs.getItemStackLists()) {
			delegate.registerRoastingRecipe(itemStacks, output, gas, temp);
		}
	}

	@Override
	public void registerGrindingRecipe(IngredientStack input, ItemStack output, List<ChanceStack> secondaries,
			Strength strength) {
		for (ItemStack itemStack : input.getItemStacks()) {
			delegate.registerGrindingRecipe(itemStack, output, secondaries, strength);
		}
	}

	@Override
	public void registerMixingRecipe(IngredientList solidInputs, FluidStack fluidInput, FluidStack fluidInput2,
			ItemStack solidOutput, FluidStack liquidOutput, FluidStack gasOutput, Condition condition, ItemStack catalyst) {
		for (List<ItemStack> itemStacks : solidInputs.getItemStackLists()) {
			delegate.registerMixingRecipe(itemStacks, fluidInput, fluidInput2, 
					solidOutput, liquidOutput, gasOutput, condition, catalyst);
		}
	}

	@Override
	public void registerMixingRecipe(IngredientList inputs, ItemStack output) {
		for (List<ItemStack> itemStacks : inputs.getItemStackLists()) {
			delegate.registerMixingRecipe(itemStacks, output);
		}
	}

}
