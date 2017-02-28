package org.pfaa.geologica.integration;

import java.util.ArrayList;
import java.util.List;

import org.pfaa.chemica.integration.AbstractRecipeRegistry;
import org.pfaa.chemica.item.IndustrialItems;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.model.Compound;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.registration.BaseRecipeRegistration;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.processing.OreMineral;
import org.pfaa.geologica.registration.RawMaterials;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GeologicaRecipeProxy extends AbstractRecipeRegistry {

	private static abstract class Registrant {
		public abstract void register(ItemStack input);
	}
	
	private void mapRecipe(ItemStack input, Registrant registrant, boolean includeImpure) {
		if (input.getItem() instanceof IndustrialMaterialItem) {
			IndustrialMaterialItem<?> item = (IndustrialMaterialItem<?>)input.getItem();
			IndustrialMaterial compound = item.getIndustrialMaterial(input);
			if (compound instanceof Compound) {
				OreMineral oreMineral = RawMaterials.get((Compound)compound);
				if (oreMineral != null && (includeImpure || oreMineral.getComponents().size() == 1)) {
					ItemStack oreMineralStack = IndustrialItems.getBestItemStack(item.getForm(), oreMineral);
					if (oreMineralStack != null) {
						registrant.register(oreMineralStack);
						List<GeoMaterial> geoMaterials = null;
						if (includeImpure)
							geoMaterials = RawMaterials.get(oreMineral);
						if (geoMaterials != null) {
							for (GeoMaterial geoMaterial : geoMaterials) {
								if (item.getForm() == Forms.DUST) {
									ItemStack crushedStack = IndustrialItems.getBestItemStack(Forms.CRUSHED, geoMaterial);
									if (crushedStack == null) {
										crushedStack = IndustrialItems.getBestItemStack(Forms.CLUMP, geoMaterial);
									}
									if (crushedStack != null) {
										registrant.register(crushedStack);
									}
									ItemStack dustStack = IndustrialItems.getBestItemStack(Forms.DUST_IMPURE, geoMaterial);
									if (dustStack != null) {
										registrant.register(dustStack);
									}
									ItemStack blockStack = IndustrialItems.getBestItemStack(Forms.ORE, geoMaterial);
									registrant.register(blockStack);
								} else if (item.getForm() == Forms.DUST_TINY) {
									ItemStack dustStack = IndustrialItems.getBestItemStack(Forms.DUST_IMPURE_TINY, geoMaterial);
									if (dustStack != null) {
										registrant.register(dustStack);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void registerSmeltingRecipe(ItemStack input, final ItemStack output, final ItemStack flux, final int temp) {
		mapRecipe(input, new Registrant() {
			@Override
			public void register(ItemStack input) {
				BaseRecipeRegistration.getRecipeRegistry().registerSmeltingRecipe(input, output, flux, temp);
			}
		}, true);
	}
	
	@Override
	public void registerSmeltingRecipe(ItemStack input, final FluidStack output, final ItemStack flux, final int temp) {
		mapRecipe(input, new Registrant() {
			@Override
			public void register(ItemStack input) {
				BaseRecipeRegistration.getRecipeRegistry().registerSmeltingRecipe(input, output, flux, temp);
			}
		}, true);
	}

	@Override
	public void registerRoastingRecipe(final List<ItemStack> inputs, final ItemStack output, final FluidStack gas, final int temp) {
		for (final ItemStack oldInput : inputs) {
			mapRecipe(oldInput, new Registrant() {
				@Override
				public void register(ItemStack input) {
					List<ItemStack> localInputs = new ArrayList<ItemStack>(inputs);
					localInputs.set(localInputs.indexOf(oldInput), input);
					BaseRecipeRegistration.getRecipeRegistry().registerRoastingRecipe(localInputs, output, gas, temp);
				}
			}, true);
		}
	}

	@Override
	public void registerMixingRecipe(List<ItemStack> inputs, ItemStack output) {
		for (final ItemStack oldInput : inputs) {
			mapRecipe(oldInput, new Registrant() {
				@Override
				public void register(ItemStack input) {
					List<ItemStack> localInputs = new ArrayList<ItemStack>(inputs);
					localInputs.set(localInputs.indexOf(oldInput), input);
					BaseRecipeRegistration.getRecipeRegistry().registerMixingRecipe(localInputs, output);
				}
			}, false);
		}
	}
}
