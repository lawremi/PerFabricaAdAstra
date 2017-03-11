package org.pfaa.chemica.registration;

import java.util.List;
import java.util.Set;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Strength;
import org.pfaa.core.item.ChanceStack;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface RecipeRegistry {
	public void registerCastingRecipe(ItemStack input, ItemStack output, ItemStack flux, int temp, int energy);
	public void registerCastingRecipe(FluidStack input, ItemStack output);
	public void registerMeltingRecipe(ItemStack input, FluidStack output, int temp, int energy);
	public void registerFreezingRecipe(FluidStack input, ItemStack output, int temp, int energy);
	public void registerBoilingRecipe(FluidStack input, FluidStack output, int temp, int energy);
	public void registerCondensingRecipe(FluidStack input, FluidStack output, int temp, int energy);
	public void registerCoolingRecipe(FluidStack input, FluidStack output, int heat);
	public void registerPrecipitationRecipe(FluidStack input, ItemStack output, int energy);
	// FIXME: both smelting functions should take a FluidStack gas parameter for e.g. SO2 capture when smelting sulfides
	// FIXME: both should also take a reductant ItemStack (null if none/carbon) 
	public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, int temp);
	public void registerSmeltingRecipe(ItemStack input, FluidStack output, ItemStack flux, int temp);
	public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries, Strength strength);
	public void registerCrushingRecipe(ItemStack input, ItemStack output, ChanceStack dust, Strength strength);
	public void registerMechanicalSeparationRecipe(ItemStack input, List<ChanceStack> outputs);
	// TODO: add FluidStack vapor parameter to facilitate 2-stage implementations
	//       - note that the vapor might actually be liquid if cryogenic -- maybe call it "columnInput"?
	public void registerDistillationRecipe(FluidStack input, List<FluidStack> outputs, Condition condition);
	public void registerAlloyingRecipe(ItemStack output, ItemStack base, List<ItemStack> solutes, int temp, int energy);
	public void registerAlloyingRecipe(FluidStack output, List<FluidStack> inputs);
	// FIXME: "roasting" refers specifically to reaction with a gas (O2); maybe call it a "furnace" recipe?
	public void registerRoastingRecipe(List<ItemStack> inputs, ItemStack output, FluidStack gas, int temp);
	public void registerMixingRecipe(List<ItemStack> solidInputs, FluidStack fluidInput, FluidStack fluidInput2, 
			ItemStack solidOutput, FluidStack liquidOutput, FluidStack gasOutput, 
			Condition condition, Set<ItemStack> catalyst);
	// TODO: remove this
	public void registerMixingRecipe(List<ItemStack> inputs, ItemStack output);
	
	public GenericRecipeRegistry getGenericRecipeRegistry();
}
