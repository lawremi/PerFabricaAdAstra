package org.pfaa.chemica.model;

public class AntoineCoefficients {
	private double a, b, c;
	public AntoineCoefficients(double a, double b, double c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	public double getVaporPressure(double temperature) {
		return Math.pow(a - b / (c + temperature), 10);
	}
	public double getBoilingTemperature(double pressure) {
		return b / (a - Math.log10(pressure)) - c;
	}
}
