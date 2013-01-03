package org.pfaa.geologica.block;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.Material;

import org.pfaa.geologica.GeoSubstance;
import org.pfaa.geologica.GeoSubstance.Strength;
import org.pfaa.geologica.GeologicaBlocks;

public class StoneBlock extends IntactGeoBlock {
	public StoneBlock(int id, Strength strength) {
		super(id, Material.rock, strength);
		setStepSound(Block.soundStoneFootstep);
	}
	
	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		int quantity;
		Material parentMaterial = getSubstance(meta).getParentMaterial();
		if (parentMaterial == Material.clay) {
			quantity = 8;
		} else {
			quantity = 1;
		}
		return quantity;
	}

	@Override
	public int idDropped(int meta, Random random, int par3) {
		int dropped = 0;
		GeoSubstance substance = getSubstance(meta);
		Material parentMaterial = substance.getParentMaterial();
		switch(substance.getStrength()) {
		case WEAK:
			if (parentMaterial == Material.ground) {
				dropped = Block.dirt.blockID;
			} else if (parentMaterial == Material.rock) {
				dropped = Block.gravel.blockID;
			} else if (parentMaterial == Material.clay) {
				dropped = Item.clay.shiftedIndex;
			} else dropped = -1;
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
}
