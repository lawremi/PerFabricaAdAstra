package org.pfaa.chemica.model;

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

	@Override
	public Aggregate removeAll() {
		return this;
	}
	
}
