package org.pfaa.geologica.registration;

import org.pfaa.Registrant;

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

	public int getStairsCompositeBlockRendererId() {
		return 0;
	}
}
