package org.pfaa.geologica;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;

public enum GeoSubstance {
	
	BRECCIA(Material.rock, Strength.WEAK, Material.rock),
	CARBONATITE(Material.rock, Strength.WEAK, Material.rock),
	CLAYSTONE(Material.rock, Strength.WEAK, Material.clay),
	CONGLOMERATE(Material.rock, Strength.WEAK, Material.rock),
	MUDSTONE(Material.rock, Strength.WEAK, Material.ground),
	
	LIMESTONE(Material.rock, Strength.MEDIUM),
	SCHIST(Material.rock, Strength.MEDIUM),
	SERPENTINITE(Material.rock, Strength.MEDIUM),
	SLATE(Material.rock, Strength.MEDIUM),
	SKARN(Material.rock, Strength.MEDIUM),
	
	ANDESITE(Material.rock, Strength.STRONG),
	BASALT(Material.rock, Strength.STRONG),
	GNEISS(Material.rock, Strength.STRONG),
	GRANITE(Material.rock, Strength.STRONG),
	GREENSCHIST(Material.rock, Strength.STRONG),
	MARBLE(Material.rock, Strength.STRONG),
	PEGMATITE(Material.rock, Strength.STRONG),
	RHYOLITE(Material.rock, Strength.STRONG),
	
	DIORITE(Material.rock, Strength.VERY_STRONG),
	GABBRO(Material.rock, Strength.VERY_STRONG),
	HORNFELS(Material.rock, Strength.VERY_STRONG),
	PERIDOTITE(Material.rock, Strength.VERY_STRONG),
	QUARTZITE(Material.rock, Strength.VERY_STRONG);

	public enum Strength { WEAK, MEDIUM, STRONG, VERY_STRONG };

	private Strength strength;
	private Material material;
	private Material parentMaterial;
	
	GeoSubstance(Material material, Strength strength, Material parentMaterial) {
		this.strength = strength;
		this.material = material;
		this.parentMaterial = parentMaterial;
	}
	
	GeoSubstance(Material material, Strength strength) {
		this(material, strength, null);
	}

	public int getId() {
		return ordinal();
	}
	
	public Strength getStrength() {
		return strength;
	}

	public Material getMaterial() {
		return material;
	}

	public Material getParentMaterial() {
		return parentMaterial;
	}
	
	public String getLowerName() {
		return name().toLowerCase();
	}

	public static GeoSubstance getForId(int id) {
		return values()[id];
	}
	
	public static List<GeoSubstance> getForMaterialAndStrength(Material material, Strength strength) {
		List<GeoSubstance> substancesToReturn = new ArrayList<GeoSubstance>();
		for (GeoSubstance substance : values()) {
			if (substance.material == material && substance.strength == strength)
				substancesToReturn.add(substance);
		}
		return substancesToReturn;
	}
}
