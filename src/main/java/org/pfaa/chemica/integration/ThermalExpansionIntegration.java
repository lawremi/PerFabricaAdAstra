package org.pfaa.chemica.integration;

import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.RecipeRegistration;
import org.pfaa.chemica.registration.RecipeRegistry;
import org.pfaa.chemica.util.ChanceStack;

import cofh.api.modhelpers.ThermalExpansionHelper;
import cpw.mods.fml.common.Loader;
import net.minecraft.item.ItemStack;

public class ThermalExpansionIntegration {
	public static void init() {
		if (Loader.isModLoaded(ModIds.THERMAL_EXPANSION)) {
			RecipeRegistration.getTarget().addRegistry(new ThermalExpansionRecipeRegistry());
		}
	}
	
	public static class ThermalExpansionRecipeRegistry implements RecipeRegistry {

		@Override
		public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries,
				Strength strength) {
			int energy = energyFromStrength(strength);
			if (secondaries.size() > 0) {
				ChanceStack secondary = secondaries.get(0);
				ThermalExpansionHelper.addPulverizerRecipe(energy, input, output, secondary.itemStack, (int)(secondary.chance * 100));
			} else {
				ThermalExpansionHelper.addPulverizerRecipe(energy, input, output);
			}
		}

		@Override
		public void registerCrushingRecipe(ItemStack input, ItemStack output, Strength strength) {
			this.registerGrindingRecipe(input, output, Collections.<ChanceStack>emptyList(), strength);
		}

		private static int energyFromStrength(Strength strength) {
			switch(strength) {
			case WEAK:
				return 2400;
			case MEDIUM:
				return 4000;
			case STRONG:
				return 5600;
			case VERY_STRONG:
				return 7200;
			default:
				throw new IllegalArgumentException("unhandled strength: " + strength);
			}
		}

		@Override
		public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp) {
			// TODO: if flux != null, use induction smelter instead
			ThermalExpansionHelper.addFurnaceRecipe(energyFromTemperatureLevel(temp), input, output);
		}

		private static int energyFromTemperatureLevel(TemperatureLevel temp) {
			return energyFromTemperature(temp.getReferenceTemperature());
		}
		
		@Override
		public void registerCastingRecipe(ItemStack input, ItemStack output, int temp) {
			ThermalExpansionHelper.addFurnaceRecipe(energyFromTemperature(temp), input, output);
		}

		private static int energyFromTemperature(int temp) {
			return temp;
		}
		
		// Other machines: crucible, induction smelter
	}
}
