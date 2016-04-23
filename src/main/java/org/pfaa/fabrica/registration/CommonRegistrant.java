package org.pfaa.fabrica.registration;

import org.pfaa.core.registration.Registrant;
import org.pfaa.fabrica.integration.ChemicaIntegration;

public class CommonRegistrant implements Registrant {

	@Override
	public void preregister() {
		BlockRegistration.init();
		ItemRegistration.init();
		OreRegistration.init();
		EntityRegistration.init();
		ChemicaIntegration.init();
	}

	@Override
	public void register() {
		OreMapping.init();
		RecipeRegistration.init();
	}
	
	@Override
	public void postregister() {
	}

}
