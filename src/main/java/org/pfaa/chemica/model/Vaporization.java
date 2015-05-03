package org.pfaa.chemica.model;

public class Vaporization {
	private double temperature;
	private AntoineCoefficients antoineCoefficients;
	
	public Vaporization(AntoineCoefficients antoineCoefficients) {
		super();
		this.antoineCoefficients = antoineCoefficients;
	}

	public Vaporization(double a, double b, double c) {
		this(new AntoineCoefficients(a, b, c));
	}
	
	public Vaporization(double temperature) {
		super();
		this.temperature = temperature;
	}
	
	public double getTemperature() {
		return this.getTemperature(Constants.STANDARD_PRESSURE);
	}
	
	public double getTemperature(double pressure) {
		if (antoineCoefficients != null) {
			return antoineCoefficients.getBoilingTemperature(pressure);
		}
		return temperature;
	}

	public AntoineCoefficients getAntoineCoefficients() {
		return antoineCoefficients;
	}
	
	public static class AntoineCoefficients {
		// For coefficients given in celcius, I think we can subtract 273 from 'c'
		private double a, b, c; // units: temperature=K, pressure=bar (need to convert to kPa (=bar/100))
		private static int KPA_PER_BAR = 100;
		public AntoineCoefficients(double a, double b, double c) {
			this.a = a;
			this.b = b;
			this.c = c;
		}
		public double getVaporPressure(double temperature) {
			return Math.pow(a - b / (c + temperature), 10) * KPA_PER_BAR;
		}
		public double getBoilingTemperature(double pressure) {
			return b / (a - Math.log10(pressure / KPA_PER_BAR)) - c;
		}
		// Langmuir's equation
		public double getEvaporationRate(double temperature, double pressure, double molecularWeight) {
			// rate is per unit area, which comes from the pressure units
			return (getVaporPressure(temperature) - pressure) * Math.sqrt(molecularWeight / (2*Math.PI*Constants.R*temperature));
		}
	}
}
