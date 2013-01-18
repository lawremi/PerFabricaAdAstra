package org.pfaa.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import org.pfaa.block.CompositeBlockAccessors;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CompositeBlockItem extends ItemBlock {

	public CompositeBlockItem(int id) {
		super(id);
		this.setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getIconFromDamage(int damage) {
		Block block = Block.blocksList[this.getBlockID()];
		return block.getBlockTextureFromSideAndMetadata(0, getMetadata(damage));
	}

	// FIXME: turns out meta and damage need to be the same (see RenderBlocks.renderBlockAsItem)
	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public String getItemNameIS(ItemStack itemStack) {
		CompositeBlockAccessors block = (CompositeBlockAccessors)Block.blocksList[this.getBlockID()];
		return super.getItemName() + "." + block.getBlockNameSuffixForMeta(itemStack.getItemDamage());
	}
}
