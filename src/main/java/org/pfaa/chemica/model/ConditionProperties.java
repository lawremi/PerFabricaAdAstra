package org.pfaa.chemica.model;

import java.awt.Color;

public class ConditionProperties {
	public final Phase phase;
	public final Color color;
	public final double density;
	public final Hazard hazard;
	public final double viscosity;
	public final int luminosity;
	public final boolean opaque;
	
	public ConditionProperties(PhaseProperties properties, Condition condition) {
		this(properties.getPhase(), 
			 properties.getColor(condition.temperature), 
			 properties.getDensity(condition),
		     properties.getHazard(), 
		     properties.getViscosity(condition.temperature),
		     properties.getLuminosity(condition.temperature),
		     properties.getOpaque());
	}
	
	public ConditionProperties(Phase phase, Color color, double density,
			Hazard hazard, double viscosity, int luminosity, boolean opaque) {
		super();
		this.phase = phase;
		this.color = color;
		this.hazard = hazard;
		this.density = density;
		this.viscosity = viscosity;
		this.luminosity = luminosity;
		this.opaque = opaque;
	}
	
	public ConditionProperties(Phase phase, Color color, double density, Hazard hazard) {
		this(phase, color, density, hazard, phase == Phase.SOLID ? Double.POSITIVE_INFINITY : Double.NaN, 0, false);
	}
	
	private ConditionProperties(ConditionProperties props, Color overrideColor) {
		this(props.phase, overrideColor, props.density, props.hazard, props.viscosity, props.luminosity, props.opaque);
	}
	
	public ConditionProperties recolor(Color overrideColor) {
		return new ConditionProperties(this, overrideColor);
	}
}
