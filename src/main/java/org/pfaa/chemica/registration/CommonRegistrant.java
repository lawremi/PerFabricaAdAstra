package org.pfaa.chemica.registration;

import org.pfaa.core.registration.Registrant;

public class CommonRegistrant implements Registrant {

	@Override
	public void preregister() {
		HandlerRegistration.init();
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
