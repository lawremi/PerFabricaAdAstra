package org.pfaa.util;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class BlockWithMeta<B extends Block> {
	public final B block;
	public final int meta;
	
	public BlockWithMeta(B block, int meta) {
		super();
		this.block = block;
		this.meta = meta;
	}
	
	public ItemStack getItemStack(int quantity) {
		return new ItemStack(this.block, quantity, this.meta);
	}
}
