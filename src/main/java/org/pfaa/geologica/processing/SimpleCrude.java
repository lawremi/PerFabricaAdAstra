package org.pfaa.geologica.processing;

import java.util.List;

import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.SimpleMixture;
import org.pfaa.chemica.model.Vaporization;

public class SimpleCrude extends SimpleMixture implements Crude {
	private ConditionProperties properties;
	private Vaporization vaporization;
	private double sulfurFraction;
	private double heat;
	private String oreDictKey;

	public SimpleCrude(ConditionProperties properties, Vaporization vaporization, double sulfurFraction, double heat) 
	{
		this.properties = properties;
		this.vaporization = vaporization;
		this.sulfurFraction = sulfurFraction;
		this.heat = heat;
	}

	public SimpleCrude(IndustrialMaterial material, double weight) {
		super(material, weight);
	}
	
	public SimpleCrude(List<MixtureComponent> components, String oreDictKey) {
		super(components);
		this.oreDictKey = oreDictKey;
	}
	
	public SimpleCrude(List<MixtureComponent> components, double heat) {
		super(components);
		this.heat = heat;
	}

	@Override
	public ConditionProperties getProperties(Condition condition) {
		if (this.properties == null) {
			return super.getProperties(condition);
		} else {
			return this.properties;
		}
	}

	@Override
	public Crude mix(IndustrialMaterial material, double weight) {
		Mixture mixture = super.mix(material, weight);
		return new SimpleCrude(mixture.getComponents(), null);
	}

	@Override
	public Vaporization getVaporization() {
		return this.vaporization;
	}
	
	@Override
	public double getSulfurFraction() {
		return this.sulfurFraction;
	}
	
	/* Some notes on the heat of combustion:
	 * 
	 * We will have defined a defined combustion unit process (including a reaction in the case of a chemical) 
	 * that combines a fuel with O2 
	 * to produce heat, along with oxidized products. In the interest of modularity, we should specify
	 * the volatile products in the gaseous state. This means that the heat of combustion corresponds
	 * to the lower heating value (LHV). A system might eventually condense those gases to recover their
	 * heat, in which case the overall process has a higher heat (the HHV), but those are two separate
	 * processes within the same system.
	 * 
	 */
	
	@Override
	public double getHeat() {
		if (this.getComponents().size() > 0) {
			return this.getHeatFromMixture();
		} else {
			return this.heat;
		}
	}

	// TODO: If there are mixture components, we need to average the heat of the components.
	//       For crude materials, that is simple. For chemicals, we would need their combustion reaction,
	//       which have not been declared, so we ignore them, except we temporarily hard code volatiles to 48. 
	//       We handle water specially, such that anthracite has a heat of 30, bituminous coal 25, and lignite 16.
	private static final double WATER_HEAT_PENALTY = 45;
	private double getHeatFromMixture() {
		double heatSum = 0;
		for (MixtureComponent comp : this.getComponents()) {
			double compHeat = 0;
			if (comp.material instanceof Crude) {
				if (comp.material == Crudes.VOLATILES) {
					compHeat = 48.0; // FIXME: remove this special case when we can compute heats for chemicals
				} else {
					compHeat = ((Crude)comp.material).getHeat();
				}
			} else if (comp.material == Compounds.H2O) {
				compHeat = -WATER_HEAT_PENALTY;
			}
			heatSum += comp.weight * compHeat;
		}
		return heatSum / this.getTotalWeight();
	}

	@Override
	public String getOreDictKey() {
		return this.oreDictKey;
	}
	
	@Override
	public Crude oreDictify(String oreDictKey) {
		return new SimpleCrude(this.getComponents(), oreDictKey);
	}
}
