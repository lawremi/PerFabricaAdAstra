package org.pfaa.fabrica.integration;

import org.pfaa.chemica.registration.BaseRecipeRegistration;

import cpw.mods.fml.common.Loader;

public class ChemicaIntegration {

	public static void init() {
		String id = Loader.instance().activeModContainer().getModId();
		BaseRecipeRegistration.putRegistry(id, new FabricaRecipeRegistry());
	}

}
