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
		if (antoineCoefficients != null) {
			return antoineCoefficients.getBoilingTemperature(1.0);
		}
		return temperature;
	}

	public AntoineCoefficients getAntoineCoefficients() {
		return antoineCoefficients;
	}
	
	/* We can use the vapor pressure to calculate the evaporation rate (Langmuir's):
	 * (mass loss rate)/(unit area) = 
	 *   (vapor pressure - ambient partial pressure)*sqrt((molecular weight)/(2*pi*R*T))
	 */
	public static class AntoineCoefficients {
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

}
