package org.pfaa.chemica.processing;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.MaterialState;

public interface ConversionType<M extends IndustrialMaterial, C extends Conversion<?>> {
	default C of(M material) {
		return this.of(MaterialStoich.of(material));
	}
	default C of(MaterialState<M> state) {
		return this.of(MaterialStoich.of(state));
	}
	C of(MaterialStoich<M> stoich);
}