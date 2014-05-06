package org.pfaa.geologica;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.PhaseProperties;
import org.pfaa.chemica.model.SimpleMixture;
import org.pfaa.geologica.processing.Aggregate.Aggregates;
import org.pfaa.geologica.processing.IndustrialMineral.IndustrialMinerals;
import org.pfaa.geologica.processing.OreMineral.Ores;
import org.pfaa.geologica.processing.SimpleOreMineral;

import com.google.common.base.CaseFormat;

public enum GeoMaterial implements Mixture {
	BRECCIA(Aggregates.GRAVEL, Strength.WEAK),
	CARBONATITE(Aggregates.STONE.add(Ores.CALCITE, 0.5), Strength.WEAK),
	CLAYSTONE(Aggregates.STONE, Strength.WEAK),
	CONGLOMERATE(Aggregates.SAND.add(Aggregates.GRAVEL, 1.0), Strength.WEAK),
	MUDSTONE(Aggregates.STONE, Strength.WEAK),
	
	LIMESTONE(Aggregates.STONE.add(Ores.CALCITE, 0.5), Strength.MEDIUM),
	SCHIST(Aggregates.STONE, Strength.MEDIUM),
	SERPENTINITE(Aggregates.STONE.add(IndustrialMinerals.CHRYSOTILE, 0.05)
			     .add(IndustrialMinerals.TALC, 0.05).add(IndustrialMinerals.OLIVINE, 0.05), Strength.MEDIUM),
	SLATE(Aggregates.STONE, Strength.MEDIUM),
	SKARN(Aggregates.STONE, Strength.MEDIUM),
	
