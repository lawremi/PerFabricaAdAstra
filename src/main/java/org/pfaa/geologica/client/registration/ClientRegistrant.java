package org.pfaa.geologica.client.registration;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.pfaa.block.CompositeBlockAccessors;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.block.StairsBlock;
import org.pfaa.geologica.client.render.CompositeBlockItemRenderer;
import org.pfaa.geologica.client.render.FallingCompositeBlockRenderer;
import org.pfaa.geologica.registration.CommonRegistrant;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientRegistrant extends CommonRegistrant {
	
	private CompositeBlockRenderer compositeBlockRenderer;
	private CompositeBlockRenderer stairsCompositeBlockRenderer;

	@Override
	public void register() {
		super.register();
		registerRenderers();
	}

	private void registerRenderers() {
		IItemRenderer renderer = new CompositeBlockItemRenderer();
		for (Block block : GeologicaBlocks.getBlocks()) {
			if (block instanceof CompositeBlockAccessors) {
				MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block), renderer);
			}
		}
		
		RenderingRegistry.registerEntityRenderingHandler(EntityFallingBlock.class, new FallingCompositeBlockRenderer());
		
		this.compositeBlockRenderer = new CompositeBlockRenderer(RenderingRegistry.getNextAvailableRenderId());
		RenderingRegistry.registerBlockHandler(this.compositeBlockRenderer);
		this.stairsCompositeBlockRenderer = new StairsCompositeBlockRenderer(RenderingRegistry.getNextAvailableRenderId());
		RenderingRegistry.registerBlockHandler(this.stairsCompositeBlockRenderer);
	}
	
	@Override
	public int getCompositeBlockRendererId() {
		return this.compositeBlockRenderer.getRenderId();
	}
	
	@Override
	public int getStairsCompositeBlockRendererId() {
		return this.stairsCompositeBlockRenderer.getRenderId();
	}
}
