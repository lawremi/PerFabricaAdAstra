package org.pfaa.geologica;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;

import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.PhaseProperties;
import org.pfaa.geologica.processing.Aggregate.Aggregates;
import org.pfaa.geologica.processing.Crude.Crudes;
import org.pfaa.geologica.processing.IndustrialMineral;
import org.pfaa.geologica.processing.IndustrialMineral.IndustrialMinerals;
import org.pfaa.geologica.processing.OreMineral;
import org.pfaa.geologica.processing.OreMineral.Ores;
import org.pfaa.geologica.processing.SimpleCrude;
import org.pfaa.geologica.processing.SimpleOre;

import com.google.common.base.CaseFormat;

public enum GeoMaterial implements Mixture {
	BRECCIA(Aggregates.GRAVEL, Strength.WEAK),
	CARBONATITE(Aggregates.STONE.mix(Ores.CALCITE, 0.5), Strength.WEAK),
	CLAYSTONE(Aggregates.STONE, Strength.WEAK),
	CONGLOMERATE(Aggregates.SAND.mix(Aggregates.GRAVEL, 1.0), Strength.WEAK),
	MUDSTONE(Aggregates.STONE, Strength.WEAK),
	
	LIMESTONE(Aggregates.STONE.mix(Ores.CALCITE, 0.5), Strength.MEDIUM),
	SCHIST(Aggregates.STONE, Strength.MEDIUM),
	SERPENTINITE(Aggregates.STONE.mix(IndustrialMinerals.CHRYSOTILE, 0.05)
			     .mix(IndustrialMinerals.TALC, 0.05).mix(IndustrialMinerals.OLIVINE, 0.05), Strength.MEDIUM),
	SLATE(Aggregates.STONE, Strength.MEDIUM),
	SKARN(Aggregates.STONE, Strength.MEDIUM),
	
	ANDESITE(Aggregates.STONE, Strength.STRONG),
	BASALT(Aggregates.STONE, Strength.STRONG),
	GNEISS(Aggregates.STONE, Strength.STRONG),
	GRANITE(Aggregates.STONE, Strength.STRONG),
	GREENSCHIST(Aggregates.STONE, Strength.STRONG),
	MARBLE(Aggregates.STONE.mix(Ores.CALCITE, 1.0), Strength.STRONG),
	PEGMATITE(Aggregates.STONE.mix(IndustrialMinerals.FELDSPAR, 0.5)
			  .mix(IndustrialMinerals.QUARTZ, 0.2).mix(IndustrialMinerals.MICA, 0.2), Strength.STRONG),
	RHYOLITE(Aggregates.STONE, Strength.STRONG),
	
	DIORITE(Aggregates.STONE, Strength.VERY_STRONG),
	GABBRO(Aggregates.STONE, Strength.VERY_STRONG),
	HORNFELS(Aggregates.STONE, Strength.VERY_STRONG),
	PERIDOTITE(Aggregates.STONE.mix(IndustrialMinerals.OLIVINE, 0.5), Strength.VERY_STRONG),
	QUARTZITE(Aggregates.SAND, Strength.VERY_STRONG),
	
	BASALTIC_MINERAL_SAND(Ores.MAGNETITE.mix(Aggregates.SAND, 0.4).mix(IndustrialMinerals.GARNET, 1.4)
					      .mix(Ores.CHROMITE, 0.2).mix(Ores.ILMENITE, 0.6).mix(Ores.RUTILE, 0.2).mix(Ores.ZIRCON, 0.2),
	                      Strength.WEAK, Material.sand),
	CASSITERITE_SAND(Ores.CASSITERITE, Strength.WEAK, Material.sand),
	GARNET_SAND(IndustrialMinerals.GARNET, Strength.WEAK, Material.sand),
	GRANITIC_MINERAL_SAND(Ores.MAGNETITE.mix(Aggregates.SAND, 1.4).mix(IndustrialMinerals.KYANITE, 0.2)
			              .mix(Ores.ILMENITE, 0.4).mix(Ores.RUTILE, 0.6).mix(Ores.ZIRCON, 0.4).mix(Ores.MONAZITE, 0.4), 
			              Strength.WEAK, Material.sand),
	QUARTZ_SAND(IndustrialMinerals.QUARTZ, Strength.WEAK, Material.sand),
	VOLCANIC_ASH(IndustrialMinerals.VOLCANIC_ASH, Strength.WEAK, Material.sand),
	
