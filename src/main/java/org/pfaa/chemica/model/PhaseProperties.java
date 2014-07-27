package org.pfaa.chemica.model;

import java.awt.Color;

import org.pfaa.chemica.utils.ColorUtils;

import net.minecraftforge.fluids.FluidRegistry;

public class PhaseProperties {
	private final Phase phase;
	private final Color color; // base color, modified by thermal radiation
	private final Hazard hazard;
	private final double density; // g/mL, not relevant for gases
	
	public PhaseProperties(Phase phase, Color color, double density, Hazard hazard) {
		this.phase = phase;
		this.color = color;
		this.density = density;
		this.hazard = hazard;
	}

	public PhaseProperties(Phase phase, Color color, Hazard hazard) {
		this(phase, color, Double.NaN, hazard);
	}
	
	public PhaseProperties(Phase phase, Color color, double density) {
		this(phase, color, density, new Hazard());
	}
	
	public PhaseProperties(Phase phase, Color color) {
		this(phase, color, Double.NaN);
	}
	
	public Phase getPhase() {
		return phase;
	}
	
	public Color getColor() {
		return color;
	}

	public Hazard getHazard() {
		return hazard;
	}
	
	public Color getColor(double temperature) {
		return this.addThermalRadiation(this.color, temperature);
	}
	
	public double getDensity(Condition condition) {
		return this.density;
	}
	
	/* dynamic (cP (mPa*s)) */
	public double getViscosity(double temperature) {
		return this.phase == Phase.SOLID ? Double.POSITIVE_INFINITY : Double.NaN;
	}
	
	
	public static final int MIN_GLOWING_TEMPERATURE = 750;
	private static final int RED_TEMPERATURE = 950, YELLOW_TEMPERATURE = 1450, WHITE_TEMPERATURE = 1650;
	
	public int getLuminosity(double temperature) {
		if (temperature > MIN_GLOWING_TEMPERATURE) {
			double tempToLuminosity = FluidRegistry.LAVA.getLuminosity() / FluidRegistry.LAVA.getTemperature();
			return (int)(temperature * tempToLuminosity);
		} else {
			return 0;
		}
	}
	
	private Color addThermalRadiation(Color color, double temperature) {
		if (temperature > MIN_GLOWING_TEMPERATURE) {
			color = ColorUtils.blendColors(color, Color.RED, temperature / RED_TEMPERATURE);
			if (temperature > RED_TEMPERATURE) {
				color = ColorUtils.blendColors(color, Color.YELLOW, temperature / YELLOW_TEMPERATURE);
				if (temperature > YELLOW_TEMPERATURE) {
					color = ColorUtils.blendColors(color, Color.WHITE, temperature / WHITE_TEMPERATURE);
				}
			} 
		}
		return color;
	}
}
