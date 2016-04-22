package org.pfaa.chemica.item;

import java.util.Collections;
import java.util.Set;

import net.minecraft.item.ItemStack;

public class ItemIngredientStack implements IngredientStack {

	private ItemStack itemStack;
	
	public ItemIngredientStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}
	
	@Override
	public String getOreDictKey() {
		return null;
	}

	@Override
	public ItemStack getBestItemStack() {
		return this.itemStack;
	}

	@Override
	public Set<ItemStack> getItemStacks() {
		return Collections.singleton(this.itemStack);
	}

	@Override
	public int getSize() {
		return this.itemStack.stackSize;
	}

}
