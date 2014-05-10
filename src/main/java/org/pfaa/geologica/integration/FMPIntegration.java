package org.pfaa.geologica.integration;

import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.block.GeoBlock;

import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.MicroMaterialRegistry;

public class FMPIntegration {
	public static void registerMicroblock(GeoBlock block) {
		for (int m = 0; m < block.getMetaCount(); m++) {
			MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(block, m), 
					                               block.getUnlocalizedName() + "." + block.getBlockNameSuffix(m));
		}
	}
}
