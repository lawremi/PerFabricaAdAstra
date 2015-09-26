package org.pfaa.geologica.integration;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;

// FIXME: stub, will become a mod integration plugin

public class TEIntegration {

	@SuppressWarnings("unused")
	private static int getPulverizerEnergy(Strength strength) {
		switch(strength) {
		case WEAK:
			return 200;
		case MEDIUM:
			return 300;
		case STRONG:
			return 450;
		case VERY_STRONG:
			return 700;
		}
		return -1;
	}
	
	@SuppressWarnings("unused")
	private static int getCrucibleEnergy(Strength strength) {
		switch(strength) {
		case WEAK:
			return 16000;
		case MEDIUM:
			return 20000;
		case STRONG:
			return 24000;
		case VERY_STRONG:
			return 30000;
		}
		return -1;
	}
	
	@SuppressWarnings("unused")
	private static int getSmeltingEnergy(TemperatureLevel temperature) {
		switch(temperature) {
		case LOW:
			return 80;
		case MEDIUM:
			return 120;
		case HIGH:
			return 160;
		case VERY_HIGH:
			return 200;
		}
		return -1;
	}
	
	public static void addPulverizerRecipe(ItemStack input, ItemStack output, ItemStack secondaryOutput, double secondaryChance, Strength strength) {
		
	}

	public static void addCrucibleRecipe(ItemStack input, FluidStack output, Strength strength) {
		
	}

	public static void addFurnaceRecipe(ItemStack itemStack, ItemStack outputStack, TemperatureLevel temperature) {
		// TODO Auto-generated method stub
		
	}
}
