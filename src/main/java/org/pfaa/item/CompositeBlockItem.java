package org.pfaa.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;

import org.pfaa.block.CompositeBlockAccessors;

public class CompositeBlockItem extends ItemBlock {

	public CompositeBlockItem(Block block) {
		super(block);
		this.setHasSubtypes(true);
	}

	@Override
	public IIcon getIconFromDamage(int damage) {
		return this.field_150939_a.getIcon(0, getMetadata(damage));
	}

	// FIXME: turns out meta and damage need to be the same (see RenderBlocks.renderBlockAsItem)
	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		CompositeBlockAccessors block = (CompositeBlockAccessors)this.field_150939_a;
		String suffix = itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE ? "*" : block.getBlockNameSuffix(itemStack.getItemDamage());
		return super.getUnlocalizedName() + "." + suffix;
	}
}
