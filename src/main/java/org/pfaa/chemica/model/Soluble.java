package org.pfaa.chemica.model;

import java.util.List;
import java.util.stream.Stream;

import org.pfaa.chemica.model.Equation.Term;

// TODO: merge into Chemical
public interface Soluble extends IndustrialMaterial {
	float MIN_SOLUBILITY = 0.001F;
	
	public Reaction getDissociation();
	default double getSolubility(Condition condition) {
		Reaction dissociation = this.getDissociation();
		double ksp = dissociation.getEquilibriumConstant(condition);
		List<Term> products = dissociation.getProducts();
		Stream<Float> stoich = products.stream().map((t) -> t.stoich);
		return Math.pow(ksp / stoich.reduce((a,b) -> a*b).get(), 1 / stoich.reduce((a,b)->a+b).get());
	}
	default double getSolubility() {
		return this.getSolubility(Condition.STP);
	}
	default boolean isSoluble(Condition condition) {
		return this.getSolubility(condition) > MIN_SOLUBILITY;
	}
}
