package org.pfaa.chemica.model;

public class Fusion implements Transition {
	private int temperature;
	
	public Fusion(int temperature) {
		this.temperature = temperature;
	}

	public int getTemperature() {
		return temperature;
	}
}
