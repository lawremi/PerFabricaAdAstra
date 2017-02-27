package org.pfaa.core.block;

import java.util.List;

import org.pfaa.core.item.ItemStackContainer;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public interface CompositeBlockAccessors extends ItemStackContainer {
	public static final int META_WIDTH = 16;
	
	public abstract String getBlockNameSuffix(int meta);
	default ItemStack getItemStack(int meta) {
		return new ItemStack((Block)this, 1, meta);
	}
	default List<ItemStack> getItemStacks() {
		List<ItemStack> stacks = Lists.newArrayList();
		for (int m = 0; m < this.getMetaCount(); m++) {
			stacks.add(this.getItemStack(m));
		}
		return stacks;
	}
	default ItemStack getWildcardStack() {
		return new ItemStack((Block)this, 1, OreDictionary.WILDCARD_VALUE);
	}
	public abstract int getMetaCount();
	
	public abstract boolean enableOverlay();
	public abstract void disableOverlay();
	
	public abstract int colorMultiplier(int meta);
}
