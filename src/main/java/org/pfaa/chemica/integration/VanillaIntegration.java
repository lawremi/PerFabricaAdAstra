package org.pfaa.chemica.integration;

import java.util.List;
import java.util.Set;

import org.pfaa.chemica.model.Condition;
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

		private ItemStack toBucket(FluidStack fluidInput) {
			FluidStack bucketFluidStack = new FluidStack(fluidInput.getFluid(), FluidContainerRegistry.BUCKET_VOLUME);
			ItemStack filledBucket = FluidContainerRegistry.fillFluidContainer(bucketFluidStack, new ItemStack(Items.bucket));
			return filledBucket;
		}
		
		protected void registerMixingRecipe(Object[] inputs, FluidStack fluidInput, FluidStack fluidInput2,
				ItemStack output, FluidStack liquidOutput, Condition condition, Set<ItemStack> catalyst) {
			if (condition.temperature != Constants.STANDARD_TEMPERATURE) {
				return;
			}
			if (fluidInput != null && fluidInput.getFluid().isGaseous() || 
				fluidInput2 != null && fluidInput2.getFluid().isGaseous()) {
				return;
			}
			if (fluidInput.amount > FluidContainerRegistry.BUCKET_VOLUME) {
				return;
			}
			if (catalyst != null) {
				return;
			}
			int offset = fluidInput2 != null ? 1 : 0;
			inputs = Arrays.copyOf(inputs, inputs.length + 1 + offset);
			inputs[inputs.length - 1 - offset] = toBucket(fluidInput);
			if (fluidInput2 != null) {
				inputs[inputs.length - 1] = toBucket(fluidInput2);
			}
			if (output == null && liquidOutput.amount >= FluidContainerRegistry.BUCKET_VOLUME) {
				inputs = Arrays.copyOf(inputs, inputs.length + 1);
				inputs[inputs.length - 1] = new ItemStack(Items.bucket);
				output = FluidContainerRegistry.fillFluidContainer(liquidOutput, new ItemStack(Items.bucket));
			}
			RecipeUtils.addShapelessRecipe(output, inputs);
		}
		
		@Override
		public void registerMixingRecipe(List<ItemStack> inputs, FluidStack additive, FluidStack fluidInput2, 
				ItemStack output, FluidStack liquidOutput, FluidStack gasOutput, 
				Condition condition, Set<ItemStack> catalyst) {
			this.registerMixingRecipe(inputs.toArray(), additive, fluidInput2, output, liquidOutput, condition, catalyst);
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
			public void registerMixingRecipe(IngredientList inputs, FluidStack additive, FluidStack fluidInput2, 
					ItemStack output, FluidStack liquidOutput, FluidStack gasOutput, 
					Condition condition, IngredientList catalysts) {
				VanillaRecipeRegistry.this.registerMixingRecipe(
						inputs.getCraftingIngredients().toArray(), additive, fluidInput2, output, liquidOutput, condition, 
						catalysts == null ? null : catalysts.getItemStacks());
			}

			@Override
			public void registerMixingRecipe(IngredientList inputs, ItemStack output) {
				VanillaRecipeRegistry.this.registerMixingRecipe(inputs.getCraftingIngredients().toArray(), output);
			}
			
		}
	}
	
	
}
