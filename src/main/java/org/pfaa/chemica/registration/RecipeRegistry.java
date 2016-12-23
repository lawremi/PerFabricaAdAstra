package org.pfaa.chemica.registration;

import java.util.List;
import java.util.Set;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.util.ChanceStack;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface RecipeRegistry {
	// FIXME: Standardize all methods to have required condition and energy (where necessary)
	public void registerCastingRecipe(ItemStack input, ItemStack output, ItemStack flux, int temp);
	public void registerCastingRecipe(FluidStack input, ItemStack output);
	public void registerMeltingRecipe(ItemStack input, FluidStack output, int temp);
	public void registerFreezingRecipe(FluidStack input, ItemStack output, int temp, int heat);
	public void registerCoolingRecipe(FluidStack input, FluidStack output, int heat);
	public void registerPrecipitationRecipe(FluidStack input, ItemStack output, int cost);
	// FIXME: both smelting functions should take a FluidStack gas parameter for e.g. SO2 capture when smelting sulfides
	public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, int temp);
	public void registerSmeltingRecipe(ItemStack input, FluidStack output, ItemStack flux, int temp);
	public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries, Strength strength);
	public void registerCrushingRecipe(ItemStack input, ItemStack output, ChanceStack dust, Strength strength);
	public void registerPhysicalSeparationRecipe(ItemStack input, List<ChanceStack> outputs);
	// TODO: add FluidStack vapor parameter to facilitate 2-stage implementations
	//       - note that the vapor might actually be liquid if cryogenic -- maybe call it "columnInput"?
	public void registerDistillationRecipe(FluidStack input, List<FluidStack> outputs, Condition condition);
	public void registerAlloyingRecipe(ItemStack output, ItemStack base, List<ItemStack> solutes, int temp);
	public void registerAlloyingRecipe(FluidStack output, List<FluidStack> inputs);
	public void registerRoastingRecipe(List<ItemStack> inputs, ItemStack output, FluidStack gas, int temp);
	public void registerMixingRecipe(List<ItemStack> solidInputs, FluidStack fluidInput, FluidStack fluidInput2, 
			ItemStack solidOutput, FluidStack liquidOutput, FluidStack gasOutput, 
			Condition condition, Set<ItemStack> catalyst);
	public void registerMixingRecipe(List<ItemStack> inputs, ItemStack output);
	
	public GenericRecipeRegistry getGenericRecipeRegistry();
}
