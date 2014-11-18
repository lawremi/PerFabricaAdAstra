package org.pfaa.geologica.registration;

import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.registry.GameRegistry;


public class EntityRegistration {
	public static void init() {
		registerTileEntities();
	}

	private static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntity.class, "GeoBlockHost");
	}
}
