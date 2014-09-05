package org.pfaa.chemica.registration;

import net.minecraftforge.common.MinecraftForge;

import org.pfaa.chemica.client.fluid.FogHandler;
import org.pfaa.chemica.fluid.FluidContainerHandler;
import org.pfaa.chemica.fluid.RespirationHandler;

import cpw.mods.fml.common.FMLCommonHandler;

public class HandlerRegistration {
	public static void init() {
		MinecraftForge.EVENT_BUS.register(FluidContainerHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(RespirationHandler.INSTANCE);
		FMLCommonHandler.instance().bus().register(RespirationHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(FogHandler.INSTANCE);
	}
}
