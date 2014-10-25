package org.pfaa.util;

import net.minecraft.block.Block;

public class BlockWithMeta<B extends Block> {
	public final B block;
	public final int meta;
	
	public BlockWithMeta(B block, int meta) {
		super();
		this.block = block;
		this.meta = meta;
	}
}
