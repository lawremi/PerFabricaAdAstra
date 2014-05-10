package org.pfaa.geologica.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;

import org.pfaa.block.CompositeBlockAccessors;
import org.pfaa.geologica.block.SlabBlock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SlabItem extends ItemSlab {
	public SlabItem(int id) {
		super(id, getSingleSlab(id), getDoubleSlab(id), isFullBlock(id));
	}

	private static SlabBlock getSingleSlab(int id) {
		return blockForItemId(id).getSingleSlab();
	}
	private static SlabBlock getDoubleSlab(int id) {
		return blockForItemId(id).getDoubleSlab();
	}
	private static boolean isFullBlock(int id) {
		return blockForItemId(id).isDoubleSlab();
	}
	
	private static SlabBlock blockForItemId(int id) {
		return (SlabBlock)Block.blocksList[id + 256];
	}
}
