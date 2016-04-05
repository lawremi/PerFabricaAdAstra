package org.pfaa.chemica.integration;

import java.util.List;

import org.pfaa.chemica.model.Constants;
import org.pfaa.chemica.model.Element;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.MaterialRecipeRegistry;
import org.pfaa.chemica.registration.MaterialRecipeRegistryProxy;
import org.pfaa.chemica.registration.RecipeRegistration;
import org.pfaa.fabrica.registration.MaterialStackList;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
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
		public void registerRoastingRecipe(List<ItemStack> inputs, ItemStack output, int temp) {
			int ironFusionTemp = Element.Fe.getFusion().getTemperature();
			if (temp <= ironFusionTemp && inputs.size() == 1) {
				FurnaceRecipes.smelting().func_151394_a(inputs.get(0), output, 0);
			}
		}

		protected void registerAbsorptionRecipe(Object[] inputs, FluidStack additive, ItemStack output, int temp) {
			if (temp != Constants.STANDARD_TEMPERATURE)
				return;
			if (additive.amount > FluidContainerRegistry.BUCKET_VOLUME) {
				return;
			}
			FluidStack bucketFluidStack = new FluidStack(additive.getFluid(), FluidContainerRegistry.BUCKET_VOLUME);
			ItemStack filledBucket = FluidContainerRegistry.fillFluidContainer(bucketFluidStack, new ItemStack(Items.bucket));
			inputs = Arrays.copyOf(inputs, inputs.length + 1);
			inputs[inputs.length - 1] = filledBucket;
			CraftingManager.getInstance().addShapelessRecipe(output, inputs);
		}
		
		@Override
		public void registerAbsorptionRecipe(List<ItemStack> inputs, FluidStack additive, ItemStack output, int temp) {
			this.registerAbsorptionRecipe(inputs.toArray(), additive, output, temp);
		}

		protected void registerMixingRecipe(Object[] inputs, ItemStack output) {
			CraftingManager.getInstance().addShapelessRecipe(output, inputs);
		}
		
		@Override
		public void registerMixingRecipe(List<ItemStack> inputs, ItemStack output) {
			this.registerMixingRecipe(inputs.toArray(), output);
		}

		@Override
		public MaterialRecipeRegistry getMaterialRecipeRegistry() {
			return new VanillaMaterialRecipeRegistry();
		}
		
		public class VanillaMaterialRecipeRegistry extends MaterialRecipeRegistryProxy {

			public VanillaMaterialRecipeRegistry() {
				super(VanillaRecipeRegistry.this);
			}

			@Override
			public void registerAbsorptionRecipe(MaterialStackList inputs, FluidStack additive, ItemStack output,
					int temp) {
				VanillaRecipeRegistry.this.registerAbsorptionRecipe(inputs.getOreDictKeys().toArray(), additive, output, temp);
			}

			@Override
			public void registerMixingRecipe(MaterialStackList inputs, ItemStack output) {
				VanillaRecipeRegistry.this.registerMixingRecipe(inputs.getOreDictKeys().toArray(), output);
			}
			
		}
	}
	
	
}
