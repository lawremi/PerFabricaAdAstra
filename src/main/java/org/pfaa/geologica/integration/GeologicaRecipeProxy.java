package org.pfaa.geologica.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pfaa.chemica.integration.AbstractRecipeRegistry;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.Compound;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.OreDictUtils;
import org.pfaa.chemica.registration.RecipeRegistration;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.processing.Mineral;
import org.pfaa.geologica.processing.OreMineral;
import org.pfaa.geologica.processing.OreMineral.Ores;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GeologicaRecipeProxy extends AbstractRecipeRegistry {

	private Map<Chemical,OreMineral> oreCompoundToMineral;
	private Map<OreMineral,List<GeoMaterial>> oreMineralToGeoMaterials;
	
	public GeologicaRecipeProxy() {
		this.oreCompoundToMineral = makeOreCompoundToMineral();
		this.oreMineralToGeoMaterials = makeOreMineralToGeoMaterials();
	}
	
	private Map<Chemical, OreMineral> makeOreCompoundToMineral() {
		Map<Chemical, OreMineral> map = new HashMap<Chemical, OreMineral>();
		for (OreMineral mineral : Ores.values()) {
			Chemical concentrate = mineral.getConcentrate();
			map.put(concentrate, mineral);
		}
		return map;
	}

	private static Map<OreMineral,List<GeoMaterial>> makeOreMineralToGeoMaterials() {
		Map<OreMineral,List<GeoMaterial>> map = new HashMap<OreMineral,List<GeoMaterial>>();
		for (GeoMaterial material : GeoMaterial.values()) {
			Mineral concentrate = material.getOreConcentrate();
			if (concentrate instanceof OreMineral) {
				OreMineral mineral = (OreMineral) concentrate;
				List<GeoMaterial> materials = map.get(mineral);
				if (materials == null) {
					materials = new ArrayList<GeoMaterial>();
					map.put(mineral, materials);
				}
				materials.add(material);
			}
		}
		return map;
	}

	private static abstract class Registrant {
		public abstract void register(ItemStack input);
	}
	
	private void mapRecipe(ItemStack input, Registrant registrant) {
		if (input.getItem() instanceof IndustrialMaterialItem) {
			IndustrialMaterialItem<?> item = (IndustrialMaterialItem<?>)input.getItem();
			IndustrialMaterial compound = item.getIndustrialMaterial(input);
			if (compound instanceof Compound) {
				OreMineral oreMineral = this.oreCompoundToMineral.get(compound);
				if (oreMineral != null) {
					ItemStack oreMineralStack = OreDictUtils.lookupBest(item.getForm(), oreMineral);
					if (oreMineralStack != null) {
						registrant.register(oreMineralStack);
						List<GeoMaterial> geoMaterials = this.oreMineralToGeoMaterials.get(oreMineral);
						if (geoMaterials != null) {
							for (GeoMaterial geoMaterial : geoMaterials) {
								if (item.getForm() == Forms.DUST) {
									ItemStack crushedStack = OreDictUtils.lookupBest(Forms.CRUSHED, geoMaterial);
									if (crushedStack == null) {
										crushedStack = OreDictUtils.lookupBest(Forms.CLUMP, geoMaterial);
									}
									if (crushedStack != null) {
										registrant.register(crushedStack);
									}
									ItemStack dustStack = OreDictUtils.lookupBest(Forms.DUST_IMPURE, geoMaterial);
									if (dustStack != null) {
										registrant.register(dustStack);
									}
									ItemStack blockStack = OreDictUtils.lookupBest("ore", geoMaterial);
									registrant.register(blockStack);
								} else if (item.getForm() == Forms.DUST_TINY) {
									ItemStack dustStack = OreDictUtils.lookupBest(Forms.DUST_IMPURE_TINY, geoMaterial);
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
	public void registerSmeltingRecipe(ItemStack input, final ItemStack output, final ItemStack flux, final TemperatureLevel temp) {
		mapRecipe(input, new Registrant() {
			@Override
			public void register(ItemStack input) {
				RecipeRegistration.getTarget().registerSmeltingRecipe(input, output, flux, temp);
			}
		});
	}

	
	@Override
	public void registerSmeltingRecipe(ItemStack input, final FluidStack output, final ItemStack flux, final TemperatureLevel temp) {
		mapRecipe(input, new Registrant() {
			@Override
			public void register(ItemStack input) {
				RecipeRegistration.getTarget().registerSmeltingRecipe(input, output, flux, temp);
			}
		});
	}

	@Override
	public void registerRoastingRecipe(final List<ItemStack> inputs, final ItemStack output, final int temp) {
		for (final ItemStack oldInput : inputs) {
			mapRecipe(oldInput, new Registrant() {
				@Override
				public void register(ItemStack input) {
					List<ItemStack> localInputs = new ArrayList<ItemStack>(inputs);
					localInputs.set(localInputs.indexOf(oldInput), input);
					RecipeRegistration.getTarget().registerRoastingRecipe(localInputs, output, temp);
				}
			});
		}
	}

}
