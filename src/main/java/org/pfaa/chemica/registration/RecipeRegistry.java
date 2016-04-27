package org.pfaa.chemica.registration;

import java.util.List;

import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.util.ChanceStack;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface RecipeRegistry {
	public void registerCastingRecipe(ItemStack input, ItemStack output, ItemStack flux, int temp);
	public void registerCastingRecipe(FluidStack input, ItemStack output);
	public void registerMeltingRecipe(ItemStack input, FluidStack output, int temp);
	public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp);
	public void registerSmeltingRecipe(ItemStack input, FluidStack output, ItemStack flux, TemperatureLevel temp);
	public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries, Strength strength);
	public void registerCrushingRecipe(ItemStack input, ItemStack output, ChanceStack dust, Strength strength);
	public void registerPhysicalSeparationRecipe(ItemStack input, List<ChanceStack> outputs);
	public void registerAlloyingRecipe(ItemStack output, ItemStack base, List<ItemStack> solutes, int temp);
	public void registerAlloyingRecipe(FluidStack output, List<FluidStack> inputs);
	public void registerRoastingRecipe(List<ItemStack> inputs, ItemStack output, FluidStack gas, int temp);
	public void registerMixingRecipe(List<ItemStack> solidInputs, FluidStack fluidInput /* or List<FluidStack> fluidInputs */,
			ItemStack solidOutput, FluidStack fluidOutput, int temp);
	public void registerMixingRecipe(List<ItemStack> inputs, ItemStack output);
	
	public GenericRecipeRegistry getGenericRecipeRegistry();
}
