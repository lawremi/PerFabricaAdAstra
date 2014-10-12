package org.pfaa.geologica.block;

import org.pfaa.block.CompositeBlockAccessors;
import org.pfaa.geologica.GeoMaterial;

public interface GeoBlockAccessors extends CompositeBlockAccessors {

	public abstract GeoMaterial getGeoMaterial(int meta);

	public abstract boolean containsGeoMaterial(GeoMaterial material);

	public abstract int getMeta(GeoMaterial rock);

}
