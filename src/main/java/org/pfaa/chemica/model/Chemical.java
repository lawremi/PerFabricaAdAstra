package org.pfaa.chemica.model;


/* A pure substance, including elements and compounds with fixed stoichiometry */
public interface Chemical extends IndustrialMaterial, Vaporizable, Fusible, Soluble {
	public ChemicalConditionProperties getProperties(Condition condition);
	public ChemicalConditionProperties getProperties(Condition condition, State state);
	public Formula getFormula();
	
	default double getEnthalpyOfFusion() {
		Condition cond = this.getFusion().getCondition();
		ChemicalConditionProperties liquid = this.getProperties(cond, State.LIQUID);
		ChemicalConditionProperties solid = this.getProperties(cond, State.SOLID);
		if (solid == null || liquid == null) {
			return Double.NaN;
		}
		return (liquid.entropy - solid.entropy) * cond.temperature;
	}
	
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
}
