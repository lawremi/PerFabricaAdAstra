package org.pfaa.chemica.processing;

import java.util.List;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;

import com.google.common.collect.Lists;

public class Communition extends DegenerateConversion implements Sizing {	
	protected Communition(IndustrialMaterial material) {
		super(material);
	}

	@Override
	public Direction getDirection() {
		return Direction.DECREASE;
	}

	@Override
	public Form getOutputForm(Form inputForm) {
		return inputForm.communite();
	}

	@Override
	public List<MaterialStoich<?>> getOutputs() {
		List<MaterialStoich<?>> outputs = super.getOutputs();
		if (this.getMaterial() instanceof Mixture) { 
			Mixture mixture = (Mixture)this.getMaterial();
			outputs = Lists.newArrayList(outputs);
			mixture.getComponents().stream().
					map(MaterialStoich::of).
					map((s) -> s.scale(1/9)).
					forEach(outputs::add);
		}
		return outputs;
	}

	public static Communition of(IndustrialMaterial material) {
		return new Communition(material);
	}
}
