package org.pfaa.chemica.model;

public class Fusion {
	private double temperature;
	private double enthalpy;
	
	public Fusion(double temperature, double enthalpy) {
		this.temperature = temperature;
		this.enthalpy = enthalpy;
	}

	public double getTemperature() {
		return temperature;
	}

	public double getEnthalpy() {
		return enthalpy;
	}
}
