package org.pfaa.geologica.processing;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.SimpleMixture;

public class SimpleAggregate extends SimpleMixture implements Aggregate {

	public SimpleAggregate(MixtureComponent... components) {
		super(components);
	}

	private SimpleAggregate(Mixture mixture) {
		super(mixture.getComponents());
	}	
	
	public Aggregate mix(IndustrialMaterial material, double weight) {
		return new SimpleAggregate(super.mix(material, weight));
	}
	
}
