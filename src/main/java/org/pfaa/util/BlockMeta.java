package org.pfaa.util;

import net.minecraft.block.Block;

public class BlockMeta {
	private Block block;
	private int metadata;
        
	public BlockMeta(Block block, int metadata) {
		super();
		this.block = block;
		this.metadata = metadata;
	}
    
	@Override
	public int hashCode()
	{
		return Block.getIdFromBlock(this.block) << Short.SIZE | this.metadata & Short.MAX_VALUE;
    }
		
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof BlockMeta)) return false;
		BlockMeta ok = (BlockMeta)o;
		if (this.block != ok.block) return false;
		if (this.metadata != ok.metadata) return false;
		return true;
	}
	public int getMetadata() {
		return this.metadata;
	}
	public Block getBlock() {
		return this.block;
	}
}
