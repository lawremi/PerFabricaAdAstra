package org.pfaa.chemica.model;

import java.awt.Color;

public interface IndustrialMaterial {
	public String name();
	public String getOreDictKey();
	public ConditionProperties getProperties(Condition condition);
	public Mixture mix(IndustrialMaterial material, double weight);
}
