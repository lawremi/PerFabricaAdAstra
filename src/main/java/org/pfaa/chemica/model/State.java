package org.pfaa.chemica.model;

import org.pfaa.chemica.processing.MaterialStoich;

public enum State {
	SOLID, LIQUID, GAS, AQUEOUS;
	
	public boolean isFluid() {
		return this == LIQUID || this == GAS;
	}

	public <T extends IndustrialMaterial> MaterialState<T> of(T material) {
		return MaterialState.of(this, material);
	}
	
	public <T extends IndustrialMaterial> MaterialStoich<T> of(float stoich, T material) {
		return MaterialStoich.of(stoich, this, material);
	}
}
