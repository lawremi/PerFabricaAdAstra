package org.pfaa.chemica.model;

import java.awt.Color;

public class ChemicalPhaseProperties extends PhaseProperties {
	public final Thermo thermo;
	
	public ChemicalPhaseProperties(Color color, double density, Hazard hazard, Thermo thermo) {
		super(color, density, hazard);
		this.thermo = thermo;
	}
	
	public static class Solid extends ChemicalPhaseProperties {
		public Solid(Color color, double density, Thermo thermo, Hazard hazard)
		{
			super(color, density, hazard, thermo);
		}
		public Solid(Color color, double density, Thermo thermo)
		{
			this(color, density, thermo, new Hazard());
		}
		public Solid(double density, Thermo thermo, Hazard hazard)
		{
			super(Color.WHITE, density, hazard, thermo);
		}
	}
	
	public static class Liquid extends ChemicalPhaseProperties {
		public Liquid(Color color, double density, Thermo thermo, Hazard hazard)
		{
			super(color, density, hazard, thermo);
		}
		public Liquid(Color color, double density, Thermo thermo)
		{
			this(color, density, thermo, new Hazard());
		}
		public Liquid(double density, Thermo thermo, Hazard hazard)
		{
			super(Color.BLUE, density, hazard, thermo);
		}
	}
	
	public static class Gas extends ChemicalPhaseProperties {
		public Gas(Color color, Thermo thermo, Hazard hazard)
		{
			super(color, Double.NaN, hazard, thermo);
		}
		public Gas(Thermo thermo, Hazard hazard)
		{
			super(Color.WHITE, Double.NaN, hazard, thermo);
		}
		public Gas(Thermo thermo)
		{
			this(thermo, new Hazard());
		}
		
		public static double getDensity(double molarMass, double temperature, double pressure) {
			return (molarMass * pressure) / (Constants.R * temperature);
		}
	}
}