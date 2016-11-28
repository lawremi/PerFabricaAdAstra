package org.pfaa.chemica.registration;

import java.util.List;
import java.util.Set;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.util.ChanceStack;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface RecipeRegistry {
	// FIXME: Should we use an abstract cost/energy value instead of temperature/condition?
	//        Right now, we cannot incorporate heat capacities and phase transitions.
	public void registerCastingRecipe(ItemStack input, ItemStack output, ItemStack flux, int temp);
	public void registerCastingRecipe(FluidStack input, ItemStack output);
	public void registerMeltingRecipe(ItemStack input, FluidStack output, int temp);
	public void registerCoolingRecipe(FluidStack input, ItemStack output, int heat);
	public void registerCrystallizationRecipe(FluidStack input, ItemStack output, int cost);
	// FIXME: both smelting functions should take a FluidStack gas parameter for e.g. SO2 capture when smelting sulfides
	public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp);
	public void registerSmeltingRecipe(ItemStack input, FluidStack output, ItemStack flux, TemperatureLevel temp);
	public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries, Strength strength);
	public void registerCrushingRecipe(ItemStack input, ItemStack output, ChanceStack dust, Strength strength);
	public void registerPhysicalSeparationRecipe(ItemStack input, List<ChanceStack> outputs);
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
