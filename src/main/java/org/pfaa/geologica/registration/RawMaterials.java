package org.pfaa.geologica.registration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pfaa.chemica.model.Chemical;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.processing.Mineral;
import org.pfaa.geologica.processing.OreMineral;

public class RawMaterials {
	private static Map<Chemical,OreMineral> oreCompoundToMineral = new HashMap<Chemical, OreMineral>();
	private static Map<OreMineral,List<GeoMaterial>> oreMineralToGeoMaterials = new HashMap<OreMineral,List<GeoMaterial>>();
	
	public static void register(OreMineral mineral) {
		oreCompoundToMineral.put(mineral.getConcentrate(), mineral);
	}
	
	public static void register(GeoMaterial material) {
		Mineral concentrate = material.getOreConcentrate();
		if (concentrate instanceof OreMineral) {
			OreMineral mineral = (OreMineral) concentrate;
			List<GeoMaterial> materials = oreMineralToGeoMaterials.get(mineral);
			if (materials == null) {
				materials = new ArrayList<GeoMaterial>();
				oreMineralToGeoMaterials.put(mineral, materials);
			}
			materials.add(material);
		}
	}
	
	public static OreMineral get(Chemical chemical) {
		return oreCompoundToMineral.get(chemical);
	}
	
	public static List<GeoMaterial> get(OreMineral mineral) {
		return oreMineralToGeoMaterials.get(mineral);
	}
}
