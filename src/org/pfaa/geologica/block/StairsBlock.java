package org.pfaa.geologica.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

public class StairsBlock extends BlockStairs {

	private final Block modelBlock;
	private final int modelBlockMeta;
	
	public StairsBlock(int id, Block block, int meta) {
		super(id, block, meta);
		this.modelBlock = block;
		this.modelBlockMeta = meta;
	}

	@Override
	public String getTextureFile() {
		return modelBlock == null ? null : modelBlock.getTextureFile();
	}

	public Block getModelBlock() {
		return modelBlock;
	}

	public int getModelBlockMeta() {
		return modelBlockMeta;
	}

}
