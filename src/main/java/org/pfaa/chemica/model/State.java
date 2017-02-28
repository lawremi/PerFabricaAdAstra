package org.pfaa.chemica.model;

import org.pfaa.chemica.processing.MaterialStoich;

public enum State {
	SOLID, LIQUID, GAS, AQUEOUS;
	
	public boolean isFluid() {
		return this == LIQUID || this == GAS || this == AQUEOUS;
	}

	public float getVolumeFactor() {
		if (this == State.GAS) { 
			return 100;
		} else if (this == State.AQUEOUS) {
			return (1 - Constants.STANDARD_SOLUTE_WEIGHT) / Constants.STANDARD_SOLUTE_WEIGHT;
		}
		return 1;
	}
	
	public State ofMatter() {
		return this == State.AQUEOUS ? State.LIQUID : this;
	}
	
	public <T extends IndustrialMaterial> MaterialState<T> of(T material) {
		return MaterialState.of(this, material);
	}
	
	public <T extends IndustrialMaterial> MaterialStoich<T> of(float stoich, T material) {
		return MaterialStoich.of(stoich, this, material);
	}
}
