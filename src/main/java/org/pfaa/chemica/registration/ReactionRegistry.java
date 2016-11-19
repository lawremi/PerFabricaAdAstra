package org.pfaa.chemica.registration;

import java.util.List;

import org.pfaa.chemica.fluid.IndustrialFluids;
import org.pfaa.chemica.item.IngredientStack;
import org.pfaa.chemica.item.MaterialStack;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Equation.Term;
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
	
	public void registerThermalDecomposition(Reaction reaction) {
		this.registerThermalDecomposition(Forms.DUST, reaction);
		this.registerThermalDecomposition(Forms.DUST_TINY, reaction);
	}
	
	public void registerThermalDecomposition(Form form, Reaction reaction) {
		float scale = 1F / reaction.getReactants().get(0).stoichiometry;
		reaction = reaction.scale(scale);
		ItemStack output = this.getProductItemStack(form, reaction);
		if (output == null) {
			return;
		}
		FluidStack gas = this.getProductFluidStack(form, reaction);
		IngredientList inputs = this.getSolidReactants(form, reaction);
		int temp = reaction.getCanonicalCondition().temperature;
		delegate.registerRoastingRecipe(inputs, output, gas, temp);
	}
	
	private IngredientList getSolidReactants(Form form, Reaction reaction) {
		List<Term> reactants = reaction.getReactants();
		List<IngredientStack> stacks = Lists.newArrayListWithCapacity(reactants.size());
		for (Term reactant : reactants) {
			if (reactant.state == State.SOLID)
				stacks.add(new MaterialStack(form, reactant.chemical, (int)reactant.stoichiometry));
		}
		return new IngredientList(stacks);
	}
	
	private ItemStack getProductItemStack(Form form, Reaction reaction) {
		for (Term product : reaction.getProducts()) {
			if (product.state == State.SOLID) {
				float amount = product.stoichiometry;
				if (amount < 1) {
					if (form == Forms.DUST) {
						form = Forms.DUST_TINY;
						amount *= Forms.DUST_TINY.getNumberPerBlock() / Forms.DUST.getNumberPerBlock();
					} else continue;
				}
				return new MaterialStack(form, product.chemical, (int)amount).getBestItemStack();
			}
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
	
	private List<FluidStack> getProductFluidStacks(Form form, Reaction reaction) {
		List<FluidStack> fluidStacks = Lists.newArrayList();
		for (Term product : reaction.getProducts()) {
			if (product.state.isFluid()) {
				fluidStacks.add(IndustrialFluids.getCanonicalFluidStack(product, form));
			}
		}
		return fluidStacks;
	}
	
	private FluidStack getProductFluidStack(Form form, Reaction reaction) {
		List<FluidStack> fluidStacks = this.getProductFluidStacks(form, reaction);
		return fluidStacks.size() > 0 ? fluidStacks.get(0) : null;
	}
	
	public void registerReaction(Reaction reaction) {
		registerReaction(Forms.DUST, reaction);
		if (reaction.hasSolidReactants()) {
			registerReaction(Forms.DUST_TINY, reaction);
		}
	}

	private FluidStack mixFluidProducts(Reaction reaction, State state, Form form) {
		List<Term> products = getProductTerms(reaction, state);
		if (products.size() == 0) {
			return null;
		}
		if (products.size() == 1) {
			return IndustrialFluids.getCanonicalFluidStack(products.get(0), form);
		}
		SimpleMixture mixture = new SimpleMixture();
		for (Term product : products) {
			mixture.mix(product.chemical, product.stoichiometry);
		}
		return IndustrialFluids.getCanonicalFluidStack(mixture, 
				(int)(IndustrialFluids.getAmount(form) * mixture.getTotalWeight()));
	}
	
	private List<Term> getProductTerms(Reaction reaction, State state) {
		List<Term> terms = Lists.newArrayList();
		for (Term product : reaction.getProducts()) {
			if (product.state == state)
				terms.add(product);
		}
		return terms;
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
		FluidStack liquidProduct = this.mixFluidProducts(reaction, State.LIQUID, form);
		FluidStack gasProduct = this.mixFluidProducts(reaction, State.GAS, form);
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
