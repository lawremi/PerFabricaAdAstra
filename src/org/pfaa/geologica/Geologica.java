package org.pfaa.geologica;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import net.minecraftforge.common.Configuration;

import org.pfaa.Registrant;

import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "PFAAGeologica", 
	 name = "Geologica", 
	 version = "@VERSION@",
	 acceptedMinecraftVersions = "@MCVERSION@")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class Geologica {

	@Instance("PFAAGeologica")
	public static Geologica instance;
	
	public static Geologica getInstance() {
		return instance;
	}
	
	@SidedProxy(clientSide = "org.pfaa.geologica.client.ClientRegistrant", 
			    serverSide = "org.pfaa.geologica.registration.CommonRegistrant")
	public static Registrant registrant;
	
	public static Logger log;
	
	private Configuration configuration;
	
	public Configuration getConfiguration() {
		return configuration;
	}
	
	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		log = event.getModLog();
		log.setParent(FMLLog.getLogger());
		configuration = new Configuration(event.getSuggestedConfigurationFile());
		registrant.preregister();
		configuration.save();
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		registrant.register();
	}
	
	@EventHandler
	public void postload(FMLPostInitializationEvent event) {
		registrant.postregister();
		exportCOGConfig();
	}
	
	private void exportCOGConfig() {
		String filename = "CustomOreGen_Geologica.xml";
		String destPath = Loader.instance().getConfigDir() + File.separator + "CustomOreGen" + 
	                  File.separator + "modules" + File.separator + "mods" + File.separator + filename;
		File destFile = new File(destPath);
		destFile.getParentFile().mkdirs();
		String resource = "/config/" + filename;
		try {
			OutputStream out = new FileOutputStream(destFile);
			InputStream in = this.getClass().getResourceAsStream(resource);
			ByteStreams.copy(in, out);
			out.flush(); out.close();
			in.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Could not find '" + destPath + "':" + e);
		} catch (IOException e) {
			throw new RuntimeException("Failed to export resource '" + resource + "':" + e);
		}
	}

	public static final String RESOURCE_DIR = "/assets/geologica";

	public static boolean isTechnical() {
		return Loader.isModLoaded("gregtech_addon");
	}
}
