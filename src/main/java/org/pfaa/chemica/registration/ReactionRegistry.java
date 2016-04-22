package org.pfaa.chemica.registration;

import java.util.ArrayList;
import java.util.List;

import org.pfaa.chemica.fluid.IndustrialFluids;
import org.pfaa.chemica.item.IngredientStack;
import org.pfaa.chemica.item.MaterialStack;
import org.pfaa.chemica.model.Equation.Term;
import org.pfaa.chemica.model.Reaction;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;

import net.minecraft.item.ItemStack;
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
		ItemStack output = this.getSolidProductItemStack(form, reaction, scale);
		if (output == null) {
			return;
		}
		IngredientList inputs = this.getReactantMaterials(form, reaction);
		int temp = reaction.getSpontaneousTemperature();
		delegate.registerRoastingRecipe(inputs, output, temp);
	}
	
	private IngredientList getReactantMaterials(Form form, Reaction reaction) {
		List<Term> reactants = reaction.getReactants();
		List<IngredientStack> stacks = new ArrayList<IngredientStack>(reactants.size());
		for (Term reactant : reactants) {
			stacks.add(new MaterialStack(form, reactant.chemical, reactant.stoichiometry));
		}
		return new IngredientList(stacks);
	}
	
	private ItemStack getSolidProductItemStack(Form form, Reaction reaction, float scale) {
		for (Term product : reaction.getProducts()) {
			if (product.state == State.SOLID) {
				float amount = scale * product.stoichiometry;
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

	public void registerSynthesis(Reaction synthesis) {
		registerSynthesis(Forms.DUST, synthesis);
		registerSynthesis(Forms.DUST_TINY, synthesis);
	}
	
	public void registerSynthesis(Form form, Reaction synthesis) {
		Term a = synthesis.getReactants().get(0);
		Term b = synthesis.getReactants().get(1);
		Term product = synthesis.getProducts().get(0);
		
		if (a.state == State.SOLID && b.state != State.SOLID && product.state == State.SOLID) {
			IngredientList inputs = new IngredientList(new MaterialStack(form, a));
			FluidStack additive = IndustrialFluids.getCanonicalFluidStack(b, form);
			ItemStack output = new MaterialStack(form, product).getBestItemStack();
			delegate.registerAbsorptionRecipe(inputs, additive, output, synthesis.getSpontaneousTemperature());
		} else {
			throw new IllegalArgumentException("unhandled synthesis type");
		}
	}
}
