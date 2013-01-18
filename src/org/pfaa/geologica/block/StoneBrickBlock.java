package org.pfaa.geologica.block;

import net.minecraft.block.Block;

import org.pfaa.geologica.GeoSubstance.Strength;

public class StoneBrickBlock extends BrickBlock {

	public StoneBrickBlock(int id, Strength strength) {
		super(id, strength);
		setStepSound(Block.soundStoneFootstep);
	}

}
