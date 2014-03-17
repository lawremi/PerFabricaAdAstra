package org.pfaa.geologica;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.PhaseProperties;
import org.pfaa.chemica.model.SimpleMixture;
import org.pfaa.geologica.processing.Aggregate.Aggregates;
import org.pfaa.geologica.processing.IndustrialMineral.IndustrialMinerals;
import org.pfaa.geologica.processing.Ore.Ores;
import org.pfaa.geologica.processing.SimpleOre;

import com.google.common.base.CaseFormat;

public enum GeoMaterial implements IndustrialMaterial {
	BRECCIA(Aggregates.GRAVEL, Strength.WEAK),
	CARBONATITE(Aggregates.STONE.add(Compounds.CaCO3, 0.5), Strength.WEAK),
	CLAYSTONE(Aggregates.STONE, Strength.WEAK),
	CONGLOMERATE(Aggregates.SAND.add(Aggregates.GRAVEL, 1.0), Strength.WEAK),
	MUDSTONE(Aggregates.STONE, Strength.WEAK),
	
	LIMESTONE(Aggregates.STONE.add(Compounds.CaCO3, 0.5), Strength.MEDIUM),
	SCHIST(Aggregates.STONE, Strength.MEDIUM),
	SERPENTINITE(Aggregates.STONE, Strength.MEDIUM),
	SLATE(Aggregates.STONE, Strength.MEDIUM),
	SKARN(Aggregates.STONE, Strength.MEDIUM),
	
	ANDESITE(Aggregates.STONE, Strength.STRONG),
	BASALT(Aggregates.STONE, Strength.STRONG),
	GNEISS(Aggregates.STONE, Strength.STRONG),
	GRANITE(Aggregates.STONE, Strength.STRONG),
	GREENSCHIST(Aggregates.STONE, Strength.STRONG),
	MARBLE(Aggregates.STONE.add(Compounds.CaCO3, 1.0), Strength.STRONG),
	PEGMATITE(Aggregates.STONE.add(IndustrialMinerals.FELDSPAR, 0.5), Strength.STRONG),
	RHYOLITE(Aggregates.STONE, Strength.STRONG),
	
	DIORITE(Aggregates.STONE, Strength.VERY_STRONG),
	GABBRO(Aggregates.STONE, Strength.VERY_STRONG),
	HORNFELS(Aggregates.STONE, Strength.VERY_STRONG),
	PERIDOTITE(Aggregates.STONE.add(IndustrialMinerals.OLIVINE, 0.5), Strength.VERY_STRONG),
	QUARTZITE(Aggregates.SAND, Strength.VERY_STRONG),
	
	BASALTIC_MINERAL_SAND(Ores.MAGNETITE.add(Aggregates.SAND, 0.4).add(IndustrialMinerals.GARNET, 1.4)
					      .add(Ores.CHROMITE, 0.2).add(Ores.ILMENITE, 0.6).add(Ores.RUTILE, 0.2).add(Ores.ZIRCON, 0.2),
	                      Strength.WEAK, Material.sand),
	CASSITERITE_SAND(Ores.CASSITERITE, Strength.WEAK, Material.sand),
	GARNET_SAND(IndustrialMinerals.GARNET, Strength.WEAK, Material.sand),
	GRANITIC_MINERAL_SAND(Ores.MAGNETITE.add(Aggregates.SAND, 1.4).add(IndustrialMinerals.KYANITE, 0.2)
			              .add(Ores.ILMENITE, 0.4).add(Ores.RUTILE, 0.6).add(Ores.ZIRCON, 0.4).add(Ores.MONAZITE, 0.4), 
			              Strength.WEAK, Material.sand),
	QUARTZ_SAND(IndustrialMinerals.QUARTZ, Strength.WEAK, Material.sand),
	VOLCANIC_ASH(IndustrialMinerals.VOLCANIC_ASH, Strength.WEAK, Material.sand),
	
	LATERITE(Aggregates.CLAY, Strength.WEAK, Material.clay),
	
	// FIXME: add niter (KNO3)? 
	BAUXITE(Ores.GIBBSITE.add(Ores.HEMATITE, 0.45).add(Ores.CALCITE, 0.10)
			.add(IndustrialMinerals.KAOLINITE, 0.10).add(IndustrialMinerals.QUARTZ, 0.05).add(Ores.ANATASE, 0.05), 
			Strength.WEAK, Material.clay),
	BENTONITE(IndustrialMinerals.BENTONITE, Strength.WEAK, Material.clay),
	FULLERS_EARTH(IndustrialMinerals.FULLERS_EARTH, Strength.WEAK, Material.clay),
	KAOLINITE(IndustrialMinerals.KAOLINITE, Strength.WEAK, Material.clay),
	BROWN_LIMONITE(Ores.LEPIDOCROCITE.add(Aggregates.CLAY, 1.0), Strength.WEAK, Material.clay),
	YELLOW_LIMONITE(Ores.GOETHITE.add(Aggregates.CLAY, 1.0), Strength.WEAK, Material.clay),
	VERMICULITE(IndustrialMinerals.VERMICULITE, Strength.WEAK, Material.clay),
	
