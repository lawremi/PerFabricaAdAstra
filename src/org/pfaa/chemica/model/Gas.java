package org.pfaa.chemica.model;

import java.awt.Color;

public class Gas extends AbstractPhaseProperties {

	public Gas(Color color, double density, double enthalpy, double entropy, Hazard hazard) {
		super(color, density, enthalpy, entropy, hazard);
	}

	public Gas(Color color, double density, double enthalpy, double entropy) {
		super(color, density, enthalpy, entropy, new Hazard());
	}

	@Override
	public Phase getPhase() {
		return Phase.GAS;
	}

}
