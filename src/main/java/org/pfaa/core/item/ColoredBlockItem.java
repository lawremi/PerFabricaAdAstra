package org.pfaa.core.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ColoredItemBlock extends ItemBlock {

	public ColoredItemBlock(Block block) {
		super(block);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack itemStack, int par2) {
		return this.field_150939_a.colorMultiplier(null, 0, 0, 0);
	}
}
