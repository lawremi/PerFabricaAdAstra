package org.pfaa.chemica.util;

import net.minecraft.item.ItemStack;

public class ChanceStack {
	public final ItemStack itemStack;
	public final float chance;
	
	public ChanceStack(ItemStack stack, float chance) {
		super();
		this.itemStack = stack.copy();
		this.chance = fudgeForUI(chance);
	}
	
	private static float fudgeForUI(float chance) {
		return chance + 0.00001F;
	}

	public ChanceStack weightChance(float weight) {
		ItemStack itemStack = this.itemStack.copy();
		float chance = this.chance;
		itemStack.stackSize *= weight;
		if (itemStack.stackSize == 0) {
			itemStack.stackSize = 1;
			chance *= weight * this.itemStack.stackSize;
		}
		return new ChanceStack(itemStack, chance);
	}
}
