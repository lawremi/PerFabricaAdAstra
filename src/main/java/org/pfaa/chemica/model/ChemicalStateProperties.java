package org.pfaa.chemica.model;

import java.awt.Color;

public class ChemicalStateProperties extends StateProperties {
	public final Thermo thermo;
	
	public ChemicalStateProperties(State state, Color color, double density, Thermo thermo, 
			Hazard hazard, boolean opaque) {
		super(state, color, density, hazard, opaque);
		this.thermo = thermo;
	}
	
	public static class Solid extends ChemicalStateProperties {
		public Solid(Color color, double density, Thermo thermo, Hazard hazard)
		{
			super(State.SOLID, color, density, thermo, hazard, true);
		}
		public Solid(Color color, double density, Thermo thermo)
		{
			this(color, density, thermo, new Hazard());
		}
		public Solid(Color color, double density)
		{
			this(color, density, new Thermo());
		}
		public Solid(double density, Thermo thermo, Hazard hazard)
		{
			this(Color.WHITE, density, thermo, hazard);
		}
		public Solid(double density, Thermo thermo)
		{
			this(density, thermo, new Hazard());
		}
		public Solid(Thermo thermo)
		{
			this(Double.NaN, thermo);
		}
		public Solid() {
			this(new Thermo());
		}
	}
	
	public static class Liquid extends ChemicalStateProperties {
		private final Yaws yaws;
		public Liquid(Color color, double density, Thermo thermo, Hazard hazard, Yaws yaws)
		{
			super(State.LIQUID, color, density, thermo, hazard, false);
			this.yaws = yaws;
		}
		public Liquid(double density, Thermo thermo, Hazard hazard, Yaws yaws)
		{
			this(COLORLESS, density, thermo, hazard, yaws);
		}
		public Liquid(double density, Thermo thermo)
		{
			this(density, thermo, new Hazard(), null);
		}
		public Liquid(Thermo thermo)
		{
			this(Double.NaN, thermo);
		}
		public Liquid() {
			this(new Thermo());
		}
		
		@Override
		public double getViscosity(double temperature) {
			return this.yaws == null ? super.getViscosity(temperature) : this.yaws.getViscosity(temperature);
		}
		
		// We can also get gas viscosities from Yaws (but they are in micropoise, not centipoise)
		public static class Yaws {
			private double A;
			private double B;
			private double C;
			private double D;

			public Yaws(double A, double B, double C, double D) {
				this.A = A;
				this.B = B;
				this.C = C;
				this.D = D;
			}
			
			public double getViscosity(double temperature) {
				double exp = this.A + this.B / temperature + this.C * temperature + this.D * Math.pow(temperature, 2);
				return Math.pow(10, exp);
			}
		}
		
	}
	
	public static class Gas extends ChemicalStateProperties {
		private final double molarMass;
		private final Sutherland sutherland;

		public Gas(Gas gas, double molarMass) {
			this(gas.getColor(), gas.thermo, gas.getHazard(), gas.sutherland, molarMass);
		}
		
		public Gas(Color color, Thermo thermo, Hazard hazard, Sutherland sutherland, double molarMass)
		{
			super(State.GAS, color, Double.NaN, thermo, hazard, false);
			this.molarMass = molarMass;
			this.sutherland = sutherland;
		}
		public Gas(Color color, Thermo thermo, Hazard hazard, Sutherland sutherland)
		{
			this(color, thermo, hazard, sutherland, Double.NaN);
		}
		public Gas(Thermo thermo, Hazard hazard, Sutherland sutherland)
		{
			this(COLORLESS, thermo, hazard, sutherland);
		}
		public Gas(Thermo thermo)
		{
			this(thermo, new Hazard(), null);
		}
		public Gas()
		{
			this(new Thermo());
		}
		
		@Override
		public double getDensity(Condition condition) {
			if (Double.isNaN(this.molarMass)) {
				return super.getDensity(condition);
			} else {
				return getDensity(condition, this.molarMass);
			}
		}
		
		private static double getDensity(Condition condition, double molarMass) {
			return (molarMass * condition.pressure) / (Constants.R * condition.temperature) / 1E6;
		}

		@Override
		public double getViscosity(double temperature) {
			return this.sutherland == null ? super.getViscosity(temperature) : this.sutherland.getViscosity(temperature);
		}
		
		public static class Sutherland {
			private double refViscosity;
			private double refTemperature;
			private double constant; // approximately 1.47 * boiling point

			public Sutherland(double refViscosity, double refTemperature, double constant) {
				this.refViscosity = refViscosity / 1000;
				this.refTemperature = refTemperature;
				this.constant = constant;
			}
			
			public double getViscosity(double temperature) {
				return refViscosity * 
						((refTemperature + constant) / (temperature + constant) * 
						Math.pow(temperature / refTemperature, 1.5)); 
			}
		}

	}
}