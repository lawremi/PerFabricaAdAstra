package org.pfaa.chemica.model;

import java.awt.Color;

public interface PhaseProperties {
	// TODO: viscosity? (supported by Forge fluids)
	
	public abstract Color getColor();

	public abstract double getDensity();

	public abstract Hazard getHazard();

	public abstract Phase getPhase();
}