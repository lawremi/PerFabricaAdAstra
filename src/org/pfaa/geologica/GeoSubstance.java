package org.pfaa.geologica;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;

import org.pfaa.chemica.Chemical;

import com.google.common.base.CaseFormat;

public enum GeoSubstance {
	
	BRECCIA(Strength.WEAK, Composition.ROCK),
	CARBONATITE(Strength.WEAK, Composition.ROCK),
	CLAYSTONE(Strength.WEAK, Composition.ROCK),
	CONGLOMERATE(Strength.WEAK, Composition.ROCK),
	MUDSTONE(Strength.WEAK, Composition.ROCK),
	
	LIMESTONE(Strength.MEDIUM, Composition.ROCK),
	SCHIST(Strength.MEDIUM, Composition.ROCK),
	SERPENTINITE(Strength.MEDIUM, Composition.ROCK),
	SLATE(Strength.MEDIUM, Composition.ROCK),
	SKARN(Strength.MEDIUM, Composition.ROCK),
	
	ANDESITE(Strength.STRONG, Composition.ROCK),
	BASALT(Strength.STRONG, Composition.ROCK),
	GNEISS(Strength.STRONG, Composition.ROCK),
	GRANITE(Strength.STRONG, Composition.ROCK),
	GREENSCHIST(Strength.STRONG, Composition.ROCK),
	MARBLE(Strength.STRONG, Composition.ROCK),
	PEGMATITE(Strength.STRONG, Composition.ROCK),
	RHYOLITE(Strength.STRONG, Composition.ROCK),
	
	DIORITE(Strength.VERY_STRONG, Composition.ROCK),
	GABBRO(Strength.VERY_STRONG, Composition.ROCK),
	HORNFELS(Strength.VERY_STRONG, Composition.ROCK),
	PERIDOTITE(Strength.VERY_STRONG, Composition.ROCK),
	QUARTZITE(Strength.VERY_STRONG, Composition.ROCK),
	
	BASALTIC_MINERAL_SAND(Strength.WEAK, Material.sand),
	GARNET_SAND(Strength.WEAK, Material.sand),
	GRANITIC_MINERAL_SAND(Strength.WEAK, Material.sand),
	QUARTZ_SAND(Strength.WEAK, Material.sand),
	VOLCANIC_ASH(Strength.WEAK, Material.sand),
	
	LATERITE(Strength.WEAK, Composition.ROCK, Material.clay),
	
	BAUXITE(Strength.WEAK, Material.clay),
	BENTONITE(Strength.WEAK, Material.clay),
	KAOLINITE(Strength.WEAK, Material.clay),
	BROWN_LIMONITE(Strength.WEAK, Material.clay),
	YELLOW_LIMONITE(Strength.WEAK, Material.clay),
	VERMICULITE(Strength.WEAK, Material.clay),
	
	BORAX(Strength.WEAK),
	CINNABAR(Strength.WEAK),
	GALENA(Strength.WEAK),
	MOLYBDENITE(Strength.WEAK),
	ROCK_SALT(Strength.WEAK),
	STIBNITE(Strength.WEAK),
	
	BARITE(Strength.MEDIUM),
	BASTNASITE(Strength.MEDIUM),
	CHALCOPYRITE(Strength.MEDIUM),
	GARNIERITE(Strength.MEDIUM),
	LEPIDOLITE(Strength.MEDIUM),
	MAGNESITE(Strength.MEDIUM),
	PENTLANDITE(Strength.MEDIUM),
	SCHEELITE(Strength.MEDIUM),
	SPHALERITE(Strength.MEDIUM),
	WOLFRAMITE(Strength.MEDIUM),
	
	BANDED_IRON(Strength.STRONG),
        BERYL(Strength.STRONG),
	CASSITERITE(Strength.STRONG),
	CHROMITE(Strength.STRONG),
	ILMENITE(Strength.STRONG),
	MAGNETITE(Strength.STRONG),
	POLLUCITE(Strength.STRONG),
	PYROLUSITE(Strength.STRONG),
        SPODUMENE(Strength.STRONG),
	TANTALITE(Strength.STRONG),
	URANINITE(Strength.STRONG),
	VANADIUM_MAGNETITE(Strength.STRONG),
	
	CHRYSOTILE(Strength.WEAK),
	DIATOMITE(Strength.WEAK),
	GLAUCONITE(Strength.WEAK),
	GRAPHITE(Strength.WEAK),
	GYPSUM(Strength.WEAK),
	MIRABILITE(Strength.WEAK),
	MICA(Strength.WEAK),
        SOAPSTONE(Strength.WEAK),
	SODA_ASH(Strength.WEAK),
	
	ALUNITE(Strength.MEDIUM),
	DOLOMITE(Strength.MEDIUM),
	KYANITE(Strength.MEDIUM),
	MALACHITE(Strength.MEDIUM),
	WOLLASTONITE(Strength.MEDIUM),
	ZEOLITE(Strength.MEDIUM),
	
	APATITE(Strength.STRONG),
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
		ROCK, ORE, MINERAL
	}
	
	private Strength strength;
	private Material material;
	private Composition composition;
	
	GeoSubstance(Strength strength, Composition composition, Material material) {
		this.strength = strength;
		this.material = material;
		this.composition = composition;
	}
	
	GeoSubstance(Strength strength, Composition composition) {
		this(strength, composition, Material.rock);
	}
	
	GeoSubstance(Strength strength, Material material) {
		this(strength, Composition.ORE, material);
	}
	
	GeoSubstance(Strength strength) {
		this(strength, Composition.ORE);
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

	public static GeoSubstance getForId(int id) {
		return values()[id];
	}
	
	public static List<GeoSubstance> lookup(Strength strength, Composition substanceType, Material material) {
		List<GeoSubstance> substancesToReturn = new ArrayList<GeoSubstance>();
		for (GeoSubstance substance : values()) {
			if (substance.material == material && substance.strength == strength && substance.composition == substanceType)
				substancesToReturn.add(substance);
		}
		return substancesToReturn;
	}
}
