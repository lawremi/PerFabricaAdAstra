package org.pfaa.chemica.item;

import java.util.Arrays;
import java.util.Set;

import org.pfaa.chemica.registration.IngredientList;

import net.minecraft.item.ItemStack;

public interface IngredientStack {
	public String getOreDictKey();
	public ItemStack getBestItemStack();
	public Set<ItemStack> getItemStacks();
	public int getSize();
	default float getChance() { return 1F; }
	public Object getCraftingIngredient();
	default IngredientList<IngredientStack> with(IngredientStack... stacks) {
		IngredientList<IngredientStack> ingredients = IngredientList.of(this);
		ingredients.addAll(Arrays.asList(stacks));
		return ingredients;
	}
}
