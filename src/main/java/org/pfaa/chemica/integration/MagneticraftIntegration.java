package org.pfaa.chemica.integration;

import java.util.List;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.registration.RecipeRegistration;
import org.pfaa.chemica.util.ChanceStack;

import com.cout970.magneticraft.api.access.MgRecipeRegister;

import cpw.mods.fml.common.Loader;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class MagneticraftIntegration {
	public static void init() {
		if (Loader.isModLoaded(ModIds.MAGNETICRAFT)) {
			RecipeRegistration.addRegistry(ModIds.MAGNETICRAFT, new MagneticraftRecipeRegistry());
		}
	}
	
	public static class MagneticraftRecipeRegistry extends AbstractRecipeRegistry {

		@Override
		public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries,
				Strength strength) {
			ItemStack extra1 = null, extra2 = null;
			float prob1 = 0, prob2 = 0;
			if (secondaries.size() > 0) {
				extra1 = secondaries.get(0).itemStack;
				prob1 = secondaries.get(0).chance;
			}
			if (secondaries.size() > 1) {
				extra2 = secondaries.get(1).itemStack;
				prob2 = secondaries.get(1).chance;
			}
			MgRecipeRegister.registerGrinderRecipe(input, output, extra1, prob1, extra2, prob2);
		}

		@Override
		public void registerCrushingRecipe(ItemStack input, ItemStack output, ChanceStack dust, Strength strength) {
			if (dust != null) {
				MgRecipeRegister.registerCrusherRecipe(input, output, dust.itemStack, dust.chance, null, 0);
			} else {
				MgRecipeRegister.registerCrusherRecipe(input, output, null, 0, null, 0);
			}
			MgRecipeRegister.registerHammerTableRecipe(input, output);
		}

		@Override
		public void registerPhysicalSeparationRecipe(ItemStack input, List<ChanceStack> outputs) {
			ItemStack extra = null;
			float prob = 0F;
			if (outputs.size() > 1) {
				extra = outputs.get(1).itemStack;
				prob = outputs.get(1).chance;
			}
			MgRecipeRegister.registerSifterRecipe(input, outputs.get(0).itemStack, extra, prob);
		}

		@Override
		public void registerDistillationRecipe(FluidStack input, List<FluidStack> outputs, Condition condition) {
			/* Two steps: 
			 * 1) Boil in distillery.
			 * 2) Separate in refinery to three outputs
			 *    - Top fraction (LPG + light/heavy naphtha)
			 *    - Middle fraction (kerosene + light/heavy gas oil)
			 *    - Bottom fraction (bitumen)
			 *    
			 * Secondary columns separate the top and middle fractions.
			 */
		}
		
	}

}
