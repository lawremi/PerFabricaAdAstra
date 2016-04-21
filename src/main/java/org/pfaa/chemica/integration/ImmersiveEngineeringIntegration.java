package org.pfaa.chemica.integration;

import java.util.List;

import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.RecipeRegistration;
import org.pfaa.chemica.util.ChanceStack;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.common.IEContent;
import cpw.mods.fml.common.Loader;
import net.minecraft.item.ItemStack;

public class ImmersiveEngineeringIntegration {
	public static void init() {
		if (Loader.isModLoaded(ModIds.IMMERSIVE_ENGINEERING)) {
			RecipeRegistration.addRegistry(ModIds.IMMERSIVE_ENGINEERING,
					new ImmersiveEngineeringRecipeRegistry());
		}
	}
	
	public static class ImmersiveEngineeringRecipeRegistry extends AbstractRecipeRegistry {

		private static final int ARC_ENERGY_PER_TICK = 512;
		
		@Override
		public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries,
				Strength strength) {
			int energy = RecipeCostUtils.grindingEnergyForStrength(strength);
			CrusherRecipe recipe = CrusherRecipe.addRecipe(output, input, energy / 2);
			for (ChanceStack secondary : secondaries) {
				recipe.addToSecondaryOutput(secondary.itemStack, secondary.chance);	
			}
		}

		@Override
		public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp) {
			ItemStack slag = new ItemStack(IEContent.itemMaterial, 1, 13);
			BlastFurnaceRecipe.addRecipe(output, input, RecipeCostUtils.blastTicksForTemperatureLevel(temp), slag);
		}

		@Override
		public void registerCastingRecipe(ItemStack input, ItemStack output, ItemStack flux, int temp) {
			int time = RecipeCostUtils.arcTicksForTemperature(temp);
			if (flux != null) {
				ArcFurnaceRecipe.addRecipe(output, input, null, time, ARC_ENERGY_PER_TICK, flux);
			} else {
				ArcFurnaceRecipe.addRecipe(output, input, null, time, ARC_ENERGY_PER_TICK);
			}
		}

		@Override
		public void registerAlloyingRecipe(ItemStack output, ItemStack base, List<ItemStack> solutes, int temp) {
			if (solutes.size() > 4) {
				return;
			}
			int time = RecipeCostUtils.arcTicksForTemperature(temp) * output.stackSize;
			ArcFurnaceRecipe recipe = ArcFurnaceRecipe.addRecipe(output, base, null, time, ARC_ENERGY_PER_TICK, 
					solutes.toArray(new Object[0]));
			recipe.setSpecialRecipeType("Alloying");
		}

		@Override
		public void registerRoastingRecipe(List<ItemStack> inputs, ItemStack output, int temp) {
			int time = RecipeCostUtils.arcTicksForTemperature(temp);
			Object[] additives = inputs.subList(1, inputs.size()).toArray();
			ArcFurnaceRecipe.addRecipe(output, inputs.get(0), null, time, ARC_ENERGY_PER_TICK, additives);
		}

		// Other machines: coke oven
	}

}
