package org.pfaa.geologica.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import org.pfaa.geologica.GeoSubstance.Strength;
import org.pfaa.geologica.GeoSubstance.SubstanceType;

public class CobblestoneBlock extends BrokenGeoBlock {

	public CobblestoneBlock(int id, Strength strength) {
		super(id, strength, SubstanceType.ROCK, Material.rock);
	}

}
