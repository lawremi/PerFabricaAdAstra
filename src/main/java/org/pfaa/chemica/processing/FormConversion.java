package org.pfaa.chemica.processing;

import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.State;

public abstract class FormConversion implements Conversion {
	private IndustrialMaterial material;
	
	protected FormConversion(IndustrialMaterial material) {
		this.material = material;
	}
	
	public IndustrialMaterial getMaterial() {
		return this.material;
	}
	
	@Override
	public Conversion.Type getType() {
		return null;
	}

	@Override
	public List<MaterialStoich<?>> getInputs() {
		return Collections.singletonList(MaterialStoich.of(State.SOLID.of(this.material)));
	}

	@Override
	public List<MaterialStoich<?>> getOutputs() {
		return this.getInputs();
	}
}
