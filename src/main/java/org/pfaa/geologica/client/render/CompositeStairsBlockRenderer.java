package org.pfaa.geologica.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import org.pfaa.geologica.block.StairsBlock;

public class CompositeStairsBlockRenderer extends CompositeBlockRenderer {

	public CompositeStairsBlockRenderer(int renderId) {
		super(renderId);
	}

	@Override
	protected void renderInventoryBlockPass(Block block, int metadata, RenderBlocks renderer) {
		StairsBlock stairsBlock = (StairsBlock)block;
		stairsBlock.enableRenderAsStairs();
		renderer.renderBlockAsItem(block, metadata, 1.0F);
		stairsBlock.disableRenderAsStairs();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		return renderer.renderBlockStairs((StairsBlock)block, x, y, z);
	}
}
