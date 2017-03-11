package org.pfaa.chemica.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Constants;
import org.pfaa.chemica.registration.BaseRecipeRegistration;
import org.pfaa.chemica.registration.GenericRecipeRegistry;
import org.pfaa.chemica.registration.GenericRecipeRegistryProxy;
import org.pfaa.chemica.registration.IngredientList;
import org.pfaa.core.item.ChanceStack;

import com.google.common.base.Strings;

import cpw.mods.fml.common.Loader;
import forestry.api.recipes.RecipeManagers;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ForestryIntegration {
	public static void init() {
		if (Loader.isModLoaded(ModIds.FORESTRY)) {
			BaseRecipeRegistration.putRegistry(ModIds.FORESTRY, new ForestryRecipeRegistry());
		}
	}

	public static class ForestryRecipeRegistry extends AbstractRecipeRegistry {
	
		@Override
		public void registerMixingRecipe(List<ItemStack> inputs, FluidStack fluidInput, FluidStack fluidInput2, 
				ItemStack output, FluidStack liquidOutput, FluidStack gasOutput, 
				Condition condition, Set<ItemStack> catalyst) {
			if (liquidOutput != null || gasOutput != null) {
				return;
			}
			this.registerMixingRecipe(inputs.toArray(), fluidInput, fluidInput2, output, condition, catalyst);
		}

		protected void registerMixingRecipe(Object[] inputs, FluidStack additive, FluidStack additive2, 
				ItemStack output, Condition condition, Set<ItemStack> catalyst) 
		{
			if (inputs.length > 9) {
				return;
			}
			if (additive2 != null) {
				return;
			}
			if (catalyst != null) {
				return;
			}
			int time = 5 + (condition.temperature / Constants.STANDARD_TEMPERATURE - 1) * 100;
			Object[] ingredients = ingredientsForInputs(inputs);
			RecipeManagers.carpenterManager.addRecipe(time, additive, null, output, ingredients);
		}
		
		private static Object[] ingredientsForInputs(Object[] inputs) {
			String[] shape = { "", "", "" };
			List<Object> ingredients = new ArrayList<Object>(1 + inputs.length * 2);
			for (int i = 0; i < inputs.length; i++) {
				shape[i / 3] += i;
			}
			shape[1] = Strings.padEnd(shape[1], shape[0].length(), ' ');
			shape[2] = Strings.padEnd(shape[2], shape[0].length(), ' ');
			ingredients.add(shape);
			for (int i = 0; i < inputs.length; i++) {
				ingredients.add(Character.forDigit(i, 10));
				ingredients.add(inputs[i]);
			}
			return ingredients.toArray();
		}

		@Override
		public void registerMechanicalSeparationRecipe(ItemStack input, List<ChanceStack> outputs) {
			Map<ItemStack,Float> map = new HashMap<ItemStack, Float>();
			for (ChanceStack output : outputs) {
				map.put(output.itemStack, output.chance);
			}
			int time = outputs.size() * 5;
			RecipeManagers.centrifugeManager.addRecipe(time, input, map);
		}

		@Override
		public GenericRecipeRegistry getGenericRecipeRegistry() {
			return new ForestryMaterialRecipeRegistry();
		}
		
		public class ForestryMaterialRecipeRegistry extends GenericRecipeRegistryProxy {

			public ForestryMaterialRecipeRegistry() {
				super(ForestryRecipeRegistry.this);
			}

			@Override
			public void registerMixingRecipe(IngredientList<?> inputs, FluidStack additive, FluidStack fluidInput2, 
					ItemStack output, FluidStack liquidOutput, FluidStack gasOutput, 
					Condition condition, IngredientList<?> catalysts) {
				if (liquidOutput != null)
					return;
				ForestryRecipeRegistry.this.registerMixingRecipe(
						inputs.getCraftingIngredients().toArray(), 
						additive, fluidInput2, output, condition, 
						catalysts == null ? null : catalysts.getItemStacks());
			}

		}

		// TODO: Still (single output)
	}
}
