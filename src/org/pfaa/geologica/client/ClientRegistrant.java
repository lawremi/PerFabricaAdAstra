package org.pfaa.geologica.client;

import org.pfaa.RegistrationUtils;
import org.pfaa.geologica.CommonRegistrant;
import org.pfaa.geologica.GeologicaTextures;

public class ClientRegistrant extends CommonRegistrant {
	@Override
	public void register() {
		super.register();
		registerTextures();
	}

	private void registerTextures() {
		RegistrationUtils.registerDeclaredTextures(GeologicaTextures.class);
	}
}
