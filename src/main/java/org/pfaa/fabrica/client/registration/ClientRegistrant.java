package org.pfaa.fabrica.client.registration;

import org.pfaa.chemica.integration.ModIds;
import org.pfaa.fabrica.Fabrica;
import org.pfaa.fabrica.FabricaBlocks;
import org.pfaa.fabrica.registration.CommonRegistrant;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import openblocks.client.renderer.block.BlockCanvasRenderer;
import openblocks.common.block.BlockCanvas;
import openmods.renderer.BlockRenderingHandler;

public class ClientRegistrant extends CommonRegistrant {
	
	@Override
	public void register() {
		super.register();
		registerRenderers();
	}
	
	private void registerRenderers() {
		if (Loader.isModLoaded(ModIds.OPEN_BLOCKS)) {
			Fabrica.renderIdFull = RenderingRegistry.getNextAvailableRenderId();
			final BlockRenderingHandler blockRenderingHandler = new BlockRenderingHandler(Fabrica.renderIdFull, true);
			BlockCanvasRenderer canvasRenderer = new BlockCanvasRenderer();
			blockRenderingHandler.addRenderer((BlockCanvas)FabricaBlocks.DRYWALL, canvasRenderer);
		}
	}
}
