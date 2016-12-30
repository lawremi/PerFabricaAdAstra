package org.pfaa.geologica;

import java.util.List;

import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.core.catalog.CatalogUtils;
import org.pfaa.core.catalog.ItemCatalog;
import org.pfaa.geologica.processing.IndustrialMineral.IndustrialMinerals;
import org.pfaa.geologica.processing.OreMineral.Ores;

public class GeologicaItems implements ItemCatalog {

	public static final IndustrialMaterialItem<GeoMaterial> CRUDE_LUMP = 
			new IndustrialMaterialItem<GeoMaterial>(Forms.LUMP, GeoMaterial.class, GeoMaterial::isCrudeSolid);
	public static final IndustrialMaterialItem<GeoMaterial> CRUDE_DUST = 
			new IndustrialMaterialItem<GeoMaterial>(Forms.DUST, GeoMaterial.class, GeoMaterial::isCrudeSolid);
	public static final IndustrialMaterialItem<GeoMaterial> CRUDE_DUST_TINY = 
			new IndustrialMaterialItem<GeoMaterial>(Forms.DUST_TINY, GeoMaterial.class, GeoMaterial::isCrudeSolid);
	public static final IndustrialMaterialItem<GeoMaterial> EARTHY_CLUMP = 
			new IndustrialMaterialItem<GeoMaterial>(Forms.CLUMP, GeoMaterial.class, GeoMaterial::isEarthyMaterial);
	public static final IndustrialMaterialItem<GeoMaterial> CLAY_LUMP = 
			new IndustrialMaterialItem<GeoMaterial>(Forms.LUMP, GeoMaterial.class, GeoMaterial::isOreClay);
	public static final IndustrialMaterialItem<GeoMaterial> ORE_CRUSHED = 
			new IndustrialMaterialItem<GeoMaterial>(Forms.CRUSHED, GeoMaterial.class, GeoMaterial::isOreRock);
	public static final IndustrialMaterialItem<GeoMaterial> ORE_DUST = 
			new IndustrialMaterialItem<GeoMaterial>(Forms.DUST_IMPURE, GeoMaterial.class, GeoMaterial::isOreRock);
	public static final IndustrialMaterialItem<GeoMaterial> ORE_DUST_TINY = 
			new IndustrialMaterialItem<GeoMaterial>(Forms.DUST_IMPURE_TINY, GeoMaterial.class, GeoMaterial::isOreRock);
	
	public static final IndustrialMaterialItem<Ores> ORE_MINERAL_DUST = 
			new IndustrialMaterialItem<Ores>(Forms.DUST, Ores.class);
	public static final IndustrialMaterialItem<Ores> ORE_MINERAL_DUST_TINY = 
			new IndustrialMaterialItem<Ores>(Forms.DUST_TINY, Ores.class);
	public static final IndustrialMaterialItem<IndustrialMinerals> INDUSTRIAL_MINERAL_DUST = 
			new IndustrialMaterialItem<IndustrialMinerals>(Forms.DUST, IndustrialMinerals.class);
	public static final IndustrialMaterialItem<IndustrialMinerals> INDUSTRIAL_MINERAL_DUST_TINY = 
			new IndustrialMaterialItem<IndustrialMinerals>(Forms.DUST_TINY, IndustrialMinerals.class);
	
	@SuppressWarnings("rawtypes")
	public static List<IndustrialMaterialItem> getIndustrialMaterialItems() {
		return CatalogUtils.getEntries(GeologicaItems.class, IndustrialMaterialItem.class);
	}
}
