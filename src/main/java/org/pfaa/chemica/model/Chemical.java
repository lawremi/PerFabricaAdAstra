package org.pfaa.chemica.model;


/* A pure substance, including elements and compounds with fixed stoichiometry */
public interface Chemical extends IndustrialMaterial, Vaporizable, Fusible, Soluble {
	public ChemicalConditionProperties getProperties(Condition condition);
	public ChemicalConditionProperties getProperties(Condition condition, State state);
	public Formula getFormula();
	
	default double getEnthalpyOfFusion() {
		Condition cond = this.getFusion().getLiquidCondition();
		ChemicalConditionProperties liquid = this.getProperties(cond, State.LIQUID);
		ChemicalConditionProperties solid = this.getProperties(cond, State.SOLID);
		if (solid == null || liquid == null) {
			return Double.NaN;
		}
		return (liquid.entropy - solid.entropy) * cond.temperature;
	}
	
	default double getEnthalpyOfVaporization() {
		Condition cond = this.getVaporization().getGasCondition();
		ChemicalConditionProperties liquid = this.getProperties(cond, State.LIQUID);
		ChemicalConditionProperties gas = this.getProperties(cond, State.GAS);
		if (gas == null || liquid == null) {
			return Double.NaN;
		}
		return (gas.entropy - liquid.entropy) * cond.temperature;
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
	
	default double getEnthalpyChange(Condition to) {
		return this.getEnthalpyChange(Condition.STP, to);
	}
	
	default double getEnthalpyChange(Condition from, Condition to) {
		// TODO: if the enthalpy is NaN, look at adjacent state and add heat of transition, which we know if we know the entropy
		ChemicalConditionProperties fromProps = this.getProperties(from);
		ChemicalConditionProperties toProps = this.getProperties(to);
		return toProps.enthalpy - fromProps.enthalpy;
	}
}
