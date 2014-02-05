package org.pfaa.chemica.model;

import org.pfaa.chemica.model.ChemicalPhaseProperties.Gas;

public class SimpleChemical implements Chemical {

	private Formula formula;
	private String oreDictKey;
	private Fusion fusion;
	private Vaporization vaporization;
	
	private ChemicalPhaseProperties solid;
	private ChemicalPhaseProperties liquid;
	private ChemicalPhaseProperties gas;
	
	public SimpleChemical(Formula formula, String oreDictKey, ChemicalPhaseProperties solid, Fusion fusion, 
			ChemicalPhaseProperties liquid,	Vaporization vaporization, ChemicalPhaseProperties gas) 
	{
		this.formula = formula;
		this.oreDictKey = oreDictKey == null ? formula.toString() : oreDictKey;;
		this.solid = solid;
		this.fusion = fusion;
		this.liquid = liquid;
		this.vaporization = vaporization;
		this.gas = determineGasDensity(gas);
	}
	
	private ChemicalPhaseProperties determineGasDensity(ChemicalPhaseProperties gas) {
		double temperature = Math.max(vaporization.getTemperature(), Constants.STANDARD_TEMPERATURE);
		double density = Gas.getDensity(formula.getMolarMass(), temperature, Constants.STANDARD_PRESSURE);
		return new ChemicalPhaseProperties(gas.color, density, gas.hazard, gas.enthalpy, gas.entropy);
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

	@Override
	public ChemicalPhaseProperties getProperties(IndustrialMaterial.Phase phase) {
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

}
