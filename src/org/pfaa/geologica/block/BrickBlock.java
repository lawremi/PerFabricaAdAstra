package org.pfaa.geologica.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import org.pfaa.geologica.GeoSubstance.SubstanceType;
import org.pfaa.geologica.GeologicaTextures;
import org.pfaa.geologica.GeoSubstance.Strength;

public class BrickBlock extends GeoBlock {

	public BrickBlock(int id, Strength strength) {
		super(id, strength, SubstanceType.ROCK, Material.rock);
	}

	@Override
	public String getTextureFile()
	{
		return GeologicaTextures.BRICK;
	}

	@Override
	protected float determineResistance(Strength strength) {
		float resistance = 0;
		switch(strength) {
		case WEAK:
			resistance = 15.0F;
			break;
		case MEDIUM:
			resistance = 25.0F;
			break;
		case STRONG:
			resistance = 35.0F;
			break;
		case VERY_STRONG:
			resistance = 45.0F;
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
			hardness = 1.5F;
			break;
		case MEDIUM:
			hardness = 2.5F;
			break;
		case STRONG:
			hardness = 3.5F;
			break;
		case VERY_STRONG:
			hardness = 4.5F;
			break;
		default:
		}
		return hardness;
	}

}
