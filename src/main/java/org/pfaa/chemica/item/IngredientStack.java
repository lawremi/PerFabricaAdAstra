package org.pfaa.chemica.item;

import java.util.Set;

import net.minecraft.item.ItemStack;

public interface IngredientStack {
	public String getOreDictKey();
	public ItemStack getBestItemStack();
	public Set<ItemStack> getItemStacks();
	public int getSize();
	public Object getCraftingIngredient();
}
