package org.pfaa.geologica;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;

import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.Hazard;
import org.pfaa.chemica.model.Chemicals;

import com.google.common.base.CaseFormat;

public enum GeoSubstance {
	
	BRECCIA(Strength.WEAK, Composition.AGGREGATE),
	CARBONATITE(Strength.WEAK, Composition.AGGREGATE),
	CLAYSTONE(Strength.WEAK, Composition.AGGREGATE),
	CONGLOMERATE(Strength.WEAK, Composition.AGGREGATE),
	MUDSTONE(Strength.WEAK, Composition.AGGREGATE),
	
	LIMESTONE(Strength.MEDIUM, Composition.AGGREGATE),
	SCHIST(Strength.MEDIUM, Composition.AGGREGATE),
	SERPENTINITE(Strength.MEDIUM, Composition.AGGREGATE),
	SLATE(Strength.MEDIUM, Composition.AGGREGATE),
	SKARN(Strength.MEDIUM, Composition.AGGREGATE),
	
	ANDESITE(Strength.STRONG, Composition.AGGREGATE),
	BASALT(Strength.STRONG, Composition.AGGREGATE),
	GNEISS(Strength.STRONG, Composition.AGGREGATE),
	GRANITE(Strength.STRONG, Composition.AGGREGATE),
	GREENSCHIST(Strength.STRONG, Composition.AGGREGATE),
	MARBLE(Strength.STRONG, Composition.AGGREGATE),
	PEGMATITE(Strength.STRONG, Composition.AGGREGATE),
	RHYOLITE(Strength.STRONG, Composition.AGGREGATE),
	
	DIORITE(Strength.VERY_STRONG, Composition.AGGREGATE),
	GABBRO(Strength.VERY_STRONG, Composition.AGGREGATE),
	HORNFELS(Strength.VERY_STRONG, Composition.AGGREGATE),
	PERIDOTITE(Strength.VERY_STRONG, Composition.AGGREGATE),
	QUARTZITE(Strength.VERY_STRONG, Composition.AGGREGATE),
	
	BASALTIC_MINERAL_SAND("iron", Strength.WEAK, Material.sand),
	CASSITERITE_SAND("tin", Strength.WEAK, Material.sand),
	GARNET_SAND(Strength.WEAK, Material.sand),
	GRANITIC_MINERAL_SAND("iron", Strength.WEAK, Material.sand),
	QUARTZ_SAND("quartz", Strength.WEAK, Material.sand),
	VOLCANIC_ASH("ash", Strength.WEAK, Material.sand),
	
	LATERITE(null, Strength.WEAK, Composition.AGGREGATE, Material.clay),
	
	BAUXITE(Strength.WEAK, Material.clay),
	BENTONITE(Strength.WEAK, Material.clay),
	FULLERS_EARTH(Strength.WEAK, Material.clay),
	KAOLINITE(Strength.WEAK, Material.clay),
	BROWN_LIMONITE("iron", Strength.WEAK, Material.clay),
	YELLOW_LIMONITE("iron", Strength.WEAK, Material.clay),
	VERMICULITE(Strength.WEAK, Material.clay),
	
	BORAX("boron", Strength.WEAK),
	CINNABAR(Strength.WEAK),
	GALENA("lead", Strength.WEAK),
	MOLYBDENITE("molybdenum", Strength.WEAK),
	PYROLUSITE("manganese", Strength.WEAK),
	ROCK_SALT("salt", Strength.WEAK),
	STIBNITE("antimony", Strength.WEAK),
	
	BARITE("barium", Strength.MEDIUM),
	BASTNASITE(Strength.MEDIUM),
	CHALCOPYRITE("copper", Strength.MEDIUM),
	GARNIERITE("nickel", Strength.MEDIUM),
	LEPIDOLITE("lithium", Strength.MEDIUM),
	MAGNESITE("magnesium", Strength.MEDIUM),
	PENTLANDITE("nickel", Strength.MEDIUM),
	SCHEELITE("tungsten", Strength.MEDIUM),
	SPHALERITE("zinc", Strength.MEDIUM),
	WOLFRAMITE("tungsten", Strength.MEDIUM),
	
