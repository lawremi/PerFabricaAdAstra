package org.pfaa.geologica.registration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.processing.OreMineral;

public class RawMaterials {
	private static Map<Chemical,OreMineral> oreCompoundToMineral = new HashMap<Chemical, OreMineral>();
	private static Map<IndustrialMaterial,List<GeoMaterial>> materialToGeoMaterials = 
			new HashMap<IndustrialMaterial,List<GeoMaterial>>();
	
	public static void register(OreMineral mineral) {
		oreCompoundToMineral.put(mineral.getConcentrate(), mineral);
	}
	
	public static void register(GeoMaterial material) {
		IndustrialMaterial concentrate = material.getConcentrate();
		if (concentrate != null) {
			List<GeoMaterial> materials = materialToGeoMaterials.get(concentrate);
			if (materials == null) {
				materials = new ArrayList<GeoMaterial>();
				materialToGeoMaterials.put(concentrate, materials);
			}
			materials.add(material);
		}
	}
	
	public static OreMineral get(Chemical chemical) {
		return oreCompoundToMineral.get(chemical);
	}
	
	public static List<GeoMaterial> get(IndustrialMaterial material) {
		return materialToGeoMaterials.get(material);
	}
}
