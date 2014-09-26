package org.pfaa.geologica.integration;

import org.pfaa.geologica.block.GeoBlock;

public class FMPIntegration {
	public static void registerMicroblock(GeoBlock block) {
		for (int m = 0; m < block.getMetaCount(); m++) {
		    /*
			MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(block, m), 
					                               block.getUnlocalizedName() + "." + block.getBlockNameSuffix(m));
					                               */
		}
	}
}
