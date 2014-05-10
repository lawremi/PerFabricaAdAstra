package org.pfaa.geologica.block;

import net.minecraft.block.material.Material;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.geologica.GeoMaterial.Strength;

public class BrokenGeoBlock extends GeoBlock {

	public BrokenGeoBlock(Strength strength, Class<? extends IndustrialMaterial> materialType, Material material) {
		super(strength, materialType, material);
	}

	@Override
	protected float determineHardness() {
		return super.determineHardness() * 2 / 3;
	}

}
