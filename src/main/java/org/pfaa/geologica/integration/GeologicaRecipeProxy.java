package org.pfaa.geologica.integration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.Compound;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.registration.OreDictUtils;
import org.pfaa.chemica.registration.RecipeRegistration;
import org.pfaa.chemica.registration.RecipeRegistry;
import org.pfaa.chemica.util.ChanceStack;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.processing.Mineral;
import org.pfaa.geologica.processing.OreMineral;
import org.pfaa.geologica.processing.OreMineral.Ores;

import net.minecraft.item.ItemStack;

public class GeologicaRecipeProxy implements RecipeRegistry {

	private Map<Chemical,OreMineral> oreCompoundToMineral;
	private Map<OreMineral,GeoMaterial> oreMineralToGeoMaterial;
	
	public GeologicaRecipeProxy() {
		this.oreCompoundToMineral = makeOreCompoundToMineral();
		this.oreMineralToGeoMaterial = makeOreMineralToGeoMaterial();
	}
	
	private Map<Chemical, OreMineral> makeOreCompoundToMineral() {
		Map<Chemical, OreMineral> map = new HashMap<Chemical, OreMineral>();
		for (OreMineral mineral : Ores.values()) {
			Chemical concentrate = mineral.getConcentrate();
			map.put(concentrate, mineral);
		}
		return map;
	}

	private static Map<OreMineral,GeoMaterial> makeOreMineralToGeoMaterial() {
		Map<OreMineral,GeoMaterial> map = new HashMap<OreMineral,GeoMaterial>();
		for (GeoMaterial material : GeoMaterial.values()) {
			Mineral concentrate = material.getOreConcentrate();
			if (concentrate instanceof OreMineral) {
				map.put((OreMineral) concentrate, material);
			}
		}
		return map;
	}

	@Override
	public void registerCastingRecipe(ItemStack input, ItemStack output, int temp) { }

	@Override
	public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp) {
		if (input.getItem() instanceof IndustrialMaterialItem) {
			IndustrialMaterialItem<?> item = (IndustrialMaterialItem<?>)input.getItem();
			IndustrialMaterial compound = item.getIndustrialMaterial(input);
			if (compound instanceof Compound) {
				OreMineral oreMineral = this.oreCompoundToMineral.get(compound);
				if (oreMineral != null) {
					ItemStack oreMineralStack = OreDictUtils.lookupBest(item.getForm(), oreMineral);
					if (oreMineralStack != null) {
						RecipeRegistration.getTarget().registerSmeltingRecipe(oreMineralStack, output, flux, temp);
						GeoMaterial geoMaterial = this.oreMineralToGeoMaterial.get(oreMineral);
						if (geoMaterial != null && item.getForm() == Forms.DUST) {
							ItemStack crushedStack = OreDictUtils.lookupBest(Forms.CRUSHED, geoMaterial);
							RecipeRegistration.getTarget().registerSmeltingRecipe(crushedStack, output, flux, temp);
							ItemStack blockStack = OreDictUtils.lookupBest(Forms.BLOCK, geoMaterial);
							RecipeRegistration.getTarget().registerSmeltingRecipe(blockStack, output, flux, temp);
						}
					}
				}
			}
		}
	}

	@Override
	public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries,
			Strength strength) { }

	@Override
	public void registerCrushingRecipe(ItemStack input, ItemStack output, Strength strength) { }

}
