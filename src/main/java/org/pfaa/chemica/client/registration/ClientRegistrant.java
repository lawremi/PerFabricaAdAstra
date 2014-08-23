package org.pfaa.chemica.client.registration;

import org.pfaa.geologica.registration.CommonRegistrant;

public class ClientRegistrant extends CommonRegistrant {

	@Override
	public void register() {
		super.register();
		ClientHandlerRegistration.init();
	}

	private void registerRenders() {
		   
	}
}
