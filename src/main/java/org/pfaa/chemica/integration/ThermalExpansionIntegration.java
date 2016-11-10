package org.pfaa.chemica.integration;

import java.util.List;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.RecipeRegistration;
import org.pfaa.chemica.util.ChanceStack;

import cofh.api.modhelpers.ThermalExpansionHelper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ThermalExpansionIntegration {
	public static void init() {
		if (Loader.isModLoaded(ModIds.THERMAL_EXPANSION)) {
			RecipeRegistration.addRegistry(ModIds.THERMAL_EXPANSION, new ThermalExpansionRecipeRegistry());
		}
	}
	
	public static ItemStack findSlag() {
		Item teMaterial = GameRegistry.findItem("ThermalExpansion", "material");
		return new ItemStack(teMaterial, 1, 514);
	}
	
	public static class ThermalExpansionRecipeRegistry extends AbstractRecipeRegistry {

		@Override
		public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries,
				Strength strength) {
			int energy = RecipeCostUtils.grindingEnergyForStrength(strength);
			if (secondaries.size() > 0) {
				ChanceStack secondary = secondaries.get(0);
				ThermalExpansionHelper.addPulverizerRecipe(energy, input, output, secondary.itemStack, (int)(secondary.chance * 100));
			} else {
				ThermalExpansionHelper.addPulverizerRecipe(energy, input, output);
			}
		}

		@Override
		public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp) {
			int energy = RecipeCostUtils.rfFromTemperatureLevel(temp);
			if (flux != null) {
				ThermalExpansionHelper.addSmelterRecipe(energy, input, flux, output, findSlag());
			} else {
				ThermalExpansionHelper.addFurnaceRecipe(energy, input.copy(), output);
			}
		}

		@Override
		public void registerCastingRecipe(ItemStack input, ItemStack output, ItemStack flux, int temp) {
			int energy = RecipeCostUtils.rfFromTemperature(temp);
			if (flux != null) {
				ThermalExpansionHelper.addSmelterRecipe(energy, input, flux, output, null);
			} else {
				ThermalExpansionHelper.addFurnaceRecipe(energy, input, output);
			}
		}

		@Override
		public void registerCastingRecipe(FluidStack input, ItemStack output) {
			// TODO: fluid transposer with the TCon ingot cast? Will need a filled cast item.
		}

		@Override
		public void registerMeltingRecipe(ItemStack input, FluidStack output, int temp) {
			int energy = RecipeCostUtils.rfFromTemperature(temp);
			if (output.getFluid() == FluidRegistry.LAVA) {
				energy *= 300;
			}
			ThermalExpansionHelper.addCrucibleRecipe(energy, input, output);
		}

		@Override
		public void registerAlloyingRecipe(ItemStack output, ItemStack base, List<ItemStack> solutes, int temp) {
			if (solutes.size() > 1) {
				return;
			}
			int energy = RecipeCostUtils.rfFromTemperature(temp) * output.stackSize;
			ThermalExpansionHelper.addSmelterRecipe(energy, base, solutes.get(0), output, null);
		}

		@Override
		public void registerRoastingRecipe(List<ItemStack> inputs, ItemStack output, FluidStack gas, int temp) {
			int energy = RecipeCostUtils.rfFromTemperature(temp);
			if (inputs.size() == 2) {
				ThermalExpansionHelper.addSmelterRecipe(energy, inputs.get(0), inputs.get(1), output);
			} else {
				ThermalExpansionHelper.addFurnaceRecipe(energy, inputs.get(0), output);
			}
		}

		@Override
		public void registerMixingRecipe(List<ItemStack> inputs, FluidStack additive, FluidStack fluidInput2, 
				ItemStack solidOutput, FluidStack liquidOutput, FluidStack gasOutput, 
				Condition condition, ItemStack catalyst) {
			if (inputs.size() > 1) {
				return;
			}
			if (solidOutput == null) {
				return;
			}
			if (fluidInput2 != null || liquidOutput != null) {
				return;
			}
			if (catalyst != null) {
				return;
			}
			int energy = RecipeCostUtils.rfFromCondition(condition);
			ThermalExpansionHelper.addTransposerFill(energy, inputs.get(0), solidOutput, additive, false);
		}
	}
}
