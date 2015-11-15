package org.pfaa.chemica.integration;

import java.util.List;

import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.RecipeRegistration;
import org.pfaa.chemica.registration.RecipeRegistry;
import org.pfaa.chemica.util.ChanceStack;

import appeng.api.features.IGrinderRegistry;
import appeng.core.AEConfig;
import appeng.core.Api;
import cpw.mods.fml.common.Loader;
import net.minecraft.item.ItemStack;

public class AppliedEnergistics2Integration {
	
	public static void init() {
		if (Loader.isModLoaded(ModIds.APPLIED_ENERGISTICS_2)) {
			RecipeRegistration.getTarget().addRegistry(new AppliedEnergistics2RecipeRegistry());
		}
	}
	
	public static class AppliedEnergistics2RecipeRegistry implements RecipeRegistry {

		@Override
		public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries,
				Strength strength) {
			int cost = costFromStrength(strength);
			IGrinderRegistry grinder = Api.INSTANCE.registries().grinder();
			if (secondaries.size() > 0) {
				ChanceStack secondary = secondaries.get(0);
				if (secondaries.size() > 1) {
					ChanceStack tertiary = secondaries.get(1);
					grinder.addRecipe(input, output, secondary.itemStack, secondary.chance, 
							          tertiary.itemStack, tertiary.chance, cost);
				} else {
					grinder.addRecipe(input, output, secondary.itemStack, secondary.chance, cost);
				}
			} else {
				grinder.addRecipe(input, output, cost);
			}
		}
		
		@Override
		public void registerCrushingRecipe(ItemStack input, ItemStack output, Strength strength) {
			IGrinderRegistry grinder = Api.INSTANCE.registries().grinder();
			int cost = costFromStrength(strength);
			if (output.stackSize > 1) {
				output = output.copy();
				ItemStack extra = output.splitStack(output.stackSize / 2);
				grinder.addRecipe(input, output, extra, (float) ( AEConfig.instance.oreDoublePercentage / 100.0 ), cost);
			} else {
				grinder.addRecipe(input, output, cost);
			}
		}

		private static int costFromStrength(Strength strength) {
			switch(strength) {
			case WEAK:
				return 4;
			case MEDIUM:
				return 8;
			case STRONG:
				return 12;
			case VERY_STRONG:
				return 16;
			default:
				throw new IllegalArgumentException("unhandled strength: " + strength);
			}
		}
		
		@Override
		public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp) {}

		@Override
		public void registerCastingRecipe(ItemStack input, ItemStack output, int temp) {}

	}
}
