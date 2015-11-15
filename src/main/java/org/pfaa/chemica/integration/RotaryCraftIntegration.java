package org.pfaa.chemica.integration;

import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.OreDictUtils;
import org.pfaa.chemica.registration.RecipeRegistration;
import org.pfaa.chemica.registration.RecipeRegistry;
import org.pfaa.chemica.util.ChanceStack;

import Reika.RotaryCraft.API.RecipeInterface;
import cpw.mods.fml.common.Loader;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RotaryCraftIntegration {
	public static void init() {
		if (Loader.isModLoaded(ModIds.ROTARY_CRAFT)) {
			RecipeRegistration.getTarget().addRegistry(new RotaryCraftRecipeRegistry());
		}
	}
	
	public static class RotaryCraftRecipeRegistry implements RecipeRegistry {

		@Override
		public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries,
				Strength strength) {
			RecipeInterface.grinder.addAPIRecipe(input, output);
		}

		@Override
		public void registerCrushingRecipe(ItemStack input, ItemStack output, Strength strength) {
			this.registerGrindingRecipe(input, output, Collections.<ChanceStack>emptyList(), strength);
		}

		@Override
		public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp) {
			int blastTemp = getBlastTemperature(temp);
			if (flux == null) {
				flux = OreDictUtils.lookupBest("rubbleLimestone");
			}
			float fluxChance = 5F;
			ItemStack coal = OreDictUtils.lookupBest("fuelAnthracite");
			if (coal == null) {
				coal = new ItemStack(Items.coal);
			}
			RecipeInterface.blastfurn.addAPIAlloying(coal, 100F, 1, 
                    								 flux, fluxChance, 1,
                    								 null, 0, 1,
                    								 input, output, -1,
                    								 false, 0, blastTemp);
			ItemStack coke = OreDictUtils.lookupBest("fuelCoke");
			if (coke != null) {
				RecipeInterface.blastfurn.addAPIAlloying(coke, 100F, 1, 
						                                 flux, fluxChance / 2, 1,
						                                 null, 0, 1,
						                                 input, output, -1,
						                                 flux != null, 0, blastTemp);	
			}
		}

		/* We take the temperature of the furnace to be the temperature of the _blast_. The heat released by
		   the oxidation of the coal is assumed to double the temperature inside the furnace, but this
		   is not modeled by RotaryCraft.
		 */
		private static int getBlastTemperature(TemperatureLevel temp) {
			return temp.getReferenceTemperature() / 2;
		}
		
		@Override
		public void registerCastingRecipe(ItemStack input, ItemStack output, int temp) {}

		// Other machines: centrifuge, rock melter, wetter (solid into fluid), drying bed (solid out of fluid) 
	}
}
