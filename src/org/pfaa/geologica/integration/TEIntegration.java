package org.pfaa.geologica.integration;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.pfaa.geologica.GeoMaterial.Strength;
import org.pfaa.geologica.processing.Ore.SmeltingTemperature;

public class TEIntegration {

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
	
	private static int getSmeltingEnergy(SmeltingTemperature temperature) {
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

	public static void addFurnaceRecipe(ItemStack itemStack, ItemStack outputStack, SmeltingTemperature temperature) {
		// TODO Auto-generated method stub
		
	}
}
