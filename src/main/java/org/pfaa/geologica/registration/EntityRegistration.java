package org.pfaa.geologica.registration;

import org.pfaa.geologica.block.IntactGeoBlock;

import cpw.mods.fml.common.registry.GameRegistry;


public class EntityRegistration {
	public static void init() {
		registerTileEntities();
	}

	private static void registerTileEntities() {
		GameRegistry.registerTileEntity(IntactGeoBlock.HostTileEntity.class, "GeoBlockHost");
	}
}