	LATERITE(Aggregates.CLAY, Strength.WEAK, Material.clay),
	
	// FIXME: add niter (KNO3)? 
	BAUXITE(Ores.GIBBSITE.mix(Ores.HEMATITE, 0.45).mix(Ores.CALCITE, 0.10)
			.mix(IndustrialMinerals.KAOLINITE, 0.10).mix(IndustrialMinerals.QUARTZ, 0.05).mix(Ores.ANATASE, 0.05), 
			Strength.WEAK, Material.clay),
	BENTONITE(IndustrialMinerals.BENTONITE, Strength.WEAK, Material.clay),
	FULLERS_EARTH(IndustrialMinerals.FULLERS_EARTH, Strength.WEAK, Material.clay),
	KAOLINITE(IndustrialMinerals.KAOLINITE, Strength.WEAK, Material.clay),
	BROWN_LIMONITE(Ores.LEPIDOCROCITE.mix(Aggregates.CLAY, 1.0), Strength.WEAK, Material.clay),
	YELLOW_LIMONITE(Ores.GOETHITE.mix(Aggregates.CLAY, 1.0), Strength.WEAK, Material.clay),
	VERMICULITE(IndustrialMinerals.VERMICULITE, Strength.WEAK, Material.clay),
	
	// FIXME: should we add stone to all of the stone ores?
	BORAX(Ores.BORAX, Strength.WEAK),
	CINNABAR(Ores.CINNABAR.mix(Ores.PYRITE, 0.05).mix(Ores.REALGAR, 0.04).mix(Ores.STIBNITE, 0.02).mix(Ores.BARITE, 0.02), 
			 Strength.WEAK),
	GALENA(Ores.GALENA.mix(Ores.SPHALERITE, 0.2).mix(Ores.ACANTHITE, 0.05).mix(Ores.FLUORITE, 0.05).mix(Ores.BISMUTHINITE, 0.05)
		   .mix(Ores.REALGAR, 0.04).mix(Ores.STIBNITE, 0.02).mix(Ores.GREENOCKITE, 0.01).mix(Ores.VANADINITE, 0.01), Strength.WEAK),
	MOLYBDENITE(Ores.MOLYBDENITE.mix(Ores.PYRITE, 0.05).mix(Ores.CHALCOPYRITE, 0.05)
			    .mix(Ores.FLUORITE, 0.02), Strength.WEAK),
	PYROLUSITE(Ores.PYROLUSITE.mix(Ores.GOETHITE, 0.1), Strength.WEAK),
	ROCK_SALT(Ores.HALITE.mix(Ores.SYLVITE, 0.5).mix(Ores.CARNALLITE, 0.1).mix(Ores.GYPSUM, 0.05), Strength.WEAK),
	STIBNITE(Ores.STIBNITE.mix(Ores.PYRITE, 0.05).mix(Ores.GALENA, 0.05).mix(Ores.REALGAR, 0.04).mix(Ores.CINNABAR, 0.02), 
			 Strength.WEAK),
	
