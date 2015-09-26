package org.pfaa.geologica;

import java.util.List;

import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.core.catalog.CatalogUtils;
import org.pfaa.core.catalog.ItemCatalog;
import org.pfaa.geologica.processing.Crude;
import org.pfaa.geologica.processing.IndustrialMineral.IndustrialMinerals;
import org.pfaa.geologica.processing.Ore;
import org.pfaa.geologica.processing.OreMineral.Ores;

import com.google.common.base.Predicate;

import net.minecraft.block.material.Material;

public class GeologicaItems implements ItemCatalog {
	private static Predicate<GeoMaterial> EarthyMaterial = new Predicate<GeoMaterial>() {
		public boolean apply(GeoMaterial obj) {
			return obj.getBlockMaterial() == Material.clay || obj.getBlockMaterial() == Material.ground;
		}
	};
	private static Predicate<GeoMaterial> CrudeRock = new Predicate<GeoMaterial>() {
		public boolean apply(GeoMaterial obj) {
			return obj.getComposition() instanceof Crude && obj.getBlockMaterial() == Material.rock;
		}
	};
	private static Predicate<GeoMaterial> OreRock = new Predicate<GeoMaterial>() {
		public boolean apply(GeoMaterial obj) {
			return obj.getComposition() instanceof Ore && obj.getBlockMaterial() == Material.rock;
		}
	};

	public static final IndustrialMaterialItem<GeoMaterial> CRUDE_LUMP = 
			new IndustrialMaterialItem<GeoMaterial>(Forms.LUMP, GeoMaterial.class, CrudeRock);
	public static final IndustrialMaterialItem<GeoMaterial> EARTHY_CLUMP = 
			new IndustrialMaterialItem<GeoMaterial>(Forms.CLUMP, GeoMaterial.class, EarthyMaterial);
	public static final IndustrialMaterialItem<GeoMaterial> ORE_CRUSHED = 
			new IndustrialMaterialItem<GeoMaterial>(Forms.CRUSHED, GeoMaterial.class, OreRock);
	public static final IndustrialMaterialItem<GeoMaterial> ORE_DUST = 
			new IndustrialMaterialItem<GeoMaterial>(Forms.DUST, GeoMaterial.class, OreRock);
	
	public static final IndustrialMaterialItem<Ores> ORE_MINERAL_DUST = 
			new IndustrialMaterialItem<Ores>(Forms.DUST, Ores.class);
	public static final IndustrialMaterialItem<IndustrialMinerals> INDUSTRIAL_MINERAL_DUST = 
			new IndustrialMaterialItem<IndustrialMinerals>(Forms.DUST, IndustrialMinerals.class);
	
	@SuppressWarnings("rawtypes")
	public static List<IndustrialMaterialItem> getIndustrialMaterialItems() {
		return CatalogUtils.getEntries(GeologicaItems.class, IndustrialMaterialItem.class);
	}
}
