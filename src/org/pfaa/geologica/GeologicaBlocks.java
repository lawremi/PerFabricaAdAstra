package org.pfaa.geologica;

import net.minecraft.block.Block;

import org.pfaa.ConfigIDProvider;
import org.pfaa.geologica.GeoSubstance.Strength;
import org.pfaa.geologica.block.CobblestoneBlock;
import org.pfaa.geologica.block.StoneBlock;

public class GeologicaBlocks {
	public static final Block WEAK_STONE = createStoneBlock("WeakStone", Strength.WEAK);
	public static final Block MEDIUM_STONE = createStoneBlock("MediumStone", Strength.MEDIUM);
	public static final Block STRONG_STONE = createStoneBlock("StrongStone", Strength.STRONG);
	public static final Block VERY_STRONG_STONE = createStoneBlock("VeryStrongStone", Strength.VERY_STRONG);
	
	public static final Block MEDIUM_COBBLESTONE = createCobbleBlock("MediumCobblestone", Strength.MEDIUM);
	public static final Block STRONG_COBBLESTONE = createCobbleBlock("StrongCobblestone", Strength.STRONG);
	
	private static Block createStoneBlock(String name, Strength strength) {
		Block block = new StoneBlock(ConfigIDProvider.getInstance().nextBlockID(name), strength);
		block.setBlockName(name);
		return block;
	}

	private static Block createCobbleBlock(String name, Strength strength) {
		Block block = new CobblestoneBlock(ConfigIDProvider.getInstance().nextBlockID(name), strength);
		block.setBlockName(name);
		return block;
	}
}
