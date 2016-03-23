package org.pfaa.geologica.block;

import org.pfaa.chemica.block.IndustrialBlockAccessors;
import org.pfaa.geologica.GeoMaterial;

public interface GeoBlockAccessors extends IndustrialBlockAccessors {

	public abstract GeoMaterial getGeoMaterial(int meta);

	public abstract boolean containsGeoMaterial(GeoMaterial material);

	public abstract int getMeta(GeoMaterial rock);

}
