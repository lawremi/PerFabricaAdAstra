package org.pfaa.geologica;

import org.pfaa.Registrant;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "PFAAGeologica", name = "Geologica", version = "0.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class Geologica {

	@Instance("PFAAGeologica")
	public static Geologica instance;
	
	public static Geologica getInstance() {
		return instance;
	}
	
	@SidedProxy(clientSide = "org.pfaa.geologica.client.ClientRegistrant", 
			    serverSide = "org.pfaa.geologica.CommonRegistrant")
	public static Registrant registrant;
	
	private Configuration configuration;
	
	public Configuration getConfiguration() {
		return configuration;
	}
	
	@PreInit
	public void preload(FMLPreInitializationEvent event)
	{
		configuration = new Configuration(event.getSuggestedConfigurationFile());
		registrant.preregister();
	}
	
	@Init
	public void load(FMLInitializationEvent event)
	{
		registrant.register();
	}
	
	@PostInit
	public void postload(FMLPostInitializationEvent event) {
		registrant.postregister();
		configuration.save();
	}
	
	public static final String RESOURCE_DIR = "/org/pfaa/geologica/resources";
}
