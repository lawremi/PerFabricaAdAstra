package org.pfaa.geologica.processing;

import java.util.List;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.PhaseProperties;
import org.pfaa.chemica.model.SimpleMixture;

public class SimpleCrude extends SimpleMixture implements Crude {
	public SimpleCrude() {
	}

	public SimpleCrude(IndustrialMaterial material, double weight) {
		super(material, weight);
	}

	public SimpleCrude(List<MixtureComponent> components) {
		super(components);
	}

	@Override
	public Crude mix(IndustrialMaterial material, double weight) {
		Mixture mixture = super.mix(material, weight);
		return new SimpleCrude(mixture.getComponents());
	}
}
