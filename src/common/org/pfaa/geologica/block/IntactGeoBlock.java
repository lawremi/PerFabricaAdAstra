package org.pfaa.geologica.block;

import net.minecraft.src.Material;
import net.minecraftforge.common.MinecraftForge;

import org.pfaa.geologica.GeoSubstance;
import org.pfaa.geologica.GeoSubstance.Strength;
import org.pfaa.geologica.Geologica;
import org.pfaa.geologica.GeologicaTextures;

public class IntactGeoBlock extends GeoBlock {

	public IntactGeoBlock(int id, Material material, Strength strength) {
		super(id, material, strength);
		MinecraftForge.setBlockHarvestLevel(this, "pickaxe", getHarvestLevelForStrength(strength));
	}

	private int getHarvestLevelForStrength(Strength strength) {
		int level = 0;
		switch(strength) {
		case WEAK:
		case MEDIUM:
			level = 0;
			break;
		case STRONG:
			level = 1;
			break;
		case VERY_STRONG:
			level = 2;
			break;
		default:
		}
		return level;
	}

	@Override
	public String getTextureFile()
	{
		return GeologicaTextures.INTACT;
	}
	
	protected float getResistanceForStrength(Strength strength) {
		float resistance = 0;
		switch(strength) {
		case WEAK:
			resistance = 10.0F;
			break;
		case MEDIUM:
			resistance = 20.0F;
			break;
		case STRONG:
			resistance = 30.0F;
			break;
		case VERY_STRONG:
			resistance = 40.0F;
			break;
		default:
		}
		return resistance;
	}

	protected float getHardnessForStrength(Strength strength) {
		float hardness = 0;
		switch(strength) {
		case WEAK:
			hardness = 1.0F;
			break;
		case MEDIUM:
			hardness = 2.0F;
			break;
		case STRONG:
			hardness = 3.0F;
			break;
		case VERY_STRONG:
			hardness = 4.0F;
			break;
		default:
		}
		return hardness;
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}
}
