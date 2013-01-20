package org.pfaa.geologica.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;

import org.pfaa.geologica.GeoSubstance;
import org.pfaa.geologica.GeoSubstance.Strength;
import org.pfaa.geologica.GeoSubstance.SubstanceType;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.GeologicaTextures;

public class RubbleBlock extends LooseGeoBlock {
	
	public RubbleBlock(int id, Strength strength) {
		super(id, strength, SubstanceType.ROCK, Material.rock);
	}
		
}
