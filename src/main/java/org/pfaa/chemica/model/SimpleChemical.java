package org.pfaa.chemica.model;

import org.pfaa.chemica.model.ChemicalPhaseProperties.Gas;
import org.pfaa.chemica.model.ChemicalPhaseProperties.Liquid;
import org.pfaa.chemica.model.ChemicalPhaseProperties.Solid;


public class SimpleChemical implements Chemical {

	private Formula formula;
	private String oreDictKey;
	private Fusion fusion;
	private Vaporization vaporization;
	
	private ChemicalPhaseProperties solid;
	private ChemicalPhaseProperties liquid;
	private ChemicalPhaseProperties gas;
	
	public SimpleChemical(Formula formula, String oreDictKey, Solid solid) {
		this(formula, oreDictKey, solid, null, null, null, null);
	}
	public SimpleChemical(Formula formula, String oreDictKey, Solid solid, Fusion fusion, 
			Liquid liquid,	Vaporization vaporization, Gas gas) 
	{
		this.formula = formula;
		this.oreDictKey = oreDictKey == null ? formula.toString() : oreDictKey;
		this.solid = solid;
		this.fusion = fusion;
		this.liquid = liquid;
		this.vaporization = vaporization;
		this.gas = gas == null ? null : new Gas(gas, formula.getMolarMass());
	}
	
	@Override
	public String getOreDictKey() {
		return oreDictKey;
	}

	@Override
	public Fusion getFusion() {
		return fusion;
	}

	@Override
	public Vaporization getVaporization() {
		return vaporization;
	}

	@Override
	public Formula getFormula() {
		return formula;
	}

	private Phase getPhaseForCondition(Condition condition) {
		if (this.vaporization != null && condition.temperature > this.vaporization.getTemperature(condition.pressure)) {
			return Phase.GAS;
		} else if (this.fusion != null && condition.temperature > this.fusion.getTemperature()) {
			return Phase.LIQUID;
		} else {
			return Phase.SOLID;
		}
	}
	
	@Override
	public ConditionProperties getProperties(Condition condition) {
		Phase phase = this.getPhaseForCondition(condition);
		return new ConditionProperties(this.getPhaseProperties(phase), condition);
	}

	private PhaseProperties getPhaseProperties(Phase phase) {
		switch(phase) {
		case SOLID:
			return solid;
		case LIQUID:
			return liquid;
		case GAS:
			return gas;
		default:
			throw new IllegalArgumentException("Unknown phase: " + phase);
		}
	}
	
	@Override
	public String name() {
		return formula.toString();
	}
	
	@Override
	public Mixture mix(IndustrialMaterial material, double weight) {
		return new SimpleMixture(this).mix(material, weight);
	}
}
