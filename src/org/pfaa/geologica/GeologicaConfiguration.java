package org.pfaa.geologica;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

import org.pfaa.IDProvider;
import org.pfaa.geologica.GeoSubstance.Composition;
import org.pfaa.geologica.GeoSubstance.Strength;

public class GeologicaConfiguration implements IDProvider {

	private Configuration config;

	public GeologicaConfiguration(Configuration config) {
		this.config = config;
	}
	
	public GeologicaConfiguration(File file) {
		this(new Configuration(file));
	}

	private static final int MAX_TERRAIN_BLOCK_ID = 255;
	private static final int MIN_TERRAIN_BLOCK_ID = 150;
	
	private int nextTerrainBlockId = MAX_TERRAIN_BLOCK_ID;
	private int nextPlacedBlockId = MAX_TERRAIN_BLOCK_ID + 1;
	private int nextItemId = 10000;
	
	@Override
	public int nextTerrainBlockID(String name) {
		return this.config.getBlock(name, nextTerrainBlockId--).getInt();
	}
	@Override
	public int nextBlockID(String name) {
		return this.config.getBlock(name, nextPlacedBlockId++).getInt();
	}
	@Override
	public int nextItemID(String name) {
		return this.config.getItem(name, nextItemId++).getInt();
	}

	public int getHarvestLevel(Composition composition, Strength strength) {
		String key = composition + "." + strength;
		Property prop = this.config.get("HarvestLevels", key, getDefaultHarvestLevel(composition, strength), 
				                        "Harvest level for " + strength + " " + composition);
		return prop.getInt();
	}
	
	private int getDefaultHarvestLevel(Composition composition, Strength strength) {
		int level = 0;
		switch(strength) {
		case WEAK:
			level = 0;
			break;
		case MEDIUM:
			level = 1;
			break;
		case STRONG:
			level = 1;
			if (composition != Composition.AGGREGATE)
				level++;
			break;
		case VERY_STRONG:
			level = 2;
			break;
		default:
		}
		return level;
	}

	public void save() {
		this.config.save();
	}

	public float getInitialStoneToolDamage(Strength strength) {
		Property prop = this.config.get("InitialStoneToolDamage", strength.name(), getDefaultInitialStoneToolDamage(strength), 
				                        "Fraction damaged for " + strength + " stone tools");
		return (float)prop.getDouble(0);
	}
	
	private static float getDefaultInitialStoneToolDamage(Strength strength) {
		float damage;
		switch(strength) {
		case WEAK:
			damage = 0.75F;
			break;
		case MEDIUM:
			damage = 0.5F;
			break;
		default:
			damage = 0F;
		}
		return damage;
	}

}
