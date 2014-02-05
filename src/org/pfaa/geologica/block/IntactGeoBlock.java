package org.pfaa.geologica.block;

import java.util.Random;

import net.minecraft.block.material.Material;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.GeoMaterial.Strength;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.processing.Gem;

public class IntactGeoBlock extends GeoBlock {

	public IntactGeoBlock(int id, Strength strength, Class<? extends IndustrialMaterial> composition, Material material) {
		super(id, strength, composition, material);
	}

	@Override
    public int quantityDroppedWithBonus(int par1, Random par2Random)
    {
        if (par1 > 0 && getSubstance(0).getComposition() instanceof Gem)
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
		GeoMaterial material = getSubstance(meta);
		if (material.getComposition() instanceof Mixture && blockMaterial == Material.rock) {
			dropped = dropRock(meta);
		} else if (material.getComposition() instanceof Gem) {
			// TODO: create a gem item
		}
		return dropped;
	}
	
	private int dropRock(int meta) {
		int dropped = 0;
		GeoMaterial material = getSubstance(meta);
		switch(material.getStrength()) {
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
	protected boolean canSilkHarvest() {
		return true;
	}
}
