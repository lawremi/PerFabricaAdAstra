package org.pfaa.chemica.processing;

import org.pfaa.chemica.model.State;

public enum Phase {
	SOLID,
	LIQUID,
	VAPOR,
	
	FLUID,
	
	AQUEOUS,
	NONPOLAR;
	
	public static Phase fromState(State state) {
		switch (state) {
		case SOLID: return Phase.SOLID;
		case LIQUID: return Phase.LIQUID;
		case GAS: return Phase.VAPOR;
		}
		throw new IllegalArgumentException("Unknown state of matter");
	}
}
