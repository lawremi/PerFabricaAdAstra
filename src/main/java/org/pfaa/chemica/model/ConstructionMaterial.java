package org.pfaa.chemica.model;

public interface ConstructionMaterial extends IndustrialMaterial {
	public Strength getStrength();
	default boolean isForBuilding() {
		return this.getStrength() != null;
	}
}
