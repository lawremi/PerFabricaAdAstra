package org.pfaa.core.item;

import org.pfaa.core.block.CompositeBlockAccessors;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;

public class CompositeBlockItem extends ItemBlock {

	public CompositeBlockItem(Block block) {
		super(block);
		this.setHasSubtypes(true);
	}
	
	@Override
	public IIcon getIconFromDamage(int damage) {
		return this.field_150939_a.getIcon(0, getMetadata(damage));
	}

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
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack itemStack, int par2) {
		CompositeBlockAccessors block = (CompositeBlockAccessors)this.field_150939_a;
		return block.colorMultiplier(itemStack.getItemDamage());
	}
}
