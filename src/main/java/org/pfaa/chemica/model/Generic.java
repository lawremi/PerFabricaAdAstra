package org.pfaa.chemica.model;

import com.google.common.collect.ImmutableList;

// Represents a role in one or more recipes, not a general class of material 
public interface Generic extends IndustrialMaterial {
	public ImmutableList<IndustrialMaterial> getSpecifics();
}
