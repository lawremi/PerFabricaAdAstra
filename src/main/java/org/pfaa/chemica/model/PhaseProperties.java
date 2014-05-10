package org.pfaa.chemica.model;

import java.awt.Color;

public class PhaseProperties {
	public final Color color;
	public final double density;
	public final Hazard hazard;
	
	public PhaseProperties(Color color, double density, Hazard hazard) {
		this.color = color;
		this.density = density;
		this.hazard = hazard;
	}
	
	public PhaseProperties(Color color, double density) {
		this(color, density, new Hazard());
	}
	
	public PhaseProperties(Color color) {
		this(color, Double.NaN);
	}
}
