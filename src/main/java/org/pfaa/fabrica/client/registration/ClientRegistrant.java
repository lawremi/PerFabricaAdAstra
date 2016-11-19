package org.pfaa.fabrica.client.registration;

import org.pfaa.chemica.integration.ModIds;
import org.pfaa.fabrica.Fabrica;
import org.pfaa.fabrica.FabricaBlocks;
import org.pfaa.fabrica.block.PaintableDrywallBlock;
import org.pfaa.fabrica.client.render.FluidReactorRenderer;
import org.pfaa.fabrica.client.renderer.SidedRenderer;
import org.pfaa.fabrica.registration.CommonRegistrant;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import openmods.renderer.BlockRenderingHandler;

public class ClientRegistrant extends CommonRegistrant {
	
	@Override
	public void register() {
		super.register();
		registerRenderers();
	}
	
	private ISimpleBlockRenderingHandler fluidReactorRenderer;
	
	private void registerRenderers() {
		if (Loader.isModLoaded(ModIds.OPEN_BLOCKS)) {
			Fabrica.renderIdFull = RenderingRegistry.getNextAvailableRenderId();
			final BlockRenderingHandler blockRenderingHandler = new BlockRenderingHandler(Fabrica.renderIdFull, true);
			SidedRenderer sidedRenderer = new SidedRenderer();
			blockRenderingHandler.addRenderer((PaintableDrywallBlock)FabricaBlocks.DRYWALL, sidedRenderer);
			RenderingRegistry.registerBlockHandler(blockRenderingHandler);
		}
		this.fluidReactorRenderer = new FluidReactorRenderer(RenderingRegistry.getNextAvailableRenderId());
		RenderingRegistry.registerBlockHandler(this.fluidReactorRenderer);
	}
	
	public int getFluidReactorRendererId() {
		return this.fluidReactorRenderer.getRenderId();
	}
}
