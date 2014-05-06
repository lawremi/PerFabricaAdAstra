package org.pfaa.geologica.processing;

import java.util.List;

import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.PhaseProperties;
import org.pfaa.chemica.model.SimpleMixture;

public class SimpleOreMineral extends SimpleMineral implements OreMineral {

	private Chemical concentrate;

	public SimpleOreMineral(Chemical concentrate) {
		super(concentrate, 1.0);
		this.concentrate = concentrate;
	}
	
	private SimpleOreMineral(Chemical concentrate, Mixture mixture) {
		super(mixture.getComponents());
		this.concentrate = concentrate;
	}
	
	@Override
	public OreMineral add(IndustrialMaterial material, double weight) {
		Mixture mixture = super.add(material, weight);
		return new SimpleOreMineral(this.concentrate, mixture);
	}

	@Override
	public String name() {
		return getOreDictKey() + "Ore";
	}

	@Override
	public String getOreDictKey() {
		return concentrate.getOreDictKey();
	}

	@Override
	public Chemical getConcentrate() {
		return concentrate;
	}

}
