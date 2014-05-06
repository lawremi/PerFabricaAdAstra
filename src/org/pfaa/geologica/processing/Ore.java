package org.pfaa.geologica.processing;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;

public interface Ore extends Mixture {
	public Mineral getConcentrate();
	public Ore add(IndustrialMaterial material, double weight);
}
