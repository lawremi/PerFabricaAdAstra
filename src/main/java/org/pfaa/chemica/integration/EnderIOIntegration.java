package org.pfaa.chemica.integration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.registration.BaseRecipeRegistration;
import org.pfaa.core.item.ChanceStack;

import cpw.mods.fml.common.Loader;
import crazypants.enderio.machine.MachineRecipeInput;
import crazypants.enderio.machine.alloy.AlloyRecipeManager;
import crazypants.enderio.machine.crusher.CrusherRecipeManager;
import crazypants.enderio.machine.recipe.BasicManyToOneRecipe;
import crazypants.enderio.machine.recipe.IRecipe;
import crazypants.enderio.machine.recipe.Recipe;
import crazypants.enderio.machine.recipe.RecipeBonusType;
import crazypants.enderio.machine.recipe.RecipeInput;
import crazypants.enderio.machine.recipe.RecipeOutput;
import crazypants.enderio.machine.vat.VatRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class EnderIOIntegration {
	public static void init() {
		if (Loader.isModLoaded(ModIds.ENDER_IO)) {
			BaseRecipeRegistration.putRegistry(ModIds.ENDER_IO, new EnderIORecipeRegistry());
		}
	}
	
	public static class EnderIORecipeRegistry extends AbstractRecipeRegistry {

		@Override
		public void registerGrindingRecipe(ItemStack input, ItemStack output, List<ChanceStack> secondaries,
				Strength strength) {
			// EnderIO seems to add recipes based on the ore dictionary? We have to remove them here.
			IRecipe existingRecipe = CrusherRecipeManager.getInstance().getRecipeForInput(input);
			CrusherRecipeManager.getInstance().getRecipes().remove(existingRecipe);
			List<RecipeOutput> outputs = new ArrayList<RecipeOutput>(secondaries.size() + 1);
			outputs.add(new RecipeOutput(output));
			for (ChanceStack secondary : secondaries) {
				outputs.add(new RecipeOutput(secondary.itemStack, secondary.chance));
			}
			int energy = (RecipeCostUtils.grindingEnergyForStrength(strength) * 3) / 4;
			Recipe recipe = new Recipe(new RecipeInput(input, true), energy, RecipeBonusType.MULTIPLY_OUTPUT, 
					outputs.toArray(new RecipeOutput[0]));
			CrusherRecipeManager.getInstance().addRecipe(recipe);
		}
		
		private void registerAlloyFurnaceRecipe(ItemStack input, ItemStack output, int energy) {
			// EnderIO seems to add recipes from the vanilla furnace. We have to remove them here.
			MachineRecipeInput machineInput = new MachineRecipeInput(0, input);
			IRecipe existingRecipe = AlloyRecipeManager.getInstance().getRecipeForInputs(new MachineRecipeInput[] { machineInput });
			AlloyRecipeManager.getInstance().getRecipes().remove(existingRecipe);
			energy += energy / 4; // slight penalty since the furnace is for alloying
			Recipe recipe = new Recipe(new RecipeInput(input, true), energy, RecipeBonusType.NONE, new RecipeOutput(output));
			AlloyRecipeManager.getInstance().addRecipe(new BasicManyToOneRecipe(recipe));
		}

		private void registerAlloyFurnaceRecipe(List<ItemStack> inputs, ItemStack output, int temp) {
			if (inputs.size() > 2) {
				return;
			}
			List<RecipeInput> recipeInputs = new ArrayList<RecipeInput>(inputs.size());
			for (ItemStack solute : inputs) {
				recipeInputs.add(new RecipeInput(solute, true));
			}
			int energy = RecipeCostUtils.rfFromTemperature(temp) * output.stackSize;
			RecipeInput[] inputArray = recipeInputs.toArray(new RecipeInput[0]);
			RecipeOutput[] outputArray = new RecipeOutput[] { new RecipeOutput(output) };
			Recipe recipe = new Recipe(inputArray, outputArray, energy, RecipeBonusType.NONE);
			AlloyRecipeManager.getInstance().addRecipe(new BasicManyToOneRecipe(recipe));
		}

		@Override
		public void registerSmeltingRecipe(ItemStack input, ItemStack output, ItemStack flux, int temp) {
			if (flux != null) {
				return;
			}
			int energy = RecipeCostUtils.rfFromSmeltingTemperature(temp);
			registerAlloyFurnaceRecipe(input, output, energy);
		}

		@Override
		public void registerCastingRecipe(ItemStack input, ItemStack output, ItemStack flux, int temp, int energy) {
			int rf = RecipeCostUtils.rfFromTemperature(temp);
			if (flux != null) {
				List<ItemStack> solute = Collections.singletonList(flux);
				this.registerAlloyingRecipe(output, input, solute, temp, 0);
			} else {
				registerAlloyFurnaceRecipe(input, output, rf);
			}
		}

		@Override
		public void registerAlloyingRecipe(ItemStack output, ItemStack base, List<ItemStack> solutes, int temp, int energy) {
			List<ItemStack> inputs = new ArrayList<ItemStack>(solutes.size() + 1);
			inputs.add(base);
			inputs.addAll(solutes);
			this.registerAlloyFurnaceRecipe(inputs, output, temp);
		}

		@Override
		public void registerRoastingRecipe(List<ItemStack> inputs, ItemStack output, FluidStack gas, int temp) {
			this.registerAlloyFurnaceRecipe(inputs, output, temp);
		}
		
		private static int VAT_BOILING_ENERGY = 10000;
		
		@Override
		public void registerMixingRecipe(List<ItemStack> solidInputs, FluidStack fluidInput, FluidStack fluidInput2, 
				ItemStack solidOutput, FluidStack liquidOutput, FluidStack gasOutput,
				Condition condition, Set<ItemStack> catalyst) {
			if (solidInputs.size() > 2) {
				return;
			}
			if (fluidInput2 != null) {
				return;
			}
			if (liquidOutput == null) {
				return;
			}
			if (catalyst != null && !catalyst.isEmpty()) {
				return;
			}
			List<RecipeInput> recipeInputs = new ArrayList<RecipeInput>(solidInputs.size() + 1);
			recipeInputs.add(new RecipeInput(fluidInput));
			for (ItemStack solute : solidInputs) {
				recipeInputs.add(new RecipeInput(solute, true));
			}
			int energy = VAT_BOILING_ENERGY * condition.temperature / Compounds.H2O.getFusion().getTemperature();
			RecipeInput[] inputArray = recipeInputs.toArray(new RecipeInput[0]);
			RecipeOutput[] outputArray = new RecipeOutput[] { new RecipeOutput(liquidOutput) };
			Recipe recipe = new Recipe(inputArray, outputArray, energy, RecipeBonusType.NONE);
			VatRecipeManager.getInstance().addRecipe(recipe);
		}
	}

}
