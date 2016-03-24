package org.pfaa.chemica.integration;

import java.util.List;

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
			RecipeRegistration.getTarget().addRegistry(new MaricultureRecipeRegistry());
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
		public void registerAbsorptionRecipe(List<ItemStack> inputs, FluidStack additive, ItemStack output, int temp) {
			if (inputs.size() > 1) {
				return;
			}
			if (temp != Constants.STANDARD_TEMPERATURE) {
				return;
			}
			RecipeVat recipe = new RecipeVat(inputs.get(0), additive, output, 1);
			MaricultureHandlers.vat.addRecipe(recipe);
		}

		@Override
		public void registerMixingRecipe(FluidStack input, List<ItemStack> additives, FluidStack output, int temp) {
			if (additives.size() > 1) {
				return;
			}
			if (temp != Constants.STANDARD_TEMPERATURE) {
				return;
			}
			RecipeVat recipe = new RecipeVat(additives.get(0), input, output, 1);
			MaricultureHandlers.vat.addRecipe(recipe);
		}
		
		// Stuff we can do with the vat: 
		//       * Drying (fluid=>solid)
		//       * Mixing (fluid+solid=>fluid, fluid+fluid=>fluid, fluid+fluid+solid=>fluid)
		//       * Absorbing (solid+fluid=>solid)
		//       * Reactions (fluid+fluid=>solid, fluid+fluid=>solid+fluid, fluid+fluid+solid=>solid+fluid)
		//       
		//       Unfortunately, no way to mix two solids into a fluid like EnderIO vat
		
		// TODO: air pump -- can we use this to collect gas? compress air?
	}
}