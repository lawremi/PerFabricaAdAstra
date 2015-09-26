package org.pfaa.chemica.registration;

import org.pfaa.chemica.client.fluid.FogHandler;
import org.pfaa.chemica.fluid.RespirationHandler;
import org.pfaa.geologica.fluid.BucketHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

public class HandlerRegistration {
	public static void init() {
		MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(RespirationHandler.INSTANCE);
		FMLCommonHandler.instance().bus().register(RespirationHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(FogHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(EnvironmentRegistrant.INSTANCE);
	}
}
