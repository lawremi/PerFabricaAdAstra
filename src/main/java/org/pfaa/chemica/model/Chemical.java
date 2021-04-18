package org.pfaa.chemica.model;

import java.util.List;
import java.util.stream.DoubleStream;

import org.pfaa.chemica.model.Equation.Term;
import org.pfaa.chemica.processing.Reaction;
import org.pfaa.chemica.registration.Reactions;

/* A pure substance, including elements and compounds with fixed stoichiometry */
public interface Chemical extends IndustrialMaterial {
	public Formula getFormula();
	
	float MIN_SOLUBILITY = 0.001F; // mol/L, assuming 1 mB water = 1 L water as solvent

	default double getSolubility(Condition condition) {
		Reaction dissociation = Reactions.dissociate(this);
		double ksp = dissociation.getEquilibriumConstant(condition);
		List<Term> products = dissociation.getProducts();
		DoubleStream stoich = products.stream().mapToDouble((t) -> t.stoich);
		return Math.pow(ksp / stoich.reduce((a,b) -> a*b).getAsDouble(), 1 / stoich.sum());
	}
	default double getSolubility() {
		return this.getSolubility(Condition.STP);
	}
	default boolean isSoluble(Condition condition) {
		return this.getSolubility(condition) > MIN_SOLUBILITY;
	}
}
