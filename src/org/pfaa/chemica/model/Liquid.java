package org.pfaa.chemica.model;

import java.awt.Color;

public class Liquid extends AbstractPhaseProperties {

	public Liquid(Color color, double density, double enthalpy, double entropy, Hazard hazard) {
		super(color, density, enthalpy, entropy, hazard);
	}
	public Liquid(Color color, double density, double enthalpy, double entropy) {
		super(color, density, enthalpy, entropy, new Hazard());
	}
	@Override
	public Phase getPhase() {
		return Phase.LIQUID;
	}
}
