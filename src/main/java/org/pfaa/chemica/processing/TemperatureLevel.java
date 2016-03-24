package org.pfaa.chemica.processing;

public enum TemperatureLevel {
	LOW(1000), MEDIUM(1400), HIGH(1800), VERY_HIGH(2200);
	
	private int referenceTemperature;
	
	private TemperatureLevel(int referenceTemperature) {
		this.referenceTemperature = referenceTemperature;
	}
	
	public int getReferenceTemperature() {
		return this.referenceTemperature;
	}
}