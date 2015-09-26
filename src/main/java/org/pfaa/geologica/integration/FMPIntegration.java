package org.pfaa.geologica.integration;

import org.pfaa.chemica.integration.ModIds;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.block.GeoBlock;

import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.MicroMaterialRegistry;
import cpw.mods.fml.common.Loader;

public class FMPIntegration {
	public static void init() {
		if (Loader.isModLoaded(ModIds.FORGE_MICROBLOCK)) {
			FMPIntegration.registerMicroblock(GeologicaBlocks.WEAK_STONE);
			FMPIntegration.registerMicroblock(GeologicaBlocks.MEDIUM_STONE);
			FMPIntegration.registerMicroblock(GeologicaBlocks.STRONG_STONE);
			FMPIntegration.registerMicroblock(GeologicaBlocks.VERY_STRONG_STONE);
			FMPIntegration.registerMicroblock(GeologicaBlocks.MEDIUM_COBBLE);
			FMPIntegration.registerMicroblock(GeologicaBlocks.STRONG_COBBLE);
			FMPIntegration.registerMicroblock(GeologicaBlocks.VERY_STRONG_COBBLE);
		}
	}
	
	public static void registerMicroblock(GeoBlock block) {
		for (int m = 0; m < block.getMetaCount(); m++) {
			MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(block, m), 
					                               block.getUnlocalizedName() + "." + block.getBlockNameSuffix(m));
		}
	}
}
