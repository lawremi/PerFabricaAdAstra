package org.pfaa.geologica.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;
import org.pfaa.block.CompositeBlock;
import org.pfaa.block.CompositeBlockAccessors;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class CompositeBlockRenderer implements ISimpleBlockRenderingHandler {

	private int renderId;

	public CompositeBlockRenderer(int renderId) {
		this.renderId = renderId;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		CompositeBlock compositeBlock = (CompositeBlock)block; 
		compositeBlock.enableDefaultRenderer();
		renderer.renderBlockAsItem(block, metadata, 1.0F);
		compositeBlock.disableDefaultRenderer();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		boolean flag = false;
		CompositeBlockAccessors compositeBlock = (CompositeBlockAccessors)block;
		compositeBlock.disableOverlay();
		flag = this.renderWorldBlockPass(x, y, z, block, renderer);
		if (flag && compositeBlock.enableOverlay()) {
			this.renderWorldBlockPass(x, y, z, block, renderer);
			compositeBlock.disableOverlay();
		}
		return flag;
	}

	protected boolean renderWorldBlockPass(int x, int y, int z, Block block,
			RenderBlocks renderer) {
		return renderer.renderStandardBlock(block, x, y, z);
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return this.renderId;
	}

	public static void enableAlphaBlending() {
		GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	}

}
