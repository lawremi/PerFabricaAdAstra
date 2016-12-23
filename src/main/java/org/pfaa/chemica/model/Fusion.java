package org.pfaa.chemica.model;

public class Fusion {
	private int temperature;
	
	public Fusion(int temperature) {
		this.temperature = temperature;
	}

	public int getTemperature() {
		return temperature;
	}
	
	public Condition getCondition() {
		return new Condition(this.temperature);
	}
	
	public Condition getSolidCondition() {
		return new Condition(this.temperature-1);
	}
}
