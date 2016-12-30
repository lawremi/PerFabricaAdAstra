package org.pfaa.chemica.model;

public class ConditionThermo {
	public final double heatCapacity;
	public final double enthalpy;
	public final double entropy;

	public ConditionThermo(double heatCapacity, double enthalpy, double entropy) {
		super();
		this.heatCapacity = heatCapacity;
		this.enthalpy = enthalpy;
		this.entropy = entropy;
	}

	public ConditionThermo() {
		this(Double.NaN, Double.NaN, Double.NaN);
	}
}
