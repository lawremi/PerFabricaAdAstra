package org.pfaa.geologica.block;

import net.minecraft.block.material.Material;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.geologica.GeoMaterial.Strength;

public class BrickGeoBlock extends GeoBlock {

	public BrickGeoBlock(Strength strength, Class<? extends IndustrialMaterial> composition, Material material) {
		super(strength, composition, material);
	}

	@Override
	protected float determineHardness() {
		float hardness = super.determineHardness();
		if (blockMaterial == Material.clay)
			hardness = hardness * 3.0F; 
		return hardness;
	}
	
}
