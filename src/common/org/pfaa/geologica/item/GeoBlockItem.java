package org.pfaa.geologica.item;

import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

import org.pfaa.geologica.GeoSubstance;
import org.pfaa.geologica.block.GeoBlock;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class GeoBlockItem extends ItemBlock {

	public GeoBlockItem(int id) {
		super(id);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getIconFromDamage(int damage) {
		GeoBlock block = (GeoBlock)Block.blocksList[this.getBlockID()];
		return block.getBlockTextureFromSideAndMetadata(2, getMetadata(damage));
	}

	// FIXME: turns out meta and damage need to be the same (see RenderBlocks.renderBlockAsItem)
	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public String getItemNameIS(ItemStack itemStack) {
		return super.getItemName() + "." + 
				getSubstanceForDamage(itemStack.getItemDamage()).getName();
	}

	private GeoSubstance getSubstanceForDamage(int damage) {
		GeoBlock block = (GeoBlock)Block.blocksList[this.getBlockID()];
		return block.getSubstance(damage);
	}

}
