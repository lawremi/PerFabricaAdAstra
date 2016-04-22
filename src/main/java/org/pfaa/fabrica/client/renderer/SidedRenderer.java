package org.pfaa.fabrica.client.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.pfaa.fabrica.block.PaintableDrywallBlock;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import openmods.renderer.IBlockRenderer;
import openmods.utils.render.RenderUtils;

public class SidedRenderer implements IBlockRenderer<PaintableDrywallBlock> {

	@Override
	public void renderInventoryBlock(PaintableDrywallBlock block, int metadata, int modelID, RenderBlocks renderer) {
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
		RenderUtils.renderInventoryBlock(renderer, block, metadata);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, PaintableDrywallBlock block, int modelId, 
			RenderBlocks renderer) {

		renderer.setRenderBoundsFromBlock(block);

		boolean visible = false;
		
		for (int i = 0; i < 6; i++) {
			block.setRenderSide(i);
			visible |= renderer.renderStandardBlock(block, x, y, z); 
		}

		return visible;
	}
}
