package org.pfaa.chemica.registration;

import java.util.List;

import org.pfaa.chemica.item.IngredientStack;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.util.ChanceStack;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface GenericRecipeRegistry {
	public void registerGrindingRecipe(IngredientStack input, ItemStack output, List<ChanceStack> secondaries, Strength strength);
	public void registerCastingRecipe(ItemStack input, ItemStack output, IngredientStack flux, int temp);
	public void registerRoastingRecipe(IngredientList inputs, ItemStack output, FluidStack gas, int temp);
	public void registerAbsorptionRecipe(IngredientList inputs, FluidStack additive, ItemStack output, int temp);
	public void registerMixingRecipe(FluidStack input, IngredientList additives, FluidStack output, int temp);
	public void registerMixingRecipe(IngredientList inputs, ItemStack output);
}
