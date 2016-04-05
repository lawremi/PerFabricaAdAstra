package org.pfaa.chemica.registration;

import java.util.HashMap;
import java.util.Map;

import org.pfaa.chemica.item.MaterialStack;
import org.pfaa.fabrica.registration.MaterialStackList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CombinedMaterialRecipeRegistry implements MaterialRecipeRegistry {

	private Map<String,MaterialRecipeRegistry> registries = new HashMap<String,MaterialRecipeRegistry>();
	
	public void addRegistry(String key, MaterialRecipeRegistry registry) {
		this.registries.put(key, registry);
	}
	
	@Override
	public void registerCastingRecipe(ItemStack input, ItemStack output, MaterialStack flux, int temp) {
		for (MaterialRecipeRegistry registry : this.registries.values()) {
			registry.registerCastingRecipe(input, output, flux, temp);
		}
	}

	@Override
	public void registerRoastingRecipe(MaterialStackList inputs, ItemStack output, int temp) {
		for (MaterialRecipeRegistry registry : this.registries.values()) {
			registry.registerRoastingRecipe(inputs, output, temp);
		}
	}

	@Override
	public void registerAbsorptionRecipe(MaterialStackList inputs, FluidStack additive, ItemStack output, int temp) {
		for (MaterialRecipeRegistry registry : this.registries.values()) {
			registry.registerAbsorptionRecipe(inputs, additive, output, temp);
		}
	}

	@Override
	public void registerMixingRecipe(FluidStack input, MaterialStackList additives, FluidStack output, int temp) {
		for (MaterialRecipeRegistry registry : this.registries.values()) {
			registry.registerMixingRecipe(input, additives, output, temp);
		}
	}

	@Override
	public void registerMixingRecipe(MaterialStackList inputs, ItemStack output) {
		for (MaterialRecipeRegistry registry : this.registries.values()) {
			registry.registerMixingRecipe(inputs, output);
		}
	}

}
