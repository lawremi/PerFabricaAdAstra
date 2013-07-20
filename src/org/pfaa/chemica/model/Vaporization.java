package org.pfaa.chemica.model;

public class Vaporization {
	private double temperature;
	private AntoineCoefficients antoineCoefficients;
	
	public Vaporization(double temperature, AntoineCoefficients antoineCoefficients) {
		super();
		this.temperature = temperature;
		this.antoineCoefficients = antoineCoefficients;
	}

	public double getTemperature() {
		return temperature;
	}

	public AntoineCoefficients getAntoineCoefficients() {
		return antoineCoefficients;
	}
}
