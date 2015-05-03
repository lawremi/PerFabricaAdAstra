package org.pfaa.chemica.model;

public class ChemicalConditionProperties extends ConditionProperties {

	public final double heatCapacity;
	public final double enthalpy;
	public final double entropy;
	
	public ChemicalConditionProperties(ChemicalStateProperties properties, Condition condition) {
		super(properties, condition);
		this.heatCapacity = properties.thermo.getHeatCapacity(condition.temperature);
		this.enthalpy = properties.thermo.getEnthalpy(condition.temperature);
		this.entropy = properties.thermo.getEntropy(condition.temperature);
	}

}
