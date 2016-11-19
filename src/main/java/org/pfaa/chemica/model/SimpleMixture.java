package org.pfaa.chemica.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.primitives.Doubles;

public class SimpleMixture implements Mixture {

	private List<MixtureComponent> components;
	private Map<Condition,ConditionProperties> propertiesCache = new HashMap<Condition, ConditionProperties>();
	private String name;
	
	protected SimpleMixture(String name, List<MixtureComponent> components) {
		this.components = components;
		this.name = name;
	}
	
	protected SimpleMixture(List<MixtureComponent> components) {
		this(null, components);
	}

	public SimpleMixture(String name, MixtureComponent... components) {
		this(name, Arrays.asList(components));
	}
	
	public SimpleMixture(MixtureComponent... components) {
		this(null, components);
	}
	
	public SimpleMixture(IndustrialMaterial material, double weight) {
		this((String)null, new MixtureComponent(material, weight));
	}
	
	public SimpleMixture(IndustrialMaterial material) {
		this(new MixtureComponent(material, 1.0));
	}
	
	private double getTotalFluidWeight(Condition condition) {
		double weight = 0;
		for (MixtureComponent comp : components) {
			if (comp.getMaterial().getProperties(condition).state != State.SOLID)
				weight += comp.weight;
		}
		return weight;
	}
	
	@Override
	public String name() {
		if (this.name != null) {
			return this.name;
		}
		if (this.components.size() == 0) {
			return "Unspecified mixture";
		}
		String name = "Mixture of ";
		double totalWeight = getTotalWeight();
		for (MixtureComponent comp : components) {
			name += (int)((comp.weight / totalWeight) * 100) + "% " + comp.material.name() + ", ";
		}
		return name.substring(0, name.length() - 1);
	}

	@Override
	public String getOreDictKey() {
		return name();
	}

	@Override
	public ConditionProperties getProperties(Condition condition) {
		if (this.components.size() == 0) {
			return null;
		}
		if (this.components.size() == 1) {
			return this.components.get(0).material.getProperties(condition);
		}
		ConditionProperties mixProps = this.propertiesCache.get(condition);
		if (mixProps != null) {
			return mixProps;
		}
		double totalWeight = getTotalWeight();
		double totalAlpha = 0;
		int a = 0, r = 0, g = 0, b = 0, luminosity = 0;
		float health = 0, flammability = 0, instability = 0;
		double density = 0, opaqueWeight = 0;
		double[] stateWeight = new double[State.values().length];
		for (MixtureComponent comp : this.components) {
			ConditionProperties props = comp.material.getProperties(condition);
			if (props == null)
				continue;
			double normWeight = comp.weight / totalWeight;
			double alpha = props.color.getAlpha() / 255.0;
			totalAlpha += alpha * comp.weight;
			a += props.color.getAlpha() * comp.weight;
			r += props.color.getRed() * alpha * comp.weight;
			b += props.color.getBlue() * alpha * comp.weight;
			g += props.color.getGreen() * alpha * comp.weight;
			density += props.density * normWeight;
			luminosity += props.luminosity * normWeight;
			health += props.hazard.health * normWeight;
			flammability += props.hazard.flammability * normWeight;
			instability += props.hazard.instability * normWeight;
			stateWeight[props.state.ordinal()] += comp.weight;
			if (props.opaque)
				opaqueWeight += normWeight; 
		}
		Color color = StateProperties.COLORLESS;
		if (totalAlpha > 0) {
			color = new Color((int)(r / totalAlpha), (int)(g / totalAlpha), (int)(b / totalAlpha), (int)(a / totalAlpha));
		}
		State phase = State.values()[Doubles.indexOf(stateWeight, Doubles.max(stateWeight))];
		mixProps = new ConditionProperties(phase, color, density,
                                           new Hazard(Math.round(health), Math.round(flammability), Math.round(instability)), 
				                           this.getViscosity(condition, density), luminosity,
				                           opaqueWeight > 0.5);
		this.propertiesCache.put(condition, mixProps);
		return mixProps;
	}

	private static final double THOMAS_A = 0.00273;
	private static final double THOMAS_B = 16.6;

	private double getViscosity(Condition condition, double density) {
		double vbi = 0;
		double fluidWeight = getTotalFluidWeight(condition);
		// Chevron/Refutas method
		for (MixtureComponent comp : this.components) {
			ConditionProperties props = comp.material.getProperties(condition);
			if (props.state != State.SOLID) {
				double normWeight = comp.weight / fluidWeight;
				double cSt = props.viscosity / props.density;
				vbi += normWeight * Math.log(cSt) / Math.log(1000 * cSt);
			}
		}
		double viscosity = Math.exp(vbi * Math.log(1000) / (1 - vbi)) * density;
		double solidFraction = 1 - fluidWeight / this.getTotalWeight();
		if (solidFraction > 0) {
			double relativeViscosity = 1 + 2.5*solidFraction + 10.05*Math.pow(solidFraction, 2.0) + 
					THOMAS_A*Math.exp(THOMAS_B*solidFraction); // Thomas (1965)
			viscosity *= relativeViscosity;
		}
		return viscosity;
	}

	@Override
	public List<MixtureComponent> getComponents() {
		return Collections.unmodifiableList(this.components);
	}
	
	@Override
	public Mixture mix(IndustrialMaterial material, double weight) {
		List<MixtureComponent> components = new ArrayList<MixtureComponent>(this.components);
		components.add(new MixtureComponent(material, weight));
		return new SimpleMixture(components);
	}
	
	public String toString() {
		return this.name();
	}
}
