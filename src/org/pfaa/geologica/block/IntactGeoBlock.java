package org.pfaa.geologica.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;

import org.pfaa.geologica.GeoSubstance.Composition;
import org.pfaa.geologica.GeoSubstance.Strength;
import org.pfaa.geologica.GeoSubstance;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.GeologicaTextures;

public class IntactGeoBlock extends GeoBlock {

	public IntactGeoBlock(int id, Strength strength, Composition composition, Material material) {
		super(id, strength, composition, material);
	}

	@Override
    public int quantityDroppedWithBonus(int par1, Random par2Random)
    {
        if (par1 > 0 && getSubstance(0).getComposition() == Composition.MINERAL)
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
		int dropped = super.idDropped(meta, random, par3);
		GeoSubstance substance = getSubstance(meta);
		if (substance.getComposition() == Composition.ROCK && blockMaterial == Material.rock) {
			dropped = dropRock(meta);
		} else if (substance.getComposition() == Composition.MINERAL) {
			// TODO: create a mineral/chemical item
		}
		return dropped;
	}
	
	private int dropRock(int meta) {
		int dropped = 0;
		GeoSubstance substance = getSubstance(meta);
		switch(substance.getStrength()) {
		case WEAK:
			dropped = GeologicaBlocks.WEAK_RUBBLE.blockID;
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

	@Override
	public String getTextureFile()
	{
		return GeologicaTextures.INTACT;
	}
		
	@Override
	protected boolean canSilkHarvest() {
		return true;
	}
}
