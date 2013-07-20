package org.pfaa.geologica.block;

import net.minecraft.block.material.Material;

import org.pfaa.geologica.GeoSubstance.Strength;
import org.pfaa.geologica.GeoSubstance.Composition;
import org.pfaa.geologica.GeologicaTextures;

public class BrokenGeoBlock extends GeoBlock {

	public BrokenGeoBlock(int id, Strength strength, Composition substanceType, Material material) {
		super(id, strength, substanceType, material);
	}

	@Override
	protected float determineHardness() {
		return super.determineHardness() * 2 / 3;
	}

}
