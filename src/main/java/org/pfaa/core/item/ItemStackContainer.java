package org.pfaa.core.item;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface ItemStackContainer {
	public List<ItemStack> getItemStacks();
	default ItemStack getWilcardStack() { return null; }
}
