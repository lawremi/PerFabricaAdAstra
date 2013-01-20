package org.pfaa.geologica.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;

import org.pfaa.geologica.GeoSubstance.SubstanceType;
import org.pfaa.geologica.GeoSubstance.Strength;
import org.pfaa.geologica.GeoSubstance;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.GeologicaTextures;

public class IntactGeoBlock extends GeoBlock {

	public IntactGeoBlock(int id, Strength strength, SubstanceType substanceType, Material material) {
		super(id, strength, substanceType, material);
		MinecraftForge.setBlockHarvestLevel(this, "pickaxe", getHarvestLevelForStrength(strength));
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		return getSubstance(meta).getSubstanceType() == SubstanceType.MINERAL ? 4  + random.nextInt(5) : 1;
	}
	
	@Override
    public int quantityDroppedWithBonus(int par1, Random par2Random)
    {
        if (par1 > 0 && this.blockID != this.idDropped(0, par2Random, par1))
        {
            int var3 = par2Random.nextInt(par1 + 2) - 1;

            if (var3 < 0)
            {
                var3 = 0;
            }

            return this.quantityDropped(par2Random) * (var3 + 1);
        }
        else
        {
            return this.quantityDropped(par2Random);
        }
    }

	@Override
	public int idDropped(int meta, Random random, int par3) {
		int dropped = 0;
		GeoSubstance substance = getSubstance(meta);
		if (substance.getSubstanceType() == SubstanceType.ROCK) {
			dropped = dropRock(meta);
		} else if (substance.getSubstanceType() == SubstanceType.ORE) {
			dropped = this.blockID;
		} else {
			// TODO
		}
		return dropped;
	}
	
	private int dropRock(int meta) {
		int dropped = 0;
		GeoSubstance substance = getSubstance(meta);
		switch(substance.getStrength()) {
		case WEAK:
			dropped = GeologicaBlocks.WEAK_STONE_RUBBLE.blockID;
			break;
		case MEDIUM:
			dropped = GeologicaBlocks.MEDIUM_COBBLESTONE.blockID;
			break;
		case STRONG:
			dropped = GeologicaBlocks.STRONG_COBBLESTONE.blockID;
			break;
		case VERY_STRONG:
			dropped = this.blockID;
			break;
		default:
			break;
		}
		return dropped;
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
	
	protected float determineResistance(Strength strength) {
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
