package org.pfaa.geologica.integration;

import org.pfaa.chemica.registration.RecipeRegistration;

public class ChemicaIntegration {
	public static void init() {
		RecipeRegistration.getTarget().addRegistry(new GeologicaRecipeProxy());
	}
}
