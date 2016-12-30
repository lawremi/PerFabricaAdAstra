package org.pfaa.chemica.model;

import java.awt.Color;

public class ConditionProperties {
	public final State state;
	public final Color color;
	public final double density;
	public final ConditionThermo thermo;
	public final Hazard hazard;
	public final double viscosity;
	public final int luminosity;
	public final boolean opaque; // FIXME: can't we just use the color alpha for this?
	
	public ConditionProperties(State phase, Color color, double density, ConditionThermo thermo,
			Hazard hazard, double viscosity, int luminosity, boolean opaque) {
		this.state = phase;
		this.color = color;
		this.density = density;
		this.thermo = thermo;
		this.hazard = hazard;
		this.viscosity = viscosity;
		this.luminosity = luminosity;
		this.opaque = opaque;
	}
	
	public ConditionProperties(State phase, Color color, double density,
			Hazard hazard, double viscosity, int luminosity, boolean opaque) {
		this(phase, color, density, new ConditionThermo(), hazard, viscosity, luminosity, opaque);
	}
	
	public ConditionProperties(State phase, Color color, double density, Hazard hazard) {
		this(phase, color, density, hazard, phase == State.SOLID ? Double.POSITIVE_INFINITY : Double.NaN, 0, false);
	}
	
	private ConditionProperties(ConditionProperties props, Color overrideColor) {
		this(props.state, overrideColor, props.density, props.hazard, props.viscosity, props.luminosity, props.opaque);
	}
	
	protected ConditionProperties(ConditionProperties props) {
		this(props.state, props.color, props.density, props.hazard, props.viscosity, props.luminosity, props.opaque);
	}
	
	public ConditionProperties recolor(Color overrideColor) {
		return new ConditionProperties(this, overrideColor);
	}
}
