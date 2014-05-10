package org.pfaa.chemica.model;

import org.pfaa.chemica.model.IndustrialMaterial.Phase;

public class MixtureComponent {
	public final IndustrialMaterial material;
	public final double weight;
	
	public MixtureComponent(IndustrialMaterial material, double weight) {
		this.material = material;
		this.weight = weight;
	}
	
	public IndustrialMaterial getMaterial() {
		return this.material;
	}
}
