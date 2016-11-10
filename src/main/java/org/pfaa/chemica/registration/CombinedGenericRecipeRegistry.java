package org.pfaa.chemica.registration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pfaa.chemica.item.IngredientStack;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.util.ChanceStack;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CombinedGenericRecipeRegistry implements GenericRecipeRegistry {

	private Map<String,GenericRecipeRegistry> registries = new HashMap<String,GenericRecipeRegistry>();
	
	public void addRegistry(String key, GenericRecipeRegistry registry) {
		this.registries.put(key, registry);
	}
	
	@Override
	public void registerCastingRecipe(ItemStack input, ItemStack output, IngredientStack flux, int temp) {
		for (GenericRecipeRegistry registry : this.registries.values()) {
			registry.registerCastingRecipe(input, output, flux, temp);
		}
	}

	@Override
	public void registerRoastingRecipe(IngredientList inputs, ItemStack output, FluidStack gas, int temp) {
		for (GenericRecipeRegistry registry : this.registries.values()) {
			registry.registerRoastingRecipe(inputs, output, gas, temp);
		}
	}

	@Override
	public void registerGrindingRecipe(IngredientStack input, ItemStack output, List<ChanceStack> secondaries,
			Strength strength) {
		for (GenericRecipeRegistry registry : this.registries.values()) {
			registry.registerGrindingRecipe(input, output, secondaries, strength);
		}
	}

	@Override
	public void registerMixingRecipe(IngredientList solidInputs, FluidStack fluidInput, FluidStack fluidInput2,
			ItemStack solidOutput, FluidStack liquidOutput, FluidStack gasOutput, Condition condition, ItemStack catalyst) {
		for (GenericRecipeRegistry registry : this.registries.values()) {
			registry.registerMixingRecipe(solidInputs, fluidInput, fluidInput2, 
					solidOutput, liquidOutput, gasOutput, condition, catalyst);
		}
	}

	@Override
	public void registerMixingRecipe(IngredientList inputs, ItemStack output) {
		for (GenericRecipeRegistry registry : this.registries.values()) {
			registry.registerMixingRecipe(inputs, output);
		}
	}

}
