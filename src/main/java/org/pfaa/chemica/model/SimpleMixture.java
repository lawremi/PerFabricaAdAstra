package org.pfaa.chemica.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimpleMixture implements Mixture {

	private List<MixtureComponent> components;
	private PhaseProperties[] properties = new PhaseProperties[Phase.values().length];
	
	protected SimpleMixture(List<MixtureComponent> components) {
		this.components = components;
		for (Phase phase : Phase.values()) {
			this.properties[phase.ordinal()] = computePhaseProperties(phase);
		}
	}

	public SimpleMixture(MixtureComponent... components) {
		this(Arrays.asList(components));
	}
	
	public SimpleMixture(IndustrialMaterial material, double weight) {
		this(new MixtureComponent(material, weight));
	}
	
	public SimpleMixture(IndustrialMaterial material) {
		this(new MixtureComponent(material, 1.0));
	}
	
	private double getTotalWeight() {
		double weight = 0;
		for (MixtureComponent comp : components) {
			weight += comp.weight;
		}
		return weight;
	}
	
	@Override
	public String name() {
		String name = "Mixture of ";
		double totalWeight = getTotalWeight();
		for (MixtureComponent comp : components) {
			name += (comp.weight / totalWeight) * 100 + "% " + comp.material.name() + ", ";
		}
		return name.substring(0, name.length() - 1);
	}

	@Override
	public String getOreDictKey() {
		return null;
	}

	private PhaseProperties computePhaseProperties(Phase phase) {
		double totalWeight = getTotalWeight();
		int r = 0, g = 0, b = 0;
		int health = 0, flammability = 0, instability = 0;
		float density = 0;
		for (MixtureComponent comp : this.components) {
			PhaseProperties props = comp.material.getProperties(phase);
			if (props == null)
				return null;
			double normWeight = comp.weight / totalWeight;
			r += props.color.getRed() * normWeight;
			b += props.color.getBlue() * normWeight;
			g += props.color.getGreen() * normWeight;
			density += props.density * normWeight;
			health = Math.max(health, props.hazard.health);
			flammability = Math.max(flammability, props.hazard.flammability);
			instability = Math.max(instability, props.hazard.instability);
		}
		return new PhaseProperties(new Color(r, g, b), density, new Hazard(health, flammability, instability));
	}

	@Override
	public PhaseProperties getProperties(Phase phase) {
		return this.properties[phase.ordinal()];
	}

	@Override
	public List<MixtureComponent> getComponents() {
		return Collections.unmodifiableList(this.components);
	}
	
	@Override
	public Mixture mix(IndustrialMaterial material, double weight) {
		List<MixtureComponent> components = new ArrayList(this.components);
		components.add(new MixtureComponent(material, weight));
		return new SimpleMixture(components);
	}
}
