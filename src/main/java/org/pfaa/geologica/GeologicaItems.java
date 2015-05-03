package org.pfaa.geologica;

import net.minecraft.block.material.Material;

import org.pfaa.ItemCatalog;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.geologica.processing.Crude;

import com.google.common.base.Predicate;

public class GeologicaItems extends ItemCatalog {
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
	
	public static final IndustrialMaterialItem<GeoMaterial> CRUDE_LUMP = 
			new IndustrialMaterialItem<GeoMaterial>(Forms.LUMP, GeoMaterial.class, CrudeRock);
	public static final IndustrialMaterialItem<GeoMaterial> EARTHY_CLUMP = 
			new IndustrialMaterialItem<GeoMaterial>(Forms.CLUMP, GeoMaterial.class, EarthyMaterial);
}
