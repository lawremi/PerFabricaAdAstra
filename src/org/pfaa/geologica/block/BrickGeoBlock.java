package org.pfaa.geologica.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import org.pfaa.geologica.GeoSubstance.Composition;
import org.pfaa.geologica.GeologicaTextures;
import org.pfaa.geologica.GeoSubstance.Strength;

public class BrickGeoBlock extends GeoBlock {

	public BrickGeoBlock(int id, Strength strength, Composition composition, Material material) {
		super(id, strength, composition, material);
	}

	@Override
	protected float determineHardness() {
		float hardness = super.determineHardness();
		if (blockMaterial == Material.clay)
			hardness = hardness * 3.0F; 
		return hardness;
	}
	
}
