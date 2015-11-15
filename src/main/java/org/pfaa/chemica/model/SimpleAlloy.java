package org.pfaa.chemica.model;


public class SimpleAlloy extends SimpleMixture implements Alloy {

	private Element base;
	private Strength strength;
	private Fusion fusion;
	
	public SimpleAlloy(Element base, Element solute, double weight) {
		super(new MixtureComponent(base, 1.0), new MixtureComponent(solute, weight));
		this.base = base;
		this.strength = base.getStrength();
	}

	private SimpleAlloy(Element base, Mixture mixture, Strength strength) {
		super(mixture.getComponents());
		this.base = base;
		this.strength = strength;
	}

	@Override
	public Element getBase() {
		return this.base;
	}

	@Override
	public Alloy alloy(Element material, double weight) {
		Mixture mixture = super.mix(material, weight);
		return new SimpleAlloy(this.base, mixture, this.strength);
	}

	@Override
	public Strength getStrength() {
		return this.strength;
	}
	
	public Alloy setStrength(Strength strength) {
		this.strength = strength;
		return this;
	}
	
	@Override
	public Fusion getFusion() {
		return this.fusion;
	}

	@Override
	public Alloy fuse(int temperature) {
		this.fusion = new Fusion(temperature);
		return this;
	}
}
