package org.pfaa.chemica.processing;

import org.pfaa.chemica.model.State;

/*
 * A phase is a part of a system that is physically separate from the others.
 * This typically but not necessarily happens by the phase being of a different physical state. 
 */

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
		case AQUEOUS: return Phase.AQUEOUS;
		}
		throw new IllegalArgumentException("Unknown state of matter");
	}
}
