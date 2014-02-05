package org.pfaa.geologica;

import net.minecraftforge.common.Configuration;

import org.pfaa.Registrant;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "PFAAGeologica", 
	 name = "Geologica", 
	 version = "@VERSION@",
	 acceptedMinecraftVersions = "@MCVERSION@")
public class Geologica {

	@Instance("PFAAGeologica")
	public static Geologica instance;
	
	public static Geologica getInstance() {
		return instance;
	}
	
	@SidedProxy(clientSide = "org.pfaa.geologica.client.registration.ClientRegistrant", 
			    serverSide = "org.pfaa.geologica.registration.CommonRegistrant")
	public static Registrant registrant;
	
	private Configuration configuration;
	
	public Configuration getConfiguration() {
		return configuration;
	}
	
	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		configuration = new Configuration(event.getSuggestedConfigurationFile());
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
	
	public static final String RESOURCE_DIR = "/assets/geologica";
}
