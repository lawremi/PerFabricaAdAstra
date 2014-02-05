package org.pfaa.geologica.processing;

import java.util.List;

import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.Molecule.Molecules;
import org.pfaa.chemica.model.PhaseProperties;

public interface Ore extends Mixture {
	public Chemical getConcentrate();
	public Ore add(IndustrialMaterial material, double weight);
	
	public static enum SmeltingTemperature {
		LOW, MEDIUM, HIGH, VERY_HIGH
	}
}
