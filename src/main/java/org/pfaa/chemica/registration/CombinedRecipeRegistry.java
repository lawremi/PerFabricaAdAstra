package org.pfaa.chemica.registration;

import java.util.ArrayList;
import java.util.List;

import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.util.ChanceStack;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CombinedRecipeRegistry implements RecipeRegistry {

	private List<RecipeRegistry> registries = new ArrayList<RecipeRegistry>();
	
	public void addRegistry(RecipeRegistry registry) {
		registries.add(registry);
	}

	@Override
	public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries,
			Strength strength) {
		for (RecipeRegistry registry : registries) {
			registry.registerGrindingRecipe(input, output, secondaries, strength);
		}
	}

	@Override
	public void registerCrushingRecipe(ItemStack input, ItemStack output, ChanceStack dust, Strength strength) {
		for (RecipeRegistry registry : registries) {
			registry.registerCrushingRecipe(input, output, dust, strength);
		}
	}

	@Override
	public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp) {
		for (RecipeRegistry registry : registries) {
			registry.registerSmeltingRecipe(input, output, flux, temp);
		}
	}

	@Override
	public void registerCastingRecipe(ItemStack input, ItemStack output, ItemStack flux, int temp) {
		for (RecipeRegistry registry : registries) {
			registry.registerCastingRecipe(input, output, flux, temp);
		}
	}

	@Override
	public void registerCastingRecipe(FluidStack input, ItemStack output) {
		for (RecipeRegistry registry : registries) {
			registry.registerCastingRecipe(input, output);
		}
	}

	@Override
	public void registerMeltingRecipe(ItemStack input, FluidStack output, int temp) {
		for (RecipeRegistry registry : registries) {
			registry.registerMeltingRecipe(input, output, temp);
		}
	}

	@Override
	public void registerSmeltingRecipe(ItemStack input, FluidStack output, ItemStack flux, TemperatureLevel temp) {
		for (RecipeRegistry registry : registries) {
			registry.registerSmeltingRecipe(input, output, flux, temp);
		}
	}

	@Override
	public void registerAlloyingRecipe(ItemStack output, ItemStack base, List<ItemStack> solutes, int temp) {
		for (RecipeRegistry registry : registries) {
			registry.registerAlloyingRecipe(output, base, solutes, temp);
		}
	}

	@Override
	public void registerAlloyingRecipe(FluidStack output, List<FluidStack> inputs) {
		for (RecipeRegistry registry : registries) {
			registry.registerAlloyingRecipe(output, inputs);
		}
	}

	@Override
	public void registerRoastingRecipe(List<ItemStack> inputs, ItemStack output, int temp) {
		for (RecipeRegistry registry : registries) {
			registry.registerRoastingRecipe(inputs, output, temp);
		}
	}

	@Override
	public void registerAbsorptionRecipe(List<ItemStack> inputs, FluidStack additive, ItemStack output, int temp) {
		for (RecipeRegistry registry : registries) {
			registry.registerAbsorptionRecipe(inputs, additive, output, temp);
		}
	}

	@Override
	public void registerMixingRecipe(FluidStack input, List<ItemStack> additives, FluidStack output, int temp) {
		for (RecipeRegistry registry : registries) {
			registry.registerMixingRecipe(input, additives, output, temp);
		}
	}

	@Override
	public void registerPhysicalSeparationRecipe(ItemStack input, List<ChanceStack> outputs) {
		for (RecipeRegistry registry : registries) {
			registry.registerPhysicalSeparationRecipe(input, outputs);
		}
	}

	@Override
	public void registerMixingRecipe(List<ItemStack> inputs, ItemStack output) {
		for (RecipeRegistry registry : registries) {
			registry.registerMixingRecipe(inputs, output);
		}
	}

}
