package org.pfaa.chemica.model;

import java.util.List;

public interface Mixture extends IndustrialMaterial {
	public List<MixtureComponent> getComponents();
}
