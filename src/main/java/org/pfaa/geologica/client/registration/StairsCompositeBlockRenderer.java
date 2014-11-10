package org.pfaa.geologica.client.registration;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import org.pfaa.geologica.block.StairsBlock;

public class StairsCompositeBlockRenderer extends CompositeBlockRenderer {

	public StairsCompositeBlockRenderer(int renderId) {
		super(renderId);
	}

	@Override
	protected boolean renderWorldBlockPass(int x, int y, int z, Block block,
			RenderBlocks renderer) {
		return renderer.renderBlockStairs((StairsBlock)block, x, y, z);
	}
}
