package org.pfaa.geologica.block;

import net.minecraft.block.material.Material;

import org.pfaa.geologica.GeoSubstance.Strength;
import org.pfaa.geologica.GeoSubstance.SubstanceType;
import org.pfaa.geologica.GeologicaTextures;

public class BrokenGeoBlock extends GeoBlock {

	public BrokenGeoBlock(int id, Strength strength, SubstanceType substanceType, Material material) {
		super(id, strength, substanceType, material);
	}

	@Override
	public String getTextureFile() {
		return GeologicaTextures.BROKEN;
	}

	@Override
	protected float determineResistance(Strength strength) {
		float resistance = 0;
		switch(strength) {
		case WEAK:
			resistance = 5.0F;
			break;
		case MEDIUM:
			resistance = 10.0F;
			break;
		case STRONG:
			resistance = 15.0F;
			break;
		case VERY_STRONG:
			resistance = 20.0F;
			break;
		default:
		}
		return resistance;
	}

	@Override
	protected float determineHardness(Strength strength) {
		float hardness = 0;
		switch(strength) {
		case WEAK:
			hardness = 1.0F;
			break;
		case MEDIUM:
			hardness = 2.0F;
			break;
		case STRONG:
			hardness = 2.5F;
			break;
		case VERY_STRONG:
			hardness = 3.0F;
			break;
		default:
		}
		return hardness;
	}

}
