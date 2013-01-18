package org.pfaa.geologica.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

import org.pfaa.geologica.GeoSubstance;
import org.pfaa.geologica.GeoSubstance.SubstanceType;
import org.pfaa.geologica.GeoSubstance.Strength;
import org.pfaa.geologica.GeologicaBlocks;

public class StoneBlock extends IntactGeoBlock {
	public StoneBlock(int id, Strength strength) {
		super(id, strength, SubstanceType.ROCK, Material.rock);
	}
}
