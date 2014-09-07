package org.pfaa.chemica.registration;

import org.pfaa.Registrant;

public class CommonRegistrant implements Registrant {

	@Override
	public void preregister() {
		HandlerRegistration.init();
		ItemRegistration.init();
	}

	@Override
	public void register() {
	}
	
	@Override
	public void postregister() {
	}

}
