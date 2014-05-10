package org.pfaa.geologica.block;

import java.util.Random;

import net.minecraft.block.material.Material;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.GeoMaterial.Strength;
import org.pfaa.geologica.GeologicaBlocks;

public class IntactGeoBlock extends GeoBlock {

	public IntactGeoBlock(int id, Strength strength, Class<? extends IndustrialMaterial> composition, Material material) {
		super(id, strength, composition, material);
	}

	@Override
	public int idDropped(int meta, Random random, int par3) {
		int dropped = super.idDropped(meta, random, par3);
		GeoMaterial material = getSubstance(meta);
		if (material.getComposition() instanceof Mixture && blockMaterial == Material.rock) {
			dropped = dropRock(meta);
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
