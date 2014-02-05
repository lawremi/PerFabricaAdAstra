package org.pfaa.geologica.block;

import org.pfaa.block.CompositeBlockAccessors;
import org.pfaa.geologica.GeoMaterial;

public interface GeoBlockAccessors extends CompositeBlockAccessors {

	public abstract GeoMaterial getSubstance(int meta);

	public abstract boolean containsSubstance(GeoMaterial material);

	public abstract int getMeta(GeoMaterial rock);

}
