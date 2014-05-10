package org.pfaa.geologica.client.registration;

import org.pfaa.geologica.registration.CommonRegistrant;

public class ClientRegistrant extends CommonRegistrant {
	@Override
	public void register() {
		super.register();
		registerRenders();
	}

	private void registerRenders() {
		   
	}
}
