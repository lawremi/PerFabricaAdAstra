package org.pfaa.geologica.client.registration;

import org.pfaa.geologica.client.render.CompositeBlockRenderer;
import org.pfaa.geologica.client.render.CompositeStairsBlockRenderer;
import org.pfaa.geologica.client.render.CompositeWallBlockRenderer;
import org.pfaa.geologica.registration.CommonRegistrant;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientRegistrant extends CommonRegistrant {
	public static CompositeBlockRenderer compositeBlockRenderer;
	public static CompositeWallBlockRenderer compositeWallBlockRenderer;
	public static CompositeStairsBlockRenderer compositeStairsBlockRenderer;
	
	@Override
	public void register() {
		super.register();
		registerRenders();
	}

	private void registerRenders() {
		compositeBlockRenderer = new CompositeBlockRenderer(RenderingRegistry.getNextAvailableRenderId());
		RenderingRegistry.registerBlockHandler(compositeBlockRenderer);
		
		compositeWallBlockRenderer = new CompositeWallBlockRenderer(RenderingRegistry.getNextAvailableRenderId());
		RenderingRegistry.registerBlockHandler(compositeWallBlockRenderer);
		
		compositeStairsBlockRenderer = new CompositeStairsBlockRenderer(RenderingRegistry.getNextAvailableRenderId());
		RenderingRegistry.registerBlockHandler(compositeStairsBlockRenderer);
	}
}
