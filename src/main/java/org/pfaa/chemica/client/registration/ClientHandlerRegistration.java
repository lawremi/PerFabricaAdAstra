package org.pfaa.chemica.client.registration;

import net.minecraftforge.common.MinecraftForge;

import org.pfaa.chemica.client.fluid.FogHandler;

public class ClientHandlerRegistration {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(FogHandler.INSTANCE);
	}

}
