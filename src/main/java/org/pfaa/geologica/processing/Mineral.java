package org.pfaa.geologica.processing;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;

public interface Mineral extends Mixture {
	public Ore mix(IndustrialMaterial material, double weight);
}
