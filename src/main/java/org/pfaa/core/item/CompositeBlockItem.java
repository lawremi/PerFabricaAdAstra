package org.pfaa.core.item;

import java.util.List;

import org.pfaa.chemica.block.IndustrialBlockAccessors;
import org.pfaa.core.block.CompositeBlockAccessors;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;

public class CompositeBlockItem extends ItemBlock implements ItemStackContainer {

	public CompositeBlockItem(Block block) {
		super(block);
		this.setHasSubtypes(true);
	}
	
	public CompositeBlockAccessors getBlock() {
		return (IndustrialBlockAccessors)this.field_150939_a;
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
	
	@Override
	public List<ItemStack> getItemStacks() {
		return this.getBlock().getItemStacks();
	}

	@Override
	public ItemStack getWilcardStack() {
		return this.getBlock().getWilcardStack();
	}
}
