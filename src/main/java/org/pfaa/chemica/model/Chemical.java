package org.pfaa.chemica.model;

import java.util.List;
import java.util.stream.Stream;

import org.pfaa.chemica.model.Equation.Term;

/* A pure substance, including elements and compounds with fixed stoichiometry */
public interface Chemical extends IndustrialMaterial {
	float MIN_SOLUBILITY = 0.001F;
	public Formula getFormula();
	
	default Reaction getDissociation() {
		Formula.Part cation = this.getFormula().getFirstPart();
		Formula.Part anion = this.getFormula().getLastPart();
		boolean simpleSalt = cation.ion != null && anion.ion != null && this.getFormula().getParts().size() == 2;
		if (!simpleSalt) {
			return null;
		}
		return Reaction.inWaterOf(1, this, State.SOLID).
				yields(cation.stoichiometry, cation.ion).
				and(anion.stoichiometry, anion.ion);
	}
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
		return this.getSolubility(condition) > Chemical.MIN_SOLUBILITY;
	}
}
