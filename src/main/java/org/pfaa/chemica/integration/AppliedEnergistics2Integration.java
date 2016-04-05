package org.pfaa.chemica.integration;

import java.util.ArrayList;
import java.util.List;

import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.registration.RecipeRegistration;
import org.pfaa.chemica.util.ChanceStack;

import appeng.api.features.IGrinderRegistry;
import appeng.core.AEConfig;
import appeng.core.Api;
import cpw.mods.fml.common.Loader;
import net.minecraft.item.ItemStack;

public class AppliedEnergistics2Integration {
	
	public static void init() {
		if (Loader.isModLoaded(ModIds.APPLIED_ENERGISTICS_2)) {
			RecipeRegistration.addRegistry(ModIds.APPLIED_ENERGISTICS_2, 
					new AppliedEnergistics2RecipeRegistry());
		}
	}
	
	public static class AppliedEnergistics2RecipeRegistry extends AbstractRecipeRegistry {

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
		public void registerCrushingRecipe(ItemStack input, ItemStack output, ChanceStack dust, Strength strength) {
			List<ChanceStack> secondaries = new ArrayList<ChanceStack>(2);
			if (output.stackSize > 1) {
				output = output.copy();
				secondaries.add(new ChanceStack(output.splitStack(output.stackSize / 2),
					(float) ( AEConfig.instance.oreDoublePercentage / 100.0 )));
			}
			if (dust != null) {
				secondaries.add(dust);
			}
			this.registerGrindingRecipe(input, output, secondaries, strength);
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
	}
}
