package org.pfaa.geologica.client;

import org.pfaa.RegistrationUtils;
import org.pfaa.geologica.CommonRegistrant;
import org.pfaa.geologica.GeologicaTextures;
import org.pfaa.geologica.client.render.entity.CustomRenderFallingSand;
import org.pfaa.geologica.entity.item.CustomEntityFallingSand;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientRegistrant extends CommonRegistrant {
	@Override
	public void register() {
		super.register();
		registerTextures();
		registerRenders();
	}

	private void registerRenders() {
		RenderingRegistry.registerEntityRenderingHandler(CustomEntityFallingSand.class, new CustomRenderFallingSand());   
	}

	private void registerTextures() {
		RegistrationUtils.registerDeclaredTextures(GeologicaTextures.class);
	}
}
