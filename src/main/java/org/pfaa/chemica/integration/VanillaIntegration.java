package org.pfaa.chemica.integration;

import java.util.ArrayList;
import java.util.List;

import org.pfaa.chemica.model.Constants;
import org.pfaa.chemica.model.Element;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.registration.RecipeRegistration;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class VanillaIntegration {
	public static void init() {
		RecipeRegistration.getTarget().addRegistry(new VanillaRecipeRegistry());
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

		@Override
		public void registerAbsorptionRecipe(List<ItemStack> inputs, FluidStack additive, ItemStack output, int temp) {
			if (temp != Constants.STANDARD_TEMPERATURE)
				return;
			FluidStack bucketFluidStack = new FluidStack(additive.getFluid(), FluidContainerRegistry.BUCKET_VOLUME);
			ItemStack filledBucket = FluidContainerRegistry.fillFluidContainer(bucketFluidStack, new ItemStack(Items.bucket));
			List<ItemStack> craftingInputs = new ArrayList<ItemStack>(inputs);
			craftingInputs.add(filledBucket);
			CraftingManager.getInstance().addShapelessRecipe(output, craftingInputs.toArray());
		}

		@Override
		public void registerMixingRecipe(List<ItemStack> inputs, ItemStack output) {
			CraftingManager.getInstance().addShapelessRecipe(output, inputs.toArray());
		}
	}
}
