package org.pfaa.geologica.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

import org.pfaa.geologica.block.SlabBlock;

public class SlabItem extends ItemSlab {
	public SlabItem(Block block) {
		super(block, ((SlabBlock)block).getSingleSlab(), 
			         ((SlabBlock)block).getDoubleSlab(), 
			         ((SlabBlock)block).isDoubleSlab());
	}
}
