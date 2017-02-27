package org.pfaa.fabrica.registration;

import org.pfaa.chemica.registration.OreDictUtils;
import org.pfaa.fabrica.FabricaItems;
import org.pfaa.fabrica.model.Generics;

public class OreRegistration {

	public static void init() {
		oreDictifyMaterialItems();
		oreDictifyGenerics();
	}

	private static void oreDictifyGenerics() {
		OreDictUtils.register(Generics.class);
	}

	private static void oreDictifyMaterialItems() {
		OreDictUtils.register(FabricaItems.getIndustrialMaterialItems());
	}
}
