package org.pfaa.core.block;

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
	
	@Override
	public int hashCode()
	{
		return Block.getIdFromBlock(this.block) << Short.SIZE | this.meta & Short.MAX_VALUE;
    }
	
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof BlockWithMeta)) return false;
		BlockWithMeta<?> ok = (BlockWithMeta<?>)o;
		if (this.block != ok.block) return false;
		if (this.meta != ok.meta) return false;
		return true;
	}
}
