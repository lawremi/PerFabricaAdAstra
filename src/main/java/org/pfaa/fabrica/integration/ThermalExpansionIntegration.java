package org.pfaa.fabrica.integration;

import org.pfaa.chemica.integration.ModIds;

import cpw.mods.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

public class ThermalExpansionIntegration {
	public static void init() {
		if (Loader.isModLoaded(ModIds.THERMAL_EXPANSION)) {
			oreDictifyItems();
		}
	}

	private static void oreDictifyItems() {
		OreDictionary.registerOre("itemSlag", org.pfaa.chemica.integration.ThermalExpansionIntegration.findSlag());
	}
}
