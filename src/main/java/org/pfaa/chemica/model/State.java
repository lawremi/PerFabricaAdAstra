package org.pfaa.chemica.model;

public enum State {
	SOLID, LIQUID, GAS;
	
	public boolean isFluid() {
		return this == LIQUID || this == GAS;
	}
}