	BANDED_IRON("iron", Strength.STRONG),
	BERYL("beryllium", Strength.STRONG),
	CASSITERITE("tin", Strength.STRONG),
	CHROMITE("chromium", Strength.STRONG),
	ILMENITE("titanium", Strength.STRONG),
	MAGNETITE("iron", Strength.STRONG),
	POLLUCITE("cesium", Strength.STRONG),
	SPODUMENE("lithium", Strength.STRONG),
	TANTALITE("tantalum", Strength.STRONG),
	URANINITE("uranium", Strength.STRONG),
	VANADIUM_MAGNETITE("iron", Strength.STRONG),
	
	CHRYSOTILE(Strength.WEAK),
	DIATOMITE(Strength.WEAK),
	GLAUCONITE(Strength.WEAK),
	GRAPHITE(Strength.WEAK),
	GYPSUM(Strength.WEAK),
	MIRABILITE(Strength.WEAK),
	MICA(Strength.WEAK),
	SOAPSTONE(Strength.WEAK),
	TRONA(Strength.WEAK),
	
	ALUNITE(Strength.MEDIUM),
	CELESTINE("strontium", Strength.MEDIUM),
	DOLOMITE(Strength.MEDIUM),
	MALACHITE("copper", Strength.MEDIUM),
	WOLLASTONITE(Strength.MEDIUM),
	ZEOLITE(Strength.MEDIUM),
	
	APATITE(Strength.STRONG),
	KYANITE(Strength.STRONG),
	PERLITE(Strength.STRONG),
	PUMICE(Strength.STRONG);
    
	public enum Strength { 
		WEAK, MEDIUM, STRONG, VERY_STRONG;
		
		public String getCamelName() {
			if (this == VERY_STRONG)
				return "veryStrong";
			else return name().toLowerCase();
		}
	}

	public enum Composition {
		AGGREGATE, ORE, PURE
	}
	
	private Strength strength;
	private Material material;
	private Composition composition;
	private String oreDictKey;
	private int meltingPoint;
	
	GeoSubstance(String oreDictKey, Strength strength, Composition composition, Material material) {
		this.strength = strength;
		this.material = material;
		this.composition = composition;
		this.oreDictKey = oreDictKey;
	}
	
	GeoSubstance(String oreDictKey, Strength strength, Composition composition) {
		this(oreDictKey, strength, composition, Material.rock);
	}
	
	GeoSubstance(Strength strength, Composition composition) {
		this(null, strength, composition, Material.rock);
	}
	
	GeoSubstance(String oreDictKey, Strength strength, Material material) {
		this(oreDictKey, strength, Composition.ORE, material);
	}
	
	GeoSubstance(Strength strength, Material material) {
		this(null, strength, material);
	}
	
	GeoSubstance(String oreDictKey, Strength strength) {
		this(oreDictKey, strength, Composition.ORE);
	}
	
	GeoSubstance(Strength strength) {
		this(null, strength);
	}
	
	public int getId() {
		return ordinal();
	}
	
	public Composition getComposition() {
		return composition;
	}

	public Strength getStrength() {
		return strength;
	}

	public Material getMaterial() {
		return material;
	}

	public String getLowerName() {
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
	}

	public String getOreDictKey() {
		return oreDictKey;
	}
	
	public static GeoSubstance getForId(int id) {
		return values()[id];
	}
	
	public static List<GeoSubstance> lookup(Strength strength, Composition composition, Material material) {
		List<GeoSubstance> substancesToReturn = new ArrayList<GeoSubstance>();
		for (GeoSubstance substance : values()) {
			if (substance.material == material && substance.strength == strength && substance.composition == composition)
				substancesToReturn.add(substance);
		}
		return substancesToReturn;
	}
	
}
