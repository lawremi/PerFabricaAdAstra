package org.pfaa.fabrica.registration;

import org.pfaa.chemica.integration.ModIds;
import org.pfaa.fabrica.entity.TileEntityColored;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class EntityRegistration {
	public static void init() {
		registerTileEntities();
	}

	private static void registerTileEntities() {
		if (Loader.isModLoaded(ModIds.OPEN_BLOCKS)) {
			GameRegistry.registerTileEntity(TileEntityColored.class, "fabrica:colored");
		}
	}
}
