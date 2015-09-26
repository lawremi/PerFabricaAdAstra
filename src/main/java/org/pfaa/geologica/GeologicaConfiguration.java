package org.pfaa.geologica;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.pfaa.chemica.model.Aggregate;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Strength;
import org.pfaa.geologica.integration.GTIntegration;

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
		Property prop = this.config.get("InitialStoneToolDamage", strength.name(), 
				getDefaultInitialStoneToolDamage(strength), 
				"Fraction damaged for " + strength + " stone tools");
		return (float)prop.getDouble(0);
	}
	
	private static float getDefaultInitialStoneToolDamage(Strength strength) {
		float damage;
		if (GTIntegration.isGregtechInstalled()) {
			return GTIntegration.getDefaultInitialStoneToolDamage(strength);
		}
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

	public float getRockHardness(Strength strength) {
		Property prop = this.config.get("RockHardness", strength.ordinal() + "_" + strength.name(), 
				getDefaultRockHardness(strength), 
                "Hardness for " + strength + " rocks");
		Property mult = this.config.get("RockHardness", "multiplier", 1.0F, "Multiplier of all rock hardness values");
		return (float)prop.getDouble(0) * (float)mult.getDouble(1.0F);
	}

	public float getRockResistance(Strength strength) {
		Property mult = this.config.get("RockResistance", "multiplier", 5.0F, "Multiplier that transforms hardness to resistance");
		return this.getRockHardness(strength) * (float)mult.getDouble(5.0F);
	}
	
	private static float getDefaultRockHardness(Strength strength) {
		float hardness = 0;
		switch(strength) {
		case WEAK:
			hardness = 1.0F;
			break;
		case MEDIUM:
			hardness = 2.0F;
			break;
		case STRONG:
			hardness = 3.0F;
			break;
		case VERY_STRONG:
			hardness = 4.0F;
			break;
		default:
		}
		return hardness;
	}

	public boolean isVanillaOreOverrideEnabled() {
		Property bool = this.config.get("Toggles", "overrideVanillaOreBlocks", false, 
			"Whether to replace vanilla ore blocks with blocks that drop ore blocks, instead of items (DEPRECATED; do not change)");
		return bool.getBoolean(false);
	}
	
	public boolean isVanillaOreGemDropEnabled() {
		Property bool = this.config.get("Toggles", "vanillaOreDropsGems", true, 
			"Whether vanilla ore blocks should drop gems, as they do in vanilla");
		return bool.getBoolean(false);
	}
}
