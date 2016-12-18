package org.pfaa.chemica.model;

import org.pfaa.chemica.processing.MaterialStoich;

public class MaterialState<T extends IndustrialMaterial> {
	public final T material;
	public final State state;
	
	protected MaterialState(T material, State state) {
		super();
		this.material = material;
		this.state = state;
	}

	public MaterialStoich<T> times(float stoich) {
		return MaterialStoich.of(stoich, this);
	}
	
	public static <T extends IndustrialMaterial> MaterialState<T> of(State state, T material) {
		return new MaterialState<T>(material, state);
	}
}
