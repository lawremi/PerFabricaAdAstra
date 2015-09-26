package org.pfaa.chemica.registration;

import org.pfaa.core.registration.Registrant;

public class CommonRegistrant implements Registrant {

	@Override
	public void preregister() {
		HandlerRegistration.init();
		BlockRegistration.init();
		ItemRegistration.init();
	}

	@Override
	public void register() {
		OreRegistration.init();
		RecipeRegistration.init();
	}
	
	@Override
	public void postregister() {
	}

}
