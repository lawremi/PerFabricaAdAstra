package org.pfaa.chemica.model;

import java.util.List;

import org.pfaa.chemica.model.IndustrialMaterial.Phase;

public interface Mixture extends IndustrialMaterial {
	public List<MixtureComponent> getComponents();

	public abstract Mixture add(IndustrialMaterial material, double weight);
}