	ANDESITE(Aggregates.STONE, Strength.STRONG),
	BASALT(Aggregates.STONE, Strength.STRONG),
	GNEISS(Aggregates.STONE, Strength.STRONG),
	GRANITE(Aggregates.STONE, Strength.STRONG),
	GREENSCHIST(Aggregates.STONE, Strength.STRONG),
	MARBLE(Aggregates.STONE.add(Ores.CALCITE, 1.0), Strength.STRONG),
	PEGMATITE(Aggregates.STONE.add(IndustrialMinerals.FELDSPAR, 0.5)
			  .add(IndustrialMinerals.QUARTZ, 0.2).add(IndustrialMinerals.MICA, 0.2), Strength.STRONG),
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
		   .add(Ores.REALGAR, 0.04).add(Ores.STIBNITE, 0.02).add(Ores.GREENOCKITE, 0.01).add(Ores.VANADINITE, 0.01), Strength.WEAK),
	MOLYBDENITE(Ores.MOLYBDENITE.add(Ores.PYRITE, 0.05).add(Ores.CHALCOPYRITE, 0.05)
			    .add(Ores.FLUORITE, 0.02), Strength.WEAK),
	PYROLUSITE(Ores.PYROLUSITE.add(Ores.GOETHITE, 0.1), Strength.WEAK),
	ROCK_SALT(Ores.HALITE.add(Ores.SYLVITE, 0.5).add(Ores.CARNALLITE, 0.1).add(Ores.GYPSUM, 0.05), Strength.WEAK),
	STIBNITE(Ores.STIBNITE.add(Ores.PYRITE, 0.05).add(Ores.GALENA, 0.05).add(Ores.REALGAR, 0.04).add(Ores.CINNABAR, 0.02), 
			 Strength.WEAK),
	
	BARITE(Ores.BARITE, Strength.MEDIUM),
	BASTNASITE(Ores.BASTNASITE, Strength.MEDIUM),
	CHALCOPYRITE(Ores.CHALCOPYRITE.add(Ores.PYRITE, 0.10).add(Ores.MOLYBDENITE, 0.05)
				 .add(Ores.COBALTITE, 0.01), Strength.MEDIUM),
	GARNIERITE(Ores.NEPOUITE.add(SERPENTINITE, 1.0), Strength.MEDIUM),
	LEPIDOLITE(Ores.LEPIDOLITE.add(PEGMATITE, 1.0).add(Ores.SPODUMENE, 0.2), Strength.MEDIUM),
	MAGNESITE(Ores.MAGNESITE.add(IndustrialMinerals.TALC, 0.2), Strength.MEDIUM),
	PENTLANDITE(Ores.PENTLANDITE.add(Ores.PYRITE, 0.1), Strength.MEDIUM),
	SCHEELITE(Ores.SCHEELITE.add(Ores.CASSITERITE, 0.2).add(Ores.WOLFRAMITE, 0.2), Strength.MEDIUM),
	SPHALERITE(Ores.SPHALERITE.add(Ores.GALENA, 0.2).add(Ores.PYRITE, 0.1), Strength.MEDIUM),
	WOLFRAMITE(Ores.WOLFRAMITE.add(Ores.CASSITERITE, 0.2).add(Ores.SCHEELITE, 0.2), Strength.MEDIUM),
	
	BANDED_IRON(Ores.HEMATITE.add(Ores.MAGNETITE, 0.5), Strength.STRONG),
	BERYL(Ores.BERYL.add(PEGMATITE, 1.0), Strength.STRONG),
	CASSITERITE(Ores.CASSITERITE.add(Ores.FLUORITE, 0.1).add(Ores.WOLFRAMITE, 0.1)
			    .add(IndustrialMinerals.APATITE, 0.05).add(Ores.MOLYBDENITE, 0.05), Strength.STRONG),
	CHROMITE(Ores.CHROMITE.add(SERPENTINITE, 0.5).add(Ores.MAGNETITE, 0.1), Strength.STRONG),
	ILMENITE(Ores.ILMENITE.add(Ores.RUTILE, 0.2).add(Ores.MAGNETITE, 0.1), Strength.STRONG),
	MAGNETITE(Ores.MAGNETITE, Strength.STRONG),
	POLLUCITE(Ores.POLLUCITE.add(PEGMATITE, 1.0).add(Ores.SPODUMENE, 0.1), Strength.STRONG),
	SPODUMENE(Ores.SPODUMENE.add(PEGMATITE, 1.0).add(Ores.LEPIDOLITE, 0.1), Strength.STRONG),
	TANTALITE(Ores.TANTALITE.add(Ores.COLUMBITE, 2.0), Strength.STRONG),
	URANINITE(Ores.URANINITE.add(Ores.CARNOTITE, 0.05), Strength.STRONG),
	VANADIUM_MAGNETITE(Ores.TITANO_MAGNETITE, Strength.STRONG),
	
	CHRYSOTILE(IndustrialMinerals.CHRYSOTILE.mixWith(SERPENTINITE, 0.5), Strength.WEAK),
	DIATOMITE(IndustrialMinerals.DIATOMITE, Strength.WEAK),
	GLAUCONITE(IndustrialMinerals.GLAUCONITE.mixWith(Aggregates.SAND, 1.0), Strength.WEAK),
	GRAPHITE(IndustrialMinerals.GRAPHITE.mixWith(IndustrialMinerals.COAL, 0.5), Strength.WEAK),
	GYPSUM(IndustrialMinerals.GYPSUM.mixWith(Ores.HALITE, 0.05), Strength.WEAK),
	MIRABILITE(IndustrialMinerals.MIRABILITE
	           .mixWith(IndustrialMinerals.GYPSUM, 0.2).add(Ores.HALITE, 0.05), Strength.WEAK),
	MICA(IndustrialMinerals.MICA.mixWith(PEGMATITE, 0.5), Strength.WEAK),
	SOAPSTONE(IndustrialMinerals.TALC.mixWith(SERPENTINITE, 0.5), Strength.WEAK),
	TRONA(IndustrialMinerals.TRONA
          .mixWith(IndustrialMinerals.GYPSUM, 0.2).add(Ores.HALITE, 0.05), Strength.WEAK),
	
	ALUNITE(IndustrialMinerals.ALUNITE.mixWith(IndustrialMinerals.BENTONITE, 0.1), Strength.MEDIUM),
	CELESTINE(Ores.CELESTINE.add(IndustrialMinerals.GYPSUM, 0.2).add(Ores.HALITE, 0.05), Strength.MEDIUM),
	DOLOMITE(IndustrialMinerals.DOLOMITE.mixWith(Ores.MAGNESITE, 0.05), Strength.MEDIUM),
	FLUORITE(Ores.FLUORITE.add(Ores.SPHALERITE, 0.06).add(Ores.GALENA, 0.02), Strength.MEDIUM),
	WOLLASTONITE(IndustrialMinerals.WOLLASTONITE.mixWith(Ores.CALCITE, 0.1), Strength.MEDIUM),
	ZEOLITE(IndustrialMinerals.ZEOLITE, Strength.MEDIUM),
	
	APATITE(IndustrialMinerals.APATITE, Strength.STRONG),
	KYANITE(IndustrialMinerals.KYANITE.mixWith(PEGMATITE, 0.5), Strength.STRONG),
	PERLITE(IndustrialMinerals.PERLITE.mixWith(IndustrialMinerals.OBSIDIAN, 0.1), Strength.STRONG),
	PUMICE(IndustrialMinerals.PUMICE, Strength.STRONG);
    
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
	private Mixture composition;
		
	GeoMaterial(Mixture composition, Strength strength, Material blockMaterial) {
		this.strength = strength;
		this.blockMaterial = blockMaterial;
		this.composition = composition;
	}
	
	GeoMaterial(Mixture composition, Strength strength) {
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
	public Mixture add(IndustrialMaterial material, double weight) {
		return composition.add(material, weight);
	}
}
