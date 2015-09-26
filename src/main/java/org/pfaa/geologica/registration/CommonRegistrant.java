package org.pfaa.geologica.registration;

import org.pfaa.core.registration.Registrant;

public class CommonRegistrant implements Registrant {

	@Override
	public void preregister() {
		BlockRegistration.init();
		ItemRegistration.init();
		EntityRegistration.init();
	}

	@Override
	public void register() {
		RecipeRegistration.init();
	}
	
	@Override
	public void postregister() {
		RecipeReplacement.init();
	}
	
	public int getCompositeBlockRendererId() {
		return 0;
	}

}
