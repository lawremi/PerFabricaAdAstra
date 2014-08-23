package org.pfaa.chemica;

import org.apache.logging.log4j.Logger;
import org.pfaa.Registrant;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "PFAAChemica", 
     useMetadata = true)
public class Chemica {
	
	@Instance("PFAAChemica")
	public static Chemica instance;
	
	public static Chemica getInstance() {
		return instance;
	}
	
	@SidedProxy(clientSide = "org.pfaa.chemica.client.registration.ClientRegistrant", 
	            serverSide = "org.pfaa.chemica.registration.CommonRegistrant")
	public static Registrant registrant;
	
	public static Logger log;
	
	private static ChemicaConfiguration configuration;
	
	public static ChemicaConfiguration getConfiguration() {
		return configuration;
	}
	
	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		log = event.getModLog();
		configuration = new ChemicaConfiguration(event.getSuggestedConfigurationFile());
		registrant.preregister();
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		registrant.register();
	}
	
	@EventHandler
	public void postload(FMLPostInitializationEvent event) {
		registrant.postregister();
		configuration.save();
	}
}