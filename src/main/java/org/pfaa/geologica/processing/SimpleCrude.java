package org.pfaa.geologica.processing;

import java.util.List;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.PhaseProperties;
import org.pfaa.chemica.model.SimpleMixture;
import org.pfaa.chemica.model.Vaporizable;
import org.pfaa.chemica.model.Vaporization;

public class SimpleCrude extends SimpleMixture implements Crude {
	private ConditionProperties properties;
	private Vaporization vaporization;
	private double sulfurFraction;

	public SimpleCrude(ConditionProperties properties, Vaporization vaporization, double sulfurFraction) {
		this.properties = properties;
		this.vaporization = vaporization;
		this.sulfurFraction = sulfurFraction;
	}

	public SimpleCrude(IndustrialMaterial material, double weight) {
		super(material, weight);
	}

	public SimpleCrude(List<MixtureComponent> components) {
		super(components);
	}
	
	@Override
	public ConditionProperties getProperties(Condition condition) {
		if (properties == null) {
			return super.getProperties(condition);
		} else {
			return properties;
		}
	}

	@Override
	public Crude mix(IndustrialMaterial material, double weight) {
		Mixture mixture = super.mix(material, weight);
		return new SimpleCrude(mixture.getComponents());
	}

	@Override
	public Vaporization getVaporization() {
		return this.vaporization;
	}
	
	public double getSulfurFraction() {
		return sulfurFraction;
	}
}
