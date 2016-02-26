package org.pfaa.geologica.registration;

import org.pfaa.core.registration.Registrant;
import org.pfaa.geologica.integration.ChemicaIntegration;
import org.pfaa.geologica.integration.FMPIntegration;

public class CommonRegistrant implements Registrant {

	@Override
	public void preregister() {
		BlockRegistration.init();
		ItemRegistration.init();
		OreRegistration.init();
		ChemicaIntegration.init();
	}

	@Override
	public void register() {
		RecipeRegistration.init();
		DropRegistration.init();
		FuelRegistration.init();
		FMPIntegration.init();
	}
	
	@Override
	public void postregister() {
		RecipeReplacement.init();
	}
	
	public int getCompositeBlockRendererId() {
		return 0;
	}

}
