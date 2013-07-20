package org.pfaa.chemica.model;

public class Hazard {
	public enum SpecialCode {
		NONE, OXIDIZER, WATER_REACTIVE, SIMPLE_ASPHYXIANT, CORROSIVE, BIOHAZARD, POISONOUS, RADIOACTIVE, CRYOGENIC
	}
	
	private int health;
	private int flammability;
	private int instability;
	private SpecialCode special;
	
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
}
