package org.pfaa;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.src.Block;
import net.minecraftforge.common.Configuration;

import org.pfaa.geologica.Geologica;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.LoaderException;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ConfigIDProvider implements IDProvider {

	private static final ConfigIDProvider instance = new ConfigIDProvider();
	private static final int MAX_TERRAIN_BLOCK_ID = 255;
	private static final int MIN_TERRAIN_BLOCK_ID = 150;
	
	private int nextTerrainBlockId = MAX_TERRAIN_BLOCK_ID;
	private int nextPlacedBlockId = MAX_TERRAIN_BLOCK_ID + 1;
	private int nextItemId = 10000;
	
	public static ConfigIDProvider getInstance() {
		return instance;
	}
	private ConfigIDProvider() {
	}
	
	@Override
	public int nextTerrainBlockID(String name) {
		Configuration config = Geologica.getInstance().getConfiguration();
		return config.getBlock(name, nextTerrainBlockId--).getInt();
	}
	@Override
	public int nextBlockID(String name) {
		Configuration config = Geologica.getInstance().getConfiguration();
		return config.getBlock(name, nextPlacedBlockId++).getInt();
	}
	@Override
	public int nextItemID(String name) {
		Configuration config = Geologica.getInstance().getConfiguration();
		return config.getItem(name, nextItemId++).getInt();
	}

}
