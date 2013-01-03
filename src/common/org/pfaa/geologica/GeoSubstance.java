package org.pfaa.geologica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.src.Material;

public class GeoSubstance {
	public enum Strength { WEAK, MEDIUM, STRONG, VERY_STRONG };

	private static List<GeoSubstance> substances = new ArrayList<GeoSubstance>();
	
	public static final GeoSubstance BRECCIA = new GeoSubstance("Breccia", Material.rock, Strength.WEAK, Material.rock);
	public static final GeoSubstance CARBONATITE = new GeoSubstance("Carbonatite", Material.rock, Strength.WEAK, Material.rock);
	public static final GeoSubstance CLAYSTONE = new GeoSubstance("Claystone", Material.rock, Strength.WEAK, Material.clay);
	public static final GeoSubstance CONGLOMERATE = new GeoSubstance("Conglomerate", Material.rock, Strength.WEAK, Material.rock);
	public static final GeoSubstance MUDSTONE = new GeoSubstance("Mudstone", Material.rock, Strength.WEAK, Material.ground);
	
	public static final GeoSubstance LIMESTONE = new GeoSubstance("Limestone", Material.rock, Strength.MEDIUM);
	public static final GeoSubstance SCHIST = new GeoSubstance("Schist", Material.rock, Strength.MEDIUM);
	public static final GeoSubstance SERPENTINITE = new GeoSubstance("Serpentinite", Material.rock, Strength.MEDIUM);
	public static final GeoSubstance SLATE = new GeoSubstance("Slate", Material.rock, Strength.MEDIUM);
	public static final GeoSubstance SKARN = new GeoSubstance("Skarn", Material.rock, Strength.MEDIUM);
	
	public static final GeoSubstance ANDESITE = new GeoSubstance("Andesite", Material.rock, Strength.STRONG);
	public static final GeoSubstance BASALT = new GeoSubstance("Basalt", Material.rock, Strength.STRONG);
	public static final GeoSubstance GNEISS = new GeoSubstance("Gneiss", Material.rock, Strength.STRONG);
	public static final GeoSubstance GRANITE = new GeoSubstance("Granite", Material.rock, Strength.STRONG);
	public static final GeoSubstance GREENSCHIST = new GeoSubstance("Greenschist", Material.rock, Strength.STRONG);
	public static final GeoSubstance MARBLE = new GeoSubstance("Marble", Material.rock, Strength.STRONG);
	public static final GeoSubstance PEGMATITE = new GeoSubstance("Pegmatite", Material.rock, Strength.STRONG);
	public static final GeoSubstance RHYOLITE = new GeoSubstance("Rhyolite", Material.rock, Strength.STRONG);
	
	public static final GeoSubstance DIORITE = new GeoSubstance("Diorite", Material.rock, Strength.VERY_STRONG);
	public static final GeoSubstance GABBRO = new GeoSubstance("Gabbro", Material.rock, Strength.VERY_STRONG);
	public static final GeoSubstance HORNFELS = new GeoSubstance("Hornfels", Material.rock, Strength.VERY_STRONG);
	public static final GeoSubstance PERIDOTITE = new GeoSubstance("Peridotite", Material.rock, Strength.VERY_STRONG);
	public static final GeoSubstance QUARTZITE = new GeoSubstance("Quartzite", Material.rock, Strength.VERY_STRONG);

	private int id;
	private String name;
	private Strength strength;
	private Material material;
	private Material parentMaterial;
	
	public GeoSubstance(String name, Material material, Strength strength, Material parentMaterial) {
		this.name = name;
		this.strength = strength;
		this.material = material;
		this.parentMaterial = parentMaterial;
		register();
	}
	
	public GeoSubstance(String name, Material material, Strength strength) {
		this(name, material, strength, null);
	}

	private void register() {
		this.id = substances.size();
		substances.add(this);
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
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

	public void setName(String name) {
		this.name = name;
	}

	public void setStrength(Strength strength) {
		this.strength = strength;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public void setParentMaterial(Material parentMaterial) {
		this.parentMaterial = parentMaterial;
	}

	public static GeoSubstance getForId(int id) {
		return substances.get(id);
	}
	
	public static List<GeoSubstance> getForMaterialAndStrength(Material material, Strength strength) {
		List<GeoSubstance> substancesToReturn = new ArrayList<GeoSubstance>();
		for (GeoSubstance substance : substances) {
			if (substance.material == material && substance.strength == strength)
				substancesToReturn.add(substance);
		}
		return substancesToReturn;
	}
}
