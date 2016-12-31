package org.pfaa.chemica.model;

public class SimpleAggregate extends SimpleMixture implements Aggregate {

	private Category category;
	
	public SimpleAggregate(Category category, MixtureComponent... components) {
		super(components);
		this.category = category;
	}

	private SimpleAggregate(Category category, Mixture mixture) {
		super(mixture.getComponents());
		this.category = category;
	}	
	
	public Aggregate mix(IndustrialMaterial material, double weight) {
		return new SimpleAggregate(this.category, super.mix(material, weight));
	}

	@Override
	public Aggregate removeAll() {
		return this;
	}

	@Override
	public Category getCategory() {
		return this.getCategory();
	}
	
}
