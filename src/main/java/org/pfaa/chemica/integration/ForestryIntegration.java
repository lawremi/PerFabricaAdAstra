package org.pfaa.chemica.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pfaa.chemica.model.Constants;
import org.pfaa.chemica.registration.RecipeRegistration;
import org.pfaa.chemica.util.ChanceStack;

import com.google.common.base.Strings;

import cpw.mods.fml.common.Loader;
import forestry.api.recipes.RecipeManagers;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ForestryIntegration {
	public static void init() {
		if (Loader.isModLoaded(ModIds.FORESTRY)) {
			RecipeRegistration.getTarget().addRegistry(new ForestryRecipeRegistry());
		}
	}

	public static class ForestryRecipeRegistry extends AbstractRecipeRegistry {
	
		@Override
		public void registerAbsorptionRecipe(List<ItemStack> inputs, FluidStack additive, ItemStack output, int temp) {
			if (inputs.size() > 9) {
				return;
			}
			int time = 5 + (temp / Constants.STANDARD_TEMPERATURE - 1) * 100;
			Object[] ingredients = ingredientsForInputs(inputs);
			RecipeManagers.carpenterManager.addRecipe(time, additive, null, output, ingredients);
		}

		private static Object[] ingredientsForInputs(List<ItemStack> inputs) {
			String[] shape = { "", "", "" };
			List<Object> ingredients = new ArrayList<Object>(1 + inputs.size() * 2);
			for (int i = 0; i < inputs.size(); i++) {
				shape[i / 3] += i;
			}
			shape[1] = Strings.padEnd(shape[1], shape[0].length(), ' ');
			shape[2] = Strings.padEnd(shape[2], shape[0].length(), ' ');
			ingredients.add(shape);
			for (int i = 0; i < inputs.size(); i++) {
				ingredients.add(Character.forDigit(i, 10));
				ingredients.add(inputs.get(i));
			}
			return ingredients.toArray();
		}

		@Override
		public void registerPhysicalSeparationRecipe(ItemStack input, List<ChanceStack> outputs) {
			Map<ItemStack,Float> map = new HashMap<ItemStack, Float>();
			for (ChanceStack output : outputs) {
				map.put(output.itemStack, output.chance);
			}
			int time = outputs.size() * 5;
			RecipeManagers.centrifugeManager.addRecipe(time, input, map);
		}
		
		// TODO: Still (single output)
	}
}
