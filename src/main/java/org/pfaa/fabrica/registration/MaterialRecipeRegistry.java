package org.pfaa.fabrica.registration;

import java.util.List;
import java.util.Set;

import org.pfaa.chemica.item.MaterialStack;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.RecipeRegistry;
import org.pfaa.chemica.util.ChanceStack;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class MaterialRecipeRegistry implements RecipeRegistry {

	private RecipeRegistry delegate;
	
	public MaterialRecipeRegistry(RecipeRegistry delegate) {
		this.delegate = delegate;
	}

	public void registerCastingRecipe(ItemStack input, ItemStack output, ItemStack flux, int temp) {
		delegate.registerCastingRecipe(input, output, flux, temp);
	}
	
	public void registerCastingRecipes(ItemStack input, ItemStack output, MaterialStack flux, int temp) {
		for (ItemStack fluxStack : flux.getItemStacks()) {
			this.registerCastingRecipe(input, output, fluxStack, temp);
		}
	}
	
	public void registerCastingRecipe(FluidStack input, ItemStack output) {
		delegate.registerCastingRecipe(input, output);
	}

	public void registerMeltingRecipe(ItemStack input, FluidStack output, int temp) {
		delegate.registerMeltingRecipe(input, output, temp);
	}

	public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp) {
		delegate.registerSmeltingRecipe(input, output, flux, temp);
	}

	public void registerSmeltingRecipe(ItemStack input, FluidStack output, ItemStack flux, TemperatureLevel temp) {
		delegate.registerSmeltingRecipe(input, output, flux, temp);
	}

	public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries,
			Strength strength) {
		delegate.registerGrindingRecipe(input, output, secondaries, strength);
	}

	public void registerCrushingRecipe(ItemStack input, ItemStack output, ChanceStack dust, Strength strength) {
		delegate.registerCrushingRecipe(input, output, dust, strength);
	}

	public void registerAlloyingRecipe(ItemStack output, ItemStack base, List<ItemStack> solutes, int temp) {
		delegate.registerAlloyingRecipe(output, base, solutes, temp);
	}

	public void registerAlloyingRecipe(FluidStack output, List<FluidStack> inputs) {
		delegate.registerAlloyingRecipe(output, inputs);
	}

	public void registerRoastingRecipe(List<ItemStack> inputs, ItemStack output, int temp) {
		delegate.registerRoastingRecipe(inputs, output, temp);
	}

	private Function<MaterialStack, Set<ItemStack>> getItemStacks = new Function<MaterialStack, Set<ItemStack>>() {
		@Override
		public Set<ItemStack> apply(MaterialStack input) {
			return input.getItemStacks();
		}
	};
	
	private Set<List<ItemStack>> getItemStackProduct(List<MaterialStack> inputs) {
		List<Set<ItemStack>> expandedInputs = Lists.transform(inputs, getItemStacks);
		Set<List<ItemStack>> product = Sets.cartesianProduct(expandedInputs);
		return product;
	}
	
	public void registerRoastingRecipes(List<MaterialStack> inputs, ItemStack output, int temp) {	
		for (List<ItemStack> input : getItemStackProduct(inputs)) {
			this.registerRoastingRecipe(input, output, temp);
		}
	}

	public void registerAbsorptionRecipe(List<ItemStack> inputs, FluidStack additive, ItemStack output, int temp) {
		delegate.registerAbsorptionRecipe(inputs, additive, output, temp);
	}

	public void registerAbsorptionRecipes(List<MaterialStack> inputs, FluidStack additive, ItemStack output, int temp) {
		for (List<ItemStack> input : getItemStackProduct(inputs)) {
			this.registerAbsorptionRecipe(input, additive, output, temp);
		}
	}
	
	public void registerMixingRecipe(FluidStack input, List<ItemStack> additives, FluidStack output, int temp) {
		delegate.registerMixingRecipe(input, additives, output, temp);
	}

	public void registerMixingRecipes(FluidStack input, List<MaterialStack> additives, FluidStack output, int temp) {
		for (List<ItemStack> additive : getItemStackProduct(additives)) {
			this.registerMixingRecipe(input, additive, output, temp);
		}
	}
	
	public void registerMixingRecipe(List<ItemStack> inputs, ItemStack output) {
		delegate.registerMixingRecipe(inputs, output);
	}

	public void registerMixingRecipes(List<MaterialStack> inputs, ItemStack output) {
		for (List<ItemStack> input : getItemStackProduct(inputs)) {
			this.registerMixingRecipe(input, output);
		}
	}
	
	public void registerPhysicalSeparationRecipe(ItemStack input, List<ChanceStack> outputs) {
		delegate.registerPhysicalSeparationRecipe(input, outputs);
	}	
}
