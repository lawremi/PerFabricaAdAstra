package org.pfaa.geologica.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.GeoMaterial.Strength;
import org.pfaa.geologica.GeologicaBlocks;

public class IntactGeoBlock extends GeoBlock {

	public IntactGeoBlock(Strength strength, Class<? extends IndustrialMaterial> composition, Material material) {
		super(strength, composition, material);
	}

	@Override
	public Item getItemDropped(int meta, Random random, int par3) {
		Item dropped = super.getItemDropped(meta, random, par3);
		GeoMaterial material = getSubstance(meta);
		if (material.getComposition() instanceof Mixture && blockMaterial == Material.rock) {
			dropped = dropRock(meta);
		}
		return dropped;
	}
	
	private Item dropRock(int meta) {
		Item dropped = null;
		GeoMaterial material = getSubstance(meta);
		switch(material.getStrength()) {
		case WEAK:
			dropped = Item.getItemFromBlock(GeologicaBlocks.WEAK_RUBBLE);
			break;
		case MEDIUM:
			dropped = Item.getItemFromBlock(GeologicaBlocks.MEDIUM_COBBLESTONE);
			break;
		case STRONG:
			dropped = Item.getItemFromBlock(GeologicaBlocks.STRONG_COBBLESTONE);
			break;
		case VERY_STRONG:
			dropped = Item.getItemFromBlock(this);
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
