package org.pfaa.chemica.registration;

import net.minecraftforge.common.MinecraftForge;

import org.pfaa.geologica.fluid.BucketHandler;

public class HandlerRegistration {
	public static void init() {
		MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);
	}
}
