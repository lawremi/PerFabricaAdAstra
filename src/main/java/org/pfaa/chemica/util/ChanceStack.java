package org.pfaa.chemica.util;

import net.minecraft.item.ItemStack;

public class ChanceStack {
	public final ItemStack itemStack;
	public final double chance;
	
	public ChanceStack(ItemStack stack, double chance) {
		super();
		this.itemStack = stack;
		this.chance = chance;
	}
}
