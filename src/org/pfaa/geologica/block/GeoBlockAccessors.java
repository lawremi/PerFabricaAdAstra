package org.pfaa.geologica.block;

import org.pfaa.block.CompositeBlockAccessors;
import org.pfaa.geologica.GeoSubstance;

public interface GeoBlockAccessors extends CompositeBlockAccessors {

	public abstract GeoSubstance getSubstance(int meta);

	public abstract boolean containsSubstance(GeoSubstance substance);

	public abstract int getMeta(GeoSubstance rock);

}