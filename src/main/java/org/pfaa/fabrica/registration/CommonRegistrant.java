package org.pfaa.fabrica.registration;

import org.pfaa.core.registration.Registrant;

public class CommonRegistrant implements Registrant {

	@Override
	public void preregister() {
		BlockRegistration.init();
		ItemRegistration.init();
		OreRegistration.init();
	}

	@Override
	public void register() {
		RecipeRegistration.init();
	}
	
	@Override
	public void postregister() {
	}

}
