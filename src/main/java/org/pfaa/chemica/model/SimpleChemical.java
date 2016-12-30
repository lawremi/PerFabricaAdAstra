package org.pfaa.chemica.model;

import java.awt.Color;

import org.pfaa.chemica.model.ChemicalStateProperties.Aqueous;
import org.pfaa.chemica.model.ChemicalStateProperties.Gas;
import org.pfaa.chemica.model.ChemicalStateProperties.Liquid;
import org.pfaa.chemica.model.ChemicalStateProperties.Solid;
import org.pfaa.chemica.model.Compound.Compounds;


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
	
	public ChemicalStateProperties getStateProperties(State state) {
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
	
	private boolean isWaterSolubleSalt() {
		Reaction dissolution = this.getDissociation();
		if (dissolution == null) {
			return false;
		}
		int et = dissolution.getEquilibriumTemperature();
		return et > Compounds.H2O.getFusion().getTemperature() && et < Compounds.H2O.getVaporization().getTemperature();
	}
	
	private Aqueous inferAqueous() {
		if (!isWaterSolubleSalt()) {
			return null;
		}
		Ion cation = this.getFormula().getCation();
		Ion anion = this.getFormula().getAnion();
		ConditionProperties cationProps = cation.getProperties(Condition.STP);
		ConditionProperties anionProps = anion.getProperties(Condition.STP);
		Color color = cationProps.color;
		if (color == null) 
			color = anionProps.color;
		double enthalpy = cationProps.thermo.enthalpy + anionProps.thermo.enthalpy;
		double entropy = cationProps.thermo.entropy + anionProps.thermo.entropy;
		Thermo thermo = new Thermo(enthalpy, entropy);
		Hazard hazard = this.getStateProperties(State.SOLID).getHazard();
		return new Aqueous(color, thermo, hazard);
	}
}
