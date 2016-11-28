package org.pfaa.chemica.registration;

import java.util.List;

import org.pfaa.chemica.fluid.IndustrialFluids;
import org.pfaa.chemica.item.IngredientStack;
import org.pfaa.chemica.item.MaterialStack;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Equation.Term;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.Reaction;
import org.pfaa.chemica.model.SimpleMixture;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ReactionRegistry {
	private GenericRecipeRegistry delegate;
	
	public ReactionRegistry(GenericRecipeRegistry delegate) {
		this.delegate = delegate;
	}
	
	public void registerRoastingReaction(Reaction reaction) {
		this.registerRoastingReaction(Forms.DUST, reaction);
		this.registerRoastingReaction(Forms.DUST_TINY, reaction);
	}
	
	private void registerRoastingReaction(Form form, Reaction reaction) {
		float scale = 1F / reaction.getReactants().get(0).stoichiometry;
		reaction = reaction.scale(scale);
		ItemStack output = this.getProductItemStack(form, reaction);
		if (output == null) {
			return;
		}
		FluidStack gas = this.getProductFluidStack(form, reaction, State.GAS);
		IngredientList inputs = this.getSolidReactants(form, reaction);
		int temp = reaction.getCanonicalCondition().temperature;
		delegate.registerRoastingRecipe(inputs, output, gas, temp);
	}
	
	private IngredientList getSolidReactants(Form form, Reaction reaction) {
		List<Term> reactants = reaction.getReactants(State.SOLID);
		List<IngredientStack> stacks = Lists.newArrayListWithCapacity(reactants.size());
		for (Term reactant : reactants) {
			stacks.add(new MaterialStack(form, reactant.chemical, (int)reactant.stoichiometry));
		}
		return new IngredientList(stacks);
	}
	
	private ItemStack getProductItemStack(Form form, Reaction reaction) {
		for (Term product : reaction.getProducts(State.SOLID)) {
			float amount = product.stoichiometry;
			if (amount < 1) {
				if (form == Forms.DUST) {
					form = Forms.DUST_TINY;
					amount *= Forms.DUST_TINY.getNumberPerBlock() / Forms.DUST.getNumberPerBlock();
				} else continue;
			}
			return new MaterialStack(form, product.chemical, (int)amount).getBestItemStack();
		}
		return null;
	}

	private List<FluidStack> getReactantFluidStacks(Form form, Reaction reaction) {
		List<FluidStack> fluidStacks = Lists.newArrayList();
		for (Term reactant : reaction.getReactants()) {
			if (reactant.state.isFluid()) {
				fluidStacks.add(IndustrialFluids.getCanonicalFluidStack(reactant, form));
			}
		}
		return fluidStacks;
	}
	
	public void registerReaction(Reaction reaction) {
		registerReaction(Forms.DUST, reaction);
		if (reaction.hasSolidReactants()) {
			registerReaction(Forms.DUST_TINY, reaction);
		}
	}

	private FluidStack getProductFluidStack(Form form, Reaction reaction, State state) {
		return IndustrialFluids.getCanonicalFluidStack(reaction.getProduct(state), state, form);
	}
	
	private void registerReaction(Form form, Reaction reaction) {
		IngredientList solidReactants = this.getSolidReactants(form, reaction);
		FluidStack fluidReactantA, fluidReactantB = null;
		if (solidReactants.size() > 0) 
			fluidReactantA = this.mixFluidReactants(form, reaction);
		else {
			List<FluidStack> fluidReactants = this.getReactantFluidStacks(form, reaction);
			fluidReactantA = fluidReactants.get(0);
			if (fluidReactants.size() > 1)
				fluidReactantB = fluidReactants.get(1);
			if (fluidReactants.size() > 2)
				return;
		}
		ItemStack solidProduct = this.getProductItemStack(form, reaction);
		FluidStack liquidProduct = this.getProductFluidStack(form, reaction, State.LIQUID);
		FluidStack gasProduct = this.getProductFluidStack(form, reaction, State.GAS);
		IngredientList catalyst = this.getCatalysts(reaction);
		delegate.registerMixingRecipe(solidReactants, fluidReactantA, fluidReactantB, 
				solidProduct, liquidProduct, gasProduct, 
				reaction.getCanonicalCondition(), catalyst);
	}
	
	private IngredientList getCatalysts(Reaction reaction) {
		return new IngredientList(reaction.getCatalysts().toArray(new IndustrialMaterial[0]));
	}

	private FluidStack mixFluidReactants(Form form, Reaction reaction) {
		List<Term> reactants = reaction.getReactants();
		List<Term> fluids = Lists.newArrayListWithCapacity(reactants.size());
		Mixture mixture = new SimpleMixture();
		for (Term reactant : reactants) {
			if (reactant.state != State.SOLID && !(reaction.isAqueous() && reactant.chemical == Compounds.H2O)) {
				fluids.add(reactant);
				mixture.mix(reactant.chemical, reactant.stoichiometry);
			}
		}
		if (fluids.size() == 0) {
			return null;
		} else if (fluids.size() == 1) {
			return IndustrialFluids.getCanonicalFluidStack(fluids.get(0), form);
		} else if (fluids.size() == 2) {
			boolean alreadyRegistered = FluidRegistry.isFluidRegistered(mixture.getOreDictKey());
			FluidStack inputA = IndustrialFluids.getCanonicalFluidStack(fluids.get(0), Forms.DUST_TINY);
			FluidStack inputB = IndustrialFluids.getCanonicalFluidStack(fluids.get(1), Forms.DUST_TINY);
			FluidStack mixtureStack = IndustrialFluids.getCanonicalFluidStack(mixture, inputA.amount + inputB.amount);
			if (!alreadyRegistered) {
				delegate.registerMixingRecipe(new IngredientList(), inputA, inputB, null, mixtureStack, 
						null, Condition.STP, null);
			}
			return mixtureStack;
		} else {
			throw new IllegalArgumentException("Cannot mix more than two fluids: " + fluids);
		}
	}
}
