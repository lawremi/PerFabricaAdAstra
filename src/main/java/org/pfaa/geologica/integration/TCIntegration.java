package org.pfaa.geologica.integration;

import net.minecraft.block.Block;

import org.pfaa.geologica.GeologicaBlocks;

public class TCIntegration {
	private static final int STONE_ID = 1;
	private static final int STONE_HARVEST_LEVEL = 1;
	
	public static void addStoneMaterials() {
		addStoneMaterial(50, GeologicaBlocks.WEAK_STONE, 0.25, 0.5);
		addStoneMaterial(51, GeologicaBlocks.MEDIUM_STONE, 0.50, 0.8);
		addStoneMaterial(52, GeologicaBlocks.STRONG_STONE, 1.0, 1.0);
		addStoneMaterial(53, GeologicaBlocks.VERY_STRONG_STONE, 1.5, 1.2);
	}
	
	public static void addStoneMaterial(int materialID, Block stone, double durabilityMultiplier, double speedMultiplier) {
		// TODO: re-add support for this, which never worked anyway
	}
}