	BARITE(Ores.BARITE, Strength.MEDIUM),
	BASTNASITE(Ores.BASTNASITE, Strength.MEDIUM),
	CHALCOPYRITE(Ores.CHALCOPYRITE.mix(Ores.PYRITE, 0.10).mix(Ores.MOLYBDENITE, 0.05)
				 .mix(Ores.COBALTITE, 0.01), Strength.MEDIUM),
	GARNIERITE(Ores.NEPOUITE.mix(SERPENTINITE, 1.0), Strength.MEDIUM),
	LEPIDOLITE(Ores.LEPIDOLITE.mix(PEGMATITE, 1.0).mix(Ores.SPODUMENE, 0.2), Strength.MEDIUM),
	MAGNESITE(Ores.MAGNESITE.mix(IndustrialMinerals.TALC, 0.2), Strength.MEDIUM),
	PENTLANDITE(Ores.PENTLANDITE.mix(Ores.PYRITE, 0.1), Strength.MEDIUM),
	SCHEELITE(Ores.SCHEELITE.mix(Ores.CASSITERITE, 0.2).mix(Ores.WOLFRAMITE, 0.2), Strength.MEDIUM),
	SPHALERITE(Ores.SPHALERITE.mix(Ores.GALENA, 0.2).mix(Ores.PYRITE, 0.1), Strength.MEDIUM),
	WOLFRAMITE(Ores.WOLFRAMITE.mix(Ores.CASSITERITE, 0.2).mix(Ores.SCHEELITE, 0.2), Strength.MEDIUM),
	
	BANDED_IRON(Ores.HEMATITE.mix(Ores.MAGNETITE, 0.5), Strength.STRONG),
	BERYL(Ores.BERYL.mix(PEGMATITE, 1.0), Strength.STRONG),
	CASSITERITE(Ores.CASSITERITE.mix(Ores.FLUORITE, 0.1).mix(Ores.WOLFRAMITE, 0.1)
			    .mix(IndustrialMinerals.APATITE, 0.05).mix(Ores.MOLYBDENITE, 0.05), Strength.STRONG),
	CHROMITE(Ores.CHROMITE.mix(SERPENTINITE, 0.5).mix(Ores.MAGNETITE, 0.1), Strength.STRONG),
	ILMENITE(Ores.ILMENITE.mix(Ores.RUTILE, 0.2).mix(Ores.MAGNETITE, 0.1), Strength.STRONG),
	MAGNETITE(Ores.MAGNETITE, Strength.STRONG),
	POLLUCITE(Ores.POLLUCITE.mix(PEGMATITE, 1.0).mix(Ores.SPODUMENE, 0.1), Strength.STRONG),
	SPODUMENE(Ores.SPODUMENE.mix(PEGMATITE, 1.0).mix(Ores.LEPIDOLITE, 0.1), Strength.STRONG),
	TANTALITE(Ores.TANTALITE.mix(Ores.COLUMBITE, 2.0), Strength.STRONG),
	URANINITE(Ores.URANINITE.mix(Ores.CARNOTITE, 0.05), Strength.STRONG),
	VANADIUM_MAGNETITE(Ores.TITANO_MAGNETITE, Strength.STRONG),
	
	CHRYSOTILE(IndustrialMinerals.CHRYSOTILE.mix(SERPENTINITE, 0.5), Strength.WEAK),
	DIATOMITE(IndustrialMinerals.DIATOMITE, Strength.WEAK),
	GLAUCONITE(IndustrialMinerals.GLAUCONITE.mix(Aggregates.SAND, 1.0), Strength.WEAK),
	GRAPHITE(IndustrialMinerals.GRAPHITE.mix(IndustrialMinerals.COAL, 0.5), Strength.WEAK),
	GYPSUM(IndustrialMinerals.GYPSUM.mix(Ores.HALITE, 0.05), Strength.WEAK),
	MIRABILITE(IndustrialMinerals.MIRABILITE
	           .mix(IndustrialMinerals.GYPSUM, 0.2).mix(Ores.HALITE, 0.05), Strength.WEAK),
	MICA(IndustrialMinerals.MICA.mix(PEGMATITE, 0.5), Strength.WEAK),
	SOAPSTONE(IndustrialMinerals.TALC.mix(SERPENTINITE, 0.5), Strength.WEAK),
	TRONA(IndustrialMinerals.TRONA
          .mix(IndustrialMinerals.GYPSUM, 0.2).mix(Ores.HALITE, 0.05), Strength.WEAK),
	
