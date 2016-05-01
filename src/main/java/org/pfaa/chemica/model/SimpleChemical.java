package org.pfaa.chemica.model;

import java.awt.Color;

import org.pfaa.chemica.model.ChemicalStateProperties.Aqueous;
import org.pfaa.chemica.model.ChemicalStateProperties.Gas;
import org.pfaa.chemica.model.ChemicalStateProperties.Liquid;
import org.pfaa.chemica.model.ChemicalStateProperties.Solid;


public class SimpleChemical implements Chemical {

	private Formula formula;
	private String oreDictKey;
	private Fusion fusion;
	private Vaporization vaporization;
	
	private ChemicalStateProperties solid;
	private ChemicalStateProperties liquid;
	private ChemicalStateProperties gas;
	private ChemicalStateProperties aqueous;
	
	public SimpleChemical(Formula formula, String oreDictKey, Solid solid) {
		this(formula, oreDictKey, solid, null, null, null, null, null);
	}
	public SimpleChemical(Formula formula, Aqueous aqueous) {
		this(formula, null, null, null, null, null, null, aqueous);
	}
	public SimpleChemical(Formula formula, String oreDictKey, Solid solid, Fusion fusion, 
			Liquid liquid,	Vaporization vaporization, Gas gas) {
		this(formula, oreDictKey, solid, fusion, liquid, vaporization, gas, null);
	}
	
	public SimpleChemical(Formula formula, String oreDictKey, Solid solid, Fusion fusion, 
			Liquid liquid,	Vaporization vaporization, Gas gas, Aqueous aqueous) 
	{
		this.formula = formula;
		this.oreDictKey = oreDictKey == null ? formula.toString() : oreDictKey;
		this.solid = solid;
		this.fusion = fusion;
		this.liquid = liquid;
		this.vaporization = vaporization;
		this.gas = gas == null ? null : new Gas(gas, formula.getMolarMass());
		this.aqueous = aqueous == null ? this.inferAqueous() : aqueous;
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

	private State getStateForCondition(Condition condition) {
		if (this.vaporization != null && condition.temperature >= this.vaporization.getTemperature(condition.pressure)) {
			return State.GAS;
		} else if (this.fusion != null && condition.temperature >= this.fusion.getTemperature()) {
			return State.LIQUID;
		} else {
			return State.SOLID;
		}
	}
	
	@Override
	public ChemicalConditionProperties getProperties(Condition condition) {
		State state = this.getStateForCondition(condition);
		return this.getProperties(condition, state);
	}
	
	@Override
	public ChemicalConditionProperties getProperties(Condition condition, State state) {
		return new ChemicalConditionProperties(this.getStateProperties(state), condition);
	}

	private ChemicalStateProperties getStateProperties(State state) {
		switch(state) {
		case SOLID:
			return solid;
		case LIQUID:
			return liquid;
		case GAS:
			return gas;
		case AQUEOUS:
			return aqueous;
		default:
			throw new IllegalArgumentException("Unknown state: " + state);
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
	
	private Aqueous inferAqueous() {
		Ion cation = this.getFormula().getCation();
		Ion anion = this.getFormula().getAnion();
		boolean simpleSalt = cation != null && anion != null && this.getFormula().getParts().size() == 2;
		if (!simpleSalt) {
			return null;
		}
		ChemicalConditionProperties cationProps = cation.getProperties(Condition.STP);
		ChemicalConditionProperties anionProps = anion.getProperties(Condition.STP);
		Color color = cationProps.color;
		if (color == null) 
			color = anionProps.color;
		double enthalpy = cationProps.enthalpy + anionProps.enthalpy;
		double entropy = cationProps.entropy + anionProps.entropy;
		Thermo thermo = new Thermo(enthalpy, entropy);
		Hazard hazard = this.getStateProperties(State.SOLID).getHazard();
		return new Aqueous(color, thermo, hazard);
	}
}
