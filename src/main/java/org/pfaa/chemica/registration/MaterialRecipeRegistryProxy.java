package org.pfaa.chemica.registration;

import java.util.List;

import org.pfaa.chemica.item.MaterialStack;
import org.pfaa.fabrica.registration.MaterialStackList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class MaterialRecipeRegistryProxy implements MaterialRecipeRegistry {

	private RecipeRegistry delegate;
	
	public MaterialRecipeRegistryProxy(RecipeRegistry delegate) {
		this.delegate = delegate;
	}

	@Override
	public void registerCastingRecipe(ItemStack input, ItemStack output, MaterialStack flux, int temp) {
		for (ItemStack itemStack : flux.getItemStacks()) {
			delegate.registerCastingRecipe(input, output, itemStack, temp);
		}
	}

	@Override
	public void registerRoastingRecipe(MaterialStackList inputs, ItemStack output, int temp) {
		for (List<ItemStack> itemStacks : inputs.getItemStackLists()) {
			delegate.registerRoastingRecipe(itemStacks, output, temp);
		}
	}

	@Override
	public void registerAbsorptionRecipe(MaterialStackList inputs, FluidStack additive, ItemStack output, int temp) {
		for (List<ItemStack> itemStacks : inputs.getItemStackLists()) {
			delegate.registerAbsorptionRecipe(itemStacks, additive, output, temp);
		}
	}

	@Override
	public void registerMixingRecipe(FluidStack input, MaterialStackList additives, FluidStack output, int temp) {
		for (List<ItemStack> itemStacks : additives.getItemStackLists()) {
			delegate.registerMixingRecipe(input, itemStacks, output, temp);
		}
	}

	@Override
	public void registerMixingRecipe(MaterialStackList inputs, ItemStack output) {
		// TODO Auto-generated method stub

	}

}