	ALUNITE(IndustrialMinerals.ALUNITE.mix(IndustrialMinerals.BENTONITE, 0.1), Strength.MEDIUM),
	CELESTINE(Ores.CELESTINE.mix(IndustrialMinerals.GYPSUM, 0.2).mix(Ores.HALITE, 0.05), Strength.MEDIUM),
	DOLOMITE(IndustrialMinerals.DOLOMITE.mix(Ores.MAGNESITE, 0.05), Strength.MEDIUM),
	FLUORITE(Ores.FLUORITE.mix(Ores.SPHALERITE, 0.06).mix(Ores.GALENA, 0.02), Strength.MEDIUM),
	WOLLASTONITE(IndustrialMinerals.WOLLASTONITE.mix(Ores.CALCITE, 0.1), Strength.MEDIUM),
	ZEOLITE(IndustrialMinerals.ZEOLITE, Strength.MEDIUM),
	
	APATITE(IndustrialMinerals.APATITE, Strength.STRONG),
	KYANITE(IndustrialMinerals.KYANITE.mix(PEGMATITE, 0.5), Strength.STRONG),
	PERLITE(IndustrialMinerals.PERLITE.mix(IndustrialMinerals.OBSIDIAN, 0.1), Strength.STRONG),
	PUMICE(IndustrialMinerals.PUMICE, Strength.STRONG),
	
	LIGHT_OIL(new SimpleCrude(Crudes.PARAFFINS, 0.5).mix(Crudes.NAPHTHAS, 0.45).mix(Crudes.AROMATICS, 0.05).
			  mix(Compounds.H2S, 0.005), Strength.WEAK, Material.water),
	HEAVY_OIL(new SimpleCrude(Crudes.PARAFFINS, 0.3).mix(Crudes.NAPHTHAS, 0.5).mix(Crudes.AROMATICS, 0.15).
			  mix(Crudes.ASPHALT, 0.05).mix(Compounds.H2S, 0.01), Strength.MEDIUM, Material.water),
	EXTRA_HEAVY_OIL(new SimpleCrude(Crudes.PARAFFINS, 0.15).mix(Crudes.NAPHTHAS, 0.45).mix(Crudes.AROMATICS, 0.30).
			        mix(Crudes.ASPHALT, 0.10).mix(Compounds.H2S, 0.02), Strength.STRONG, Material.water),
	OIL_SANDS(Crudes.ASPHALT.mix(Aggregates.SAND, 1.0), Strength.VERY_STRONG, Material.sand),
	NATURAL_GAS(Compounds.METHANE.mix(Compounds.ETHANE, 0.05).mix(Compounds.PROPANE, 0.002).
			    mix(Compounds.N_BUTANE, 0.0003).mix(Compounds.ISO_BUTANE, 0.0003), 
			    Strength.WEAK, Material.air),
	OIL_SHALE(Crudes.KEROGEN.mix(MUDSTONE, 1.0).mix(Crudes.ASPHALT, 0.5), Strength.WEAK)
	;
    
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
	private Mixture composition;
		
	GeoMaterial(Mixture composition, Strength strength, Material blockMaterial) {
		this.strength = strength;
		this.blockMaterial = blockMaterial;
		this.composition = composition;
	}
	
	GeoMaterial(Mixture composition, Strength strength) {
		this(composition, strength, Material.rock);
	}
	
	GeoMaterial(OreMineral composition, Strength strength, Material blockMaterial) {
		this(new SimpleOre(composition), strength, blockMaterial);
	}
	
	GeoMaterial(OreMineral composition, Strength strength) {
		this(composition, strength, Material.rock);
	}
	
	GeoMaterial(IndustrialMineral composition, Strength strength, Material blockMaterial) {
		this(new SimpleOre(composition), strength, blockMaterial);
	}
	
	GeoMaterial(IndustrialMineral composition, Strength strength) {
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

	@Override
	public List<MixtureComponent> getComponents() {
		return composition.getComponents();
	}

	@Override
	public Mixture mix(IndustrialMaterial material, double weight) {
		return composition.mix(material, weight);
	}
}
