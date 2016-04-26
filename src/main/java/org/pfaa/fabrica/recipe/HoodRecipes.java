package org.pfaa.fabrica.recipe;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class HoodRecipes {
	
	private static Map<ItemStackKey, FluidStack> recipes = Maps.newHashMap();
	
	private static class ItemStackKey {
		private Item item;
		private int damage;
		public ItemStackKey(ItemStack itemStack) {
			this.item = itemStack.getItem();
			this.damage = itemStack.getItemDamage();
		}
		public boolean equals(Object other) {
			if (other instanceof ItemStackKey) {
				ItemStackKey otherKey = (ItemStackKey)other;
				return otherKey.item == this.item && otherKey.damage == this.damage;
			}
			return false;
		}
		public int hashCode() {
			return (this.item.hashCode() * 31) + this.damage;
		}
	}
	
	public static void addRecipe(ItemStack input, FluidStack output) {
		FluidStack outputCopy = output.copy();
		outputCopy.amount /= input.stackSize;
		recipes.put(new ItemStackKey(input), outputCopy);
	}
	
	public static FluidStack getOutput(ItemStack input) {
		FluidStack output = recipes.get(new ItemStackKey(input));
		if (output != null) {
			output.amount *= input.stackSize;
		}
		return output;
	}
}
