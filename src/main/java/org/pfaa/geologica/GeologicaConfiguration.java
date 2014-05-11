package org.pfaa.geologica;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.geologica.GeoMaterial.Strength;
import org.pfaa.geologica.processing.Aggregate;

public class GeologicaConfiguration {

	private Configuration config;

	public GeologicaConfiguration(Configuration config) {
		this.config = config;
	}
	
	public GeologicaConfiguration(File file) {
		this(new Configuration(file));
	}

	public int getHarvestLevel(Class<? extends IndustrialMaterial> composition, Strength strength) {
		String key = composition.getSimpleName() + "." + strength.ordinal() + "_" + strength;
		Property prop = this.config.get("HarvestLevels", key, getDefaultHarvestLevel(composition, strength), 
				                        "Harvest level for " + strength + " " + composition.getSimpleName());
		return prop.getInt();
	}
	
	private int getDefaultHarvestLevel(Class<? extends IndustrialMaterial> composition, Strength strength) {
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
			if (!Aggregate.class.isAssignableFrom(composition))
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
