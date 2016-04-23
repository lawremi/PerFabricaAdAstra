package org.pfaa.fabrica.recipe;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class HoodRecipes {
	
	private static Map<ItemStack, FluidStack> recipes = Maps.newIdentityHashMap();
	
	public static void addRecipe(ItemStack input, FluidStack output) {
		FluidStack outputCopy = output.copy();
		ItemStack inputCopy = input.copy();
		outputCopy.amount /= input.stackSize;
		inputCopy.stackSize = 1;
		recipes.put(inputCopy, outputCopy);
	}
	
	public static FluidStack getOutput(ItemStack input) {
		ItemStack inputCopy = input.copy();
		inputCopy.stackSize = 1;
		FluidStack output = recipes.get(inputCopy).copy();
		output.amount *= input.stackSize;
		return output;
	}
}
