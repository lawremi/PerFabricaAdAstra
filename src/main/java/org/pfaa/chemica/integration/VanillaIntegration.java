package org.pfaa.chemica.integration;

import java.util.List;

import org.pfaa.chemica.model.Constants;
import org.pfaa.chemica.model.Element;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.GenericRecipeRegistry;
import org.pfaa.chemica.registration.GenericRecipeRegistryProxy;
import org.pfaa.chemica.registration.IngredientList;
import org.pfaa.chemica.registration.RecipeRegistration;
import org.pfaa.chemica.registration.RecipeUtils;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import scala.actors.threadpool.Arrays;

public class VanillaIntegration {
	public static void init() {
		RecipeRegistration.addRegistry(ModIds.VANILLA, new VanillaRecipeRegistry());
	}
	
	public static class VanillaRecipeRegistry extends AbstractRecipeRegistry {

		@Override
		public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, TemperatureLevel temp) {
			if (flux == null && temp != TemperatureLevel.VERY_HIGH) {
				FurnaceRecipes.smelting().func_151394_a(input, output, 0);
			}
		}

		@Override
		public void registerCastingRecipe(ItemStack input, ItemStack output, ItemStack flux, int temp) {
			int ironFusionTemp = Element.Fe.getFusion().getTemperature();
			if (temp <= ironFusionTemp && flux == null) {
				FurnaceRecipes.smelting().func_151394_a(input, output, 0);
			}
		}
		
		@Override
		public void registerRoastingRecipe(List<ItemStack> inputs, ItemStack output, FluidStack gas, int temp) {
			int ironFusionTemp = Element.Fe.getFusion().getTemperature();
			if (temp <= ironFusionTemp && inputs.size() == 1) {
				FurnaceRecipes.smelting().func_151394_a(inputs.get(0), output, 0);
			}
		}

		protected void registerMixingRecipe(Object[] inputs, FluidStack fluidInput, 
				ItemStack output, FluidStack fluidOutput, int temp) {
			if (temp != Constants.STANDARD_TEMPERATURE)
				return;
			if (fluidInput.amount > FluidContainerRegistry.BUCKET_VOLUME) {
				return;
			}
			FluidStack bucketFluidStack = new FluidStack(fluidInput.getFluid(), FluidContainerRegistry.BUCKET_VOLUME);
			ItemStack filledBucket = FluidContainerRegistry.fillFluidContainer(bucketFluidStack, new ItemStack(Items.bucket));
			inputs = Arrays.copyOf(inputs, inputs.length + 1);
			inputs[inputs.length - 1] = filledBucket;
			if (output == null && fluidOutput.amount >= FluidContainerRegistry.BUCKET_VOLUME) {
				inputs = Arrays.copyOf(inputs, inputs.length + 1);
				inputs[inputs.length - 1] = new ItemStack(Items.bucket);
				output = FluidContainerRegistry.fillFluidContainer(fluidOutput, new ItemStack(Items.bucket));
			}
			RecipeUtils.addShapelessRecipe(output, inputs);
		}
		
		@Override
		public void registerMixingRecipe(List<ItemStack> inputs, FluidStack additive, 
				ItemStack output, FluidStack fluidOutput, int temp) {
			this.registerMixingRecipe(inputs.toArray(), additive, output, fluidOutput, temp);
		}

		protected void registerMixingRecipe(Object[] inputs, ItemStack output) {
			RecipeUtils.addShapelessRecipe(output, inputs);
		}
		
		@Override
		public void registerMixingRecipe(List<ItemStack> inputs, ItemStack output) {
			this.registerMixingRecipe(inputs.toArray(), output);
		}

		@Override
		public GenericRecipeRegistry getGenericRecipeRegistry() {
			return new VanillaMaterialRecipeRegistry();
		}
		
		public class VanillaMaterialRecipeRegistry extends GenericRecipeRegistryProxy {

			public VanillaMaterialRecipeRegistry() {
				super(VanillaRecipeRegistry.this);
			}

			@Override
			public void registerMixingRecipe(IngredientList inputs, FluidStack additive, 
					ItemStack output, FluidStack fluidOutput, int temp) {
				VanillaRecipeRegistry.this.registerMixingRecipe(
						inputs.getCraftingIngredients().toArray(), additive, output, fluidOutput, temp);
			}

			@Override
			public void registerMixingRecipe(IngredientList inputs, ItemStack output) {
				VanillaRecipeRegistry.this.registerMixingRecipe(inputs.getCraftingIngredients().toArray(), output);
			}
			
		}
	}
	
	
}
