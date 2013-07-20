package org.pfaa.chemica.model;

import java.awt.Color;

public class Solid extends AbstractPhaseProperties {
	public Solid(Color color, double density, double enthalpy, double entropy) {
		this(color, density, enthalpy, entropy, new Hazard());
	}
	public Solid(Color color, double density, double enthalpy, double entropy, Hazard hazard) {
		super(color, density, enthalpy, entropy, hazard);
	}
	@Override
	public Phase getPhase() {
		return Phase.SOLID;
	}
}