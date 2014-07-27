package org.pfaa.chemica.model;

public class Condition {
	public double temperature;
	public double pressure;
	
	public Condition(double temperature, double pressure) {
		super();
		this.temperature = temperature;
		this.pressure = pressure;
	}
	
	public static Condition STP = new Condition(Constants.STANDARD_TEMPERATURE, Constants.STANDARD_PRESSURE);
}
