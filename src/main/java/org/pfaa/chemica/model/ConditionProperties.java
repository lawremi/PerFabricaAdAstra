package org.pfaa.chemica.model;

import java.awt.Color;

import org.pfaa.chemica.utils.ColorUtils;

import net.minecraftforge.fluids.FluidRegistry;

public class ConditionProperties {
	public final Phase phase;
	public final Color color;
	public final double density;
	public final Hazard hazard;
	public final double viscosity;
	public final int luminosity;
	
	public ConditionProperties(PhaseProperties properties, Condition condition) {
		this(properties.getPhase(), 
			 properties.getColor(condition.temperature), 
			 properties.getDensity(condition),
		     properties.getHazard(), 
		     properties.getViscosity(condition.temperature),
		     properties.getLuminosity(condition.temperature));
	}
	
	public ConditionProperties(Phase phase, Color color, double density,
			Hazard hazard, double viscosity, int luminosity) {
		super();
		this.phase = phase;
		this.color = color;
		this.hazard = hazard;
		this.density = density;
		this.viscosity = viscosity;
		this.luminosity = luminosity;
	}
	
	public ConditionProperties(Phase phase, Color color, double density, Hazard hazard) {
		this(phase, color, density, hazard, phase == Phase.SOLID ? Double.POSITIVE_INFINITY : Double.NaN, 0);
	}
}
