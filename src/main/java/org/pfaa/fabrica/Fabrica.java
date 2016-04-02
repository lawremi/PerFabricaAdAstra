package org.pfaa.fabrica;

import org.apache.logging.log4j.Logger;
import org.pfaa.fabrica.integration.ModIntegration;
import org.pfaa.core.registration.Registrant;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = "PFAAFabrica", 
	 useMetadata = true)
public class Fabrica {
	@Instance("PFAAFabrica")
	public static Fabrica instance;
	
	public static Fabrica getInstance() {
		return instance;
	}
	
	@SidedProxy(clientSide = "org.pfaa.fabrica.client.registration.ClientRegistrant", 
	            serverSide = "org.pfaa.fabrica.registration.CommonRegistrant")
	public static Registrant registrant;
	
	public static Logger log;
	
	private static FabricaConfiguration configuration;
	
	public static FabricaConfiguration getConfiguration() {
		return configuration;
	}
	
	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		log = event.getModLog();
		configuration = new FabricaConfiguration(event.getSuggestedConfigurationFile());
		registrant.preregister();
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		ModIntegration.init();
		registrant.register();
	}
	
	@EventHandler
	public void postload(FMLPostInitializationEvent event) {
		registrant.postregister();
		configuration.save();
	}
}
