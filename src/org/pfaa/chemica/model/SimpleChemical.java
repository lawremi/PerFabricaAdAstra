package org.pfaa.chemica.model;


public class SimpleChemical implements Chemical {

	private Formula formula;
	private String oreDictKey;
	private Fusion fusion;
	private Vaporization vaporization;
	
	private ChemicalPhaseProperties solid;
	private ChemicalPhaseProperties liquid;
	private ChemicalPhaseProperties gas;
	
	public SimpleChemical(Formula formula, String oreDictKey, ChemicalPhaseProperties solid) {
		this(formula, oreDictKey, solid, null, null, null, null);
	}
	public SimpleChemical(Formula formula, String oreDictKey, ChemicalPhaseProperties solid, Fusion fusion, 
			ChemicalPhaseProperties liquid,	Vaporization vaporization, ChemicalPhaseProperties gas) 
	{
		this.formula = formula;
		this.oreDictKey = oreDictKey == null ? formula.toString() : oreDictKey;
		this.solid = solid;
		this.fusion = fusion;
		this.liquid = liquid;
		this.vaporization = vaporization;
		this.gas = gas;
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
