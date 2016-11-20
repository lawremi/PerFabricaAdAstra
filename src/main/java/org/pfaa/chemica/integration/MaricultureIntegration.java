package org.pfaa.chemica.integration;

import java.util.List;
import java.util.Set;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Constants;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.OreDictUtils;
import org.pfaa.chemica.registration.RecipeRegistration;
import org.pfaa.chemica.util.ChanceStack;

import cpw.mods.fml.common.Loader;
import mariculture.api.core.MaricultureHandlers;
import mariculture.api.core.RecipeCasting;
import mariculture.api.core.RecipeCasting.RecipeBlockCasting;
import mariculture.api.core.RecipeCasting.RecipeIngotCasting;
import mariculture.api.core.RecipeCasting.RecipeNuggetCasting;
import mariculture.api.core.RecipeSmelter;
import mariculture.api.core.RecipeVat;
import mariculture.api.fishery.Fishing;
import mariculture.api.fishery.RecipeSifter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class MaricultureIntegration {
	public static void init() {
		if (Loader.isModLoaded(ModIds.MARICULTURE)) {
			RecipeRegistration.addRegistry(ModIds.MARICULTURE, new MaricultureRecipeRegistry());
		}
	}

	public static class MaricultureRecipeRegistry extends AbstractRecipeRegistry {

		@Override
		public void registerPhysicalSeparationRecipe(ItemStack input, List<ChanceStack> outputs) {
			int[] oreIds = OreDictionary.getOreIDs(input);
			for (int oreId : oreIds) {
				String oreName = OreDictionary.getOreName(oreId);
				if (!oreName.startsWith("dust")) {
					continue;
				}
				for (ChanceStack output : outputs.subList(0, 1)) { // currently restricted to first output
					ItemStack outputStack = output.itemStack;
					int amount = outputStack.stackSize;
					int chance = (int)(output.chance * 100);
					RecipeSifter recipe = new RecipeSifter(outputStack, oreName, amount, amount, chance);
					Fishing.sifter.addRecipe(recipe);
				}
			}
		}

		@Override
		public void registerSmeltingRecipe(ItemStack input, FluidStack output, ItemStack flux, TemperatureLevel temp) {
			if (flux != null) {
				return;
			}
			RecipeSmelter recipe = new RecipeSmelter(input, temp.getReferenceTemperature() - 300, output, null, 0);
			MaricultureHandlers.crucible.addRecipe(recipe);
		}

		@Override
		public void registerCastingRecipe(FluidStack input, ItemStack output) {
			RecipeCasting recipe;
			if (OreDictUtils.hasPrefix(output, "block"))
				recipe = new RecipeBlockCasting(input, output);
			else if (OreDictUtils.hasPrefix(output, "ingot"))
				recipe = new RecipeIngotCasting(input, output);
			else if (OreDictUtils.hasPrefix(output, "nugget"))
				recipe = new RecipeNuggetCasting(input, output);
			else return;
			MaricultureHandlers.casting.addRecipe(recipe);
		}

		@Override
		public void registerMeltingRecipe(ItemStack input, FluidStack output, int temp) {
			RecipeSmelter recipe = new RecipeSmelter(input, temp - 273, output, null, 0);
			MaricultureHandlers.crucible.addRecipe(recipe);
		}

		@Override
		public void registerAlloyingRecipe(FluidStack output, List<FluidStack> inputs) {
			if (inputs.size() != 2) {
				return;
			}
			RecipeVat recipe = new RecipeVat(inputs.get(0), inputs.get(1), output, 1);
			MaricultureHandlers.vat.addRecipe(recipe);
		}

		@Override
		public void registerMixingRecipe(List<ItemStack> solidInputs, FluidStack fluidInput, FluidStack fluidInput2, 
				ItemStack solidOutput, FluidStack liquidOutput, FluidStack gasOutput, 
				Condition condition, Set<ItemStack> catalyst) {
			if (solidInputs.size() > 1) {
				return;
			}
			if (condition.temperature != Constants.STANDARD_TEMPERATURE) {
				return;
			}
			if (fluidInput != null && fluidInput.getFluid().isGaseous() || 
				fluidInput2 != null && fluidInput2.getFluid().isGaseous()) {
				return;
			}
			if (catalyst != null) {
				return;
			}
			RecipeVat recipe = new RecipeVat(solidInputs.get(0), fluidInput, fluidInput2, liquidOutput, solidOutput, 1);
			MaricultureHandlers.vat.addRecipe(recipe);
		}

		private static final int MAX_FREEZING_TIME = 10;
		
		@Override
		public void registerFreezingRecipe(FluidStack input, ItemStack output, int temp) {
			if (temp < Constants.STANDARD_TEMPERATURE) {
				return;
			}
			int time = (int)(MAX_FREEZING_TIME * (Constants.STANDARD_TEMPERATURE / (float)temp));
			RecipeVat recipe = new RecipeVat(input, output, time);
			MaricultureHandlers.vat.addRecipe(recipe);
		}
		
		// Stuff we can do with the vat: 
		//       * Drying/freezing (fluid=>solid)
		//       * Mixing (fluid+solid=>fluid, fluid+fluid=>fluid, fluid+fluid+solid=>fluid)
		//       * Absorbing (solid+fluid=>solid)
		//       * Reactions (fluid+fluid=>solid, fluid+fluid=>solid+fluid, fluid+fluid+solid=>solid+fluid)
		//       
		//       Unfortunately, no way to mix two solids into a fluid like EnderIO vat
		
		// TODO: air pump -- can we use this to collect gas? compress air?
	}
}