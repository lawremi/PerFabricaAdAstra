package org.pfaa.chemica.block;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.core.block.CompositeBlockAccessors;

public interface IndustrialBlockAccessors extends CompositeBlockAccessors {
	public IndustrialMaterial getIndustrialMaterial(int meta);
}
