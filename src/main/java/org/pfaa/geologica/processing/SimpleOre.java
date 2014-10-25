package org.pfaa.geologica.processing;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.SimpleMixture;

public class SimpleOre extends SimpleMixture implements Ore {

	private Mineral concentrate;

	public SimpleOre(Mineral concentrate) {
		super(concentrate);
		this.concentrate = concentrate;
	}
	
	public SimpleOre(Ore ore) {
		this(ore.getConcentrate(), ore);
	}
	
	private SimpleOre(Mineral concentrate, Mixture mixture) {
		super(mixture.getComponents());
		this.concentrate = concentrate;
	}
	
	@Override
	public Mineral getConcentrate() {
		return concentrate;
	}

	public Ore mix(IndustrialMaterial material, double weight) {
		Mixture mixture = super.mix(material, weight);
		return new SimpleOre(this.concentrate, mixture);
	}
	
	@Override
	public String name() {
		return concentrate.name() + " ore";
	}

	@Override
	public String getOreDictKey() {
		return concentrate.getOreDictKey();
	}
}
