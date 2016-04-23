package org.pfaa.chemica.registration;

import java.util.List;

import org.pfaa.chemica.item.IngredientStack;
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
	public void registerAbsorptionRecipe(IngredientList inputs, FluidStack additive, ItemStack output, int temp) {
		for (List<ItemStack> itemStacks : inputs.getItemStackLists()) {
			delegate.registerAbsorptionRecipe(itemStacks, additive, output, temp);
		}
	}

	@Override
	public void registerMixingRecipe(FluidStack input, IngredientList additives, FluidStack output, int temp) {
		for (List<ItemStack> itemStacks : additives.getItemStackLists()) {
			delegate.registerMixingRecipe(input, itemStacks, output, temp);
		}
	}

	@Override
	public void registerMixingRecipe(IngredientList inputs, ItemStack output) {
		for (List<ItemStack> itemStacks : inputs.getItemStackLists()) {
			delegate.registerMixingRecipe(itemStacks, output);
		}
	}

	@Override
	public void registerGrindingRecipe(IngredientStack input, ItemStack output, List<ChanceStack> secondaries,
			Strength strength) {
		for (ItemStack itemStack : input.getItemStacks()) {
			delegate.registerGrindingRecipe(itemStack, output, secondaries, strength);
		}
	}

}
