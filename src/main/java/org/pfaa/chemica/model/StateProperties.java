package org.pfaa.chemica.model;

import java.awt.Color;

import net.minecraftforge.fluids.FluidRegistry;

import org.pfaa.chemica.util.ColorUtils;

public class StateProperties {
	public static final Color COLORLESS = new Color(230, 230, 245, 0);
	
	private final State phase;
	private final Color color; // base color, modified by thermal radiation
	private final Hazard hazard;
	private final double density; // g/mL, not relevant for gases
	private final boolean opaque;

	public final Thermo thermo;
	
	public StateProperties(State phase, Color color, double density, Hazard hazard, boolean opaque) {
		this(phase, color, density, new Thermo(), hazard, opaque);
	}
	
	public StateProperties(State phase, Color color, double density, Thermo thermo, Hazard hazard, boolean opaque) {
		this.phase = phase;
		this.color = color;
		this.density = density;
		this.thermo = thermo;
		this.hazard = hazard;
		this.opaque = opaque;
	}
	
	public State getPhase() {
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
		return this.phase == State.SOLID ? Double.POSITIVE_INFINITY : Double.NaN;
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

	public boolean getOpaque() {
		return this.opaque;
	}
	
	public ConditionProperties at(Condition condition) {
		return this.at(condition, null);
	}
	
	public ConditionProperties at(Condition condition, Thermo adjacent) {
		return new ConditionProperties(
				this.getPhase(),
				this.getColor(condition.temperature),
				this.getDensity(condition),
				this.thermo.at(condition, adjacent),
				this.getHazard(),
				this.getViscosity(condition.temperature),
				this.getLuminosity(condition.temperature),
				this.getOpaque());
	}
}
