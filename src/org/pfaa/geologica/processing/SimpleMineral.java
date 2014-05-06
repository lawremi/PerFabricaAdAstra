package org.pfaa.geologica.processing;

import java.util.List;

import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.SimpleMixture;

public class SimpleMineral extends SimpleMixture implements Mineral {
	public SimpleMineral(Chemical concentrate, double weight) {
		super(concentrate, weight);
	}

	public SimpleMineral(List<MixtureComponent> components) {
		super(components);
	}

	@Override
	public Ore add(IndustrialMaterial material, double weight) {
		return new SimpleOre(this).add(material, weight);
	}
}
