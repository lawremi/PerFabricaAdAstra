package org.pfaa.geologica.integration;

import org.pfaa.chemica.registration.RecipeRegistration;

import cpw.mods.fml.common.Loader;

public class ChemicaIntegration {
	public static void init() {
		String id = Loader.instance().activeModContainer().getModId();
		RecipeRegistration.putRegistry(id, new GeologicaRecipeProxy());
	}
}
