package org.pfaa.chemica.model;


public class SimpleAlloy extends SimpleMixture implements Alloy {

	private Element base;
	
	public SimpleAlloy(Element base, Element solute, double weight) {
		super(new MixtureComponent(base, 1.0), new MixtureComponent(solute, weight));
		this.base = base;
	}

	private SimpleAlloy(Element base, Mixture mixture) {
		super(mixture.getComponents());
	}

	@Override
	public Element getBase() {
		return this.base;
	}

	@Override
	public Alloy alloy(Element material, double weight) {
		Mixture mixture = super.mix(material, weight);
		return new SimpleAlloy(this.base, mixture);
	}
}
