package org.pfaa.geologica;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;

import org.pfaa.chemica.Chemical;

public enum GeoSubstance {
	
	BRECCIA(Strength.WEAK, SubstanceType.ROCK),
	CARBONATITE(Strength.WEAK, SubstanceType.ROCK),
	CLAYSTONE(Strength.WEAK, SubstanceType.ROCK), // like sandstone
	CONGLOMERATE(Strength.WEAK, SubstanceType.ROCK),
	MUDSTONE(Strength.WEAK, SubstanceType.ROCK),
	
	LIMESTONE(Strength.MEDIUM, SubstanceType.ROCK),
	SCHIST(Strength.MEDIUM, SubstanceType.ROCK),
	SERPENTINITE(Strength.MEDIUM, SubstanceType.ROCK),
	SLATE(Strength.MEDIUM, SubstanceType.ROCK),
	SKARN(Strength.MEDIUM, SubstanceType.ROCK),
	
	ANDESITE(Strength.STRONG, SubstanceType.ROCK),
	BASALT(Strength.STRONG, SubstanceType.ROCK),
	GNEISS(Strength.STRONG, SubstanceType.ROCK),
	GRANITE(Strength.STRONG, SubstanceType.ROCK),
	GREENSCHIST(Strength.STRONG, SubstanceType.ROCK),
	MARBLE(Strength.STRONG, SubstanceType.ROCK),
	PEGMATITE(Strength.STRONG, SubstanceType.ROCK),
	RHYOLITE(Strength.STRONG, SubstanceType.ROCK),
	
	DIORITE(Strength.VERY_STRONG, SubstanceType.ROCK),
	GABBRO(Strength.VERY_STRONG, SubstanceType.ROCK),
	HORNFELS(Strength.VERY_STRONG, SubstanceType.ROCK),
	PERIDOTITE(Strength.VERY_STRONG, SubstanceType.ROCK),
	QUARTZITE(Strength.VERY_STRONG, SubstanceType.ROCK),
	
	MAGNETITE(Strength.STRONG);

	public enum Strength { 
		WEAK, MEDIUM, STRONG, VERY_STRONG;
		
		public String getCamelName() {
			if (this == VERY_STRONG)
				return "veryStrong";
			else return name().toLowerCase();
		}
	}

	public enum SubstanceType {
		ROCK, ORE, MINERAL
	}
	
	private Strength strength;
	private Material material;
	private SubstanceType substanceType;
	
	GeoSubstance(Strength strength, SubstanceType substanceType, Material material) {
		this.strength = strength;
		this.material = material;
		this.substanceType = substanceType;
	}
	
	GeoSubstance(Strength strength, SubstanceType substanceType) {
		this(strength, substanceType, Material.rock);
	}
	
	GeoSubstance(Strength strength) {
		this(strength, SubstanceType.ORE);
	}
	
	public int getId() {
		return ordinal();
	}
	
	public SubstanceType getSubstanceType() {
		return substanceType;
	}

	public Strength getStrength() {
		return strength;
	}

	public Material getMaterial() {
		return material;
	}

	public String getLowerName() {
		return name().toLowerCase();
	}

	public static GeoSubstance getForId(int id) {
		return values()[id];
	}
	
	public static List<GeoSubstance> lookup(Strength strength, SubstanceType substanceType, Material material) {
		List<GeoSubstance> substancesToReturn = new ArrayList<GeoSubstance>();
		for (GeoSubstance substance : values()) {
			if (substance.material == material && substance.strength == strength && substance.substanceType == substanceType)
				substancesToReturn.add(substance);
		}
		return substancesToReturn;
	}
}