	// FIXME: should we add stone to all of the stone ores?
	BORAX(Ores.BORAX, Strength.WEAK),
	CINNABAR(Ores.CINNABAR.add(Ores.PYRITE, 0.05).add(Ores.REALGAR, 0.04).add(Ores.STIBNITE, 0.02).add(Ores.BARITE, 0.02), 
			 Strength.WEAK),
	GALENA(Ores.GALENA.add(Ores.SPHALERITE, 0.2).add(Ores.ACANTHITE, 0.05).add(Ores.FLUORITE, 0.05).add(Ores.BISMUTHINITE, 0.05)
		   .add(Ores.REALGAR, 0.04).add(Ores.STIBNITE, 0.02).add(Ores.GREENOCKITE, 0.01), Strength.WEAK),
	MOLYBDENITE(Ores.MOLYBDENITE.add(Ores.PYRITE, 0.05).add(Ores.CHALCOPYRITE, 0.05)
			    .add(Ores.FLUORITE, 0.02), Strength.WEAK),
	PYROLUSITE(Ores.PYROLUSITE.add(Ores.GOETHITE, 0.1), Strength.WEAK),
	ROCK_SALT(Ores.HALITE.add(Ores.SYLVITE, 0.5).add(Ores.CARNALLITE, 0.1).add(Ores.GYPSUM, 0.05), Strength.WEAK),
	STIBNITE(Ores.STIBNITE.add(Ores.PYRITE, 0.05).add(Ores.GALENA, 0.05).add(Ores.REALGAR, 0.04).add(Ores.CINNABAR, 0.02), 
			 Strength.WEAK),
	
	BARITE(Ores.BARITE.add(Ores.CELESTINE, 0.05), Strength.MEDIUM),
	BASTNASITE(Strength.MEDIUM),
	CHALCOPYRITE("copper", Strength.MEDIUM),
	GARNIERITE("nickel", Strength.MEDIUM),
	LEPIDOLITE(Strength.MEDIUM),
	MAGNESITE("magnesium", Strength.MEDIUM),
	PENTLANDITE("nickel", Strength.MEDIUM),
	SCHEELITE("tungstate", Strength.MEDIUM),
	SPHALERITE("zinc", Strength.MEDIUM),
	WOLFRAMITE("tungstate", Strength.MEDIUM),
	
	BANDED_IRON("iron", Strength.STRONG),
	BERYL(Strength.STRONG),
	CASSITERITE("tin", Strength.STRONG),
	CHROMITE("chromium", Strength.STRONG),
	ILMENITE(Strength.STRONG),
	MAGNETITE("iron", Strength.STRONG),
	POLLUCITE(Strength.STRONG),
	SPODUMENE(Strength.STRONG),
	TANTALITE(Strength.STRONG),
	URANINITE(Strength.STRONG),
	VANADIUM_MAGNETITE(Strength.STRONG),
	
	CHRYSOTILE("asbestos", Strength.WEAK),
	DIATOMITE(Strength.WEAK),
	GLAUCONITE(Strength.WEAK),
	GRAPHITE(Strength.WEAK),
	GYPSUM(Strength.WEAK),
	MIRABILITE(Strength.WEAK),
	MICA(Strength.WEAK),
	SOAPSTONE("talc", Strength.WEAK),
	TRONA(Strength.WEAK),
	
	ALUNITE(Strength.MEDIUM),
	CELESTINE("strontium", Strength.MEDIUM),
	DOLOMITE(Strength.MEDIUM),
	FLUORITE(Ores.FLUORITE, Strength.MEDIUM),
	WOLLASTONITE(Strength.MEDIUM),
	ZEOLITE(Strength.MEDIUM),
	
	APATITE(Strength.STRONG),
	KYANITE(Strength.STRONG),
	PERLITE(Strength.STRONG),
	PUMICE(Strength.STRONG);
    
	static Mixture ROCK = new SimpleMixture(Aggregates.STONE);
	
	public enum Strength { 
		WEAK, MEDIUM, STRONG, VERY_STRONG;
		
		public String getCamelName() {
			if (this == VERY_STRONG)
				return "veryStrong";
			else return name().toLowerCase();
		}
	}

	private Strength strength;
	private Material blockMaterial;
	private IndustrialMaterial composition;
		
	GeoMaterial(IndustrialMaterial composition, Strength strength, Material blockMaterial) {
		this.strength = strength;
		this.blockMaterial = blockMaterial;
		this.composition = composition;
	}
	
	GeoMaterial(IndustrialMaterial composition, Strength strength) {
		this(composition, strength, Material.rock);
	}
	
	public int getId() {
		return ordinal();
	}
	
	public Strength getStrength() {
		return strength;
	}

	public Material getBlockMaterial() {
		return blockMaterial;
	}

	public String getLowerName() {
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
	}

	public String getOreDictKey() {
		return composition.getOreDictKey();
	}
	
	public static GeoMaterial getForId(int id) {
		return values()[id];
	}
	
	public IndustrialMaterial getComposition() {
		return this.composition;
	}

	public static List<GeoMaterial> lookup(Strength strength, Class<? extends IndustrialMaterial> compositionType, Material blockMaterial) {
		List<GeoMaterial> materialsToReturn = new ArrayList<GeoMaterial>();
		for (GeoMaterial material : values()) {
			if (material.blockMaterial == blockMaterial && material.strength == strength && 
				  compositionType.isAssignableFrom(material.composition.getClass()))
				materialsToReturn.add(material);
		}
		return materialsToReturn;
	}

	@Override
	public PhaseProperties getProperties(Phase phase) {
		return composition.getProperties(phase);
	}	
}
