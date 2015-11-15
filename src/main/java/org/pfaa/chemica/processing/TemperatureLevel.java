package org.pfaa.chemica.processing;

public enum TemperatureLevel {
	LOW(800), MEDIUM(1200), HIGH(1600), VERY_HIGH(2000);
	
	private int referenceTemperature;
	
	private TemperatureLevel(int referenceTemperature) {
		this.referenceTemperature = referenceTemperature;
	}
	
	public int getReferenceTemperature() {
		return this.referenceTemperature;
	}
}