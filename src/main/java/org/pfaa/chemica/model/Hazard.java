package org.pfaa.chemica.model;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Hazard {
	public enum SpecialCode {
		NONE, OXIDIZER, WATER_REACTIVE, SIMPLE_ASPHYXIANT, CORROSIVE, BIOHAZARD, POISONOUS, RADIOACTIVE, CRYOGENIC
	}
	
	public final int health;
	public final int flammability;
	public final int instability;
	public final SpecialCode special;
	
	public Hazard(int health, int flammability, int instability, SpecialCode special) {
		if (health < 0 || health > 4)
			throw new IllegalArgumentException("'health' must be in [0,4]");
		if (flammability < 0 || flammability  > 4)
			throw new IllegalArgumentException("'flammability' must be in [0,4]");
		if (instability < 0 || instability > 4)
			throw new IllegalArgumentException("'instability' must be in [0,4]");
		this.health = health;
		this.flammability = flammability;
		this.instability = instability;
		this.special = special;
	}
	
	public Hazard(int health, int flammability, int instability) {
		this(health, flammability, instability, SpecialCode.NONE);
	}

	public Hazard() {
		this(1, 0, 0);
	}

	private static final double[] IGNITION_TEMP_LIMITS = new double[] { 
		Double.POSITIVE_INFINITY, 500, 400, Constants.STANDARD_TEMPERATURE, 200
	};

	public boolean isFlammable(Condition condition) {
		return condition.temperature >= IGNITION_TEMP_LIMITS[this.flammability];
	}
	
	private static final double[] EXPLOSION_TEMP_LIMITS = new double[] { 
		Double.POSITIVE_INFINITY, 500, 400, 300, Constants.STANDARD_TEMPERATURE
	};
	
	public int getExplosionRadius(Condition condition) {
		if (condition.temperature >= EXPLOSION_TEMP_LIMITS[this.instability]) {
			return this.instability; 
		} else {
			return 0;
		}
	}
	
	private PotionEffect createPotionEffect(Potion potion, int durationAmplifier) {
		return new PotionEffect(potion.id, 20 * (int)Math.pow(2, this.health + durationAmplifier));
	}
	
	// happens when inside a fluid block
	public List<PotionEffect> getContactEffects() {
		List<PotionEffect> effects = new ArrayList<PotionEffect>();
		if (this.health >= 2) {
			effects.add(this.createPotionEffect(Potion.poison, 0));
		}
		return effects;
	}
	
	// happens when drinking a liquid (potion)
	public List<PotionEffect> getIngestionEffects() {
		List<PotionEffect> effects = new ArrayList<PotionEffect>();
		if (this.health >= 1) {
			effects.add(this.createPotionEffect(Potion.confusion, 1));
		}
		if (this.health >= 2) {
			effects.add(this.createPotionEffect(Potion.poison, 1));
		}
		return effects;
	}
}
