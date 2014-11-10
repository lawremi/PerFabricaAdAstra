package org.pfaa.geologica; // FIXME: should probably be in blocks package

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;

import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.geologica.processing.Aggregate.Aggregates;
import org.pfaa.geologica.processing.Crude.Crudes;
import org.pfaa.geologica.processing.IndustrialMineral;
import org.pfaa.geologica.processing.IndustrialMineral.IndustrialMinerals;
import org.pfaa.geologica.processing.OreMineral;
import org.pfaa.geologica.processing.OreMineral.Ores;
import org.pfaa.geologica.processing.SimpleCrude;
import org.pfaa.geologica.processing.SimpleOre;
import org.pfaa.geologica.processing.SimpleVanillaOre;

import com.google.common.base.CaseFormat;

public enum GeoMaterial implements Mixture {
	BRECCIA(Aggregates.GRAVEL, Strength.WEAK),
	CARBONATITE(Aggregates.STONE.mix(Ores.CALCITE, 0.5).mix(Ores.PYROCHLORE, 0.02).mix(Ores.MICROLITE, 0.005), Strength.WEAK),
	CLAYSTONE(Aggregates.STONE, Strength.WEAK),
	CONGLOMERATE(Aggregates.SAND.mix(Aggregates.GRAVEL, 1.0), Strength.WEAK),
	MUDSTONE(Aggregates.STONE, Strength.WEAK),
	
	LIMESTONE(Aggregates.STONE.mix(Ores.CALCITE, 0.5), Strength.MEDIUM),
	// TODO: yield mica?
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
			  .mix(IndustrialMinerals.QUARTZ, 0.2).mix(IndustrialMinerals.MICA, 0.2).mix(Ores.MICROLITE, 0.02), 
			  Strength.STRONG),
	RHYOLITE(Aggregates.STONE, Strength.STRONG),
	SANDSTONE(Aggregates.STONE, Strength.STRONG),
	
	DIORITE(Aggregates.STONE, Strength.VERY_STRONG),
	GABBRO(Aggregates.STONE, Strength.VERY_STRONG),
	HORNFELS(Aggregates.STONE, Strength.VERY_STRONG),
	PERIDOTITE(Aggregates.STONE.mix(IndustrialMinerals.OLIVINE, 0.5), Strength.VERY_STRONG),
	QUARTZITE(Aggregates.SAND, Strength.VERY_STRONG),

	LIGHT_OIL(new SimpleCrude(Crudes.FUEL_GAS, 0.1).mix(Crudes.LIGHT_NAPHTHA, 0.35).mix(Crudes.HEAVY_NAPHTHA, 0.25).
			  mix(Crudes.KEROSENE, 0.1).mix(Crudes.LIGHT_GAS_OIL, 0.05).mix(Crudes.HEAVY_GAS_OIL, 0.05).
			  mix(Crudes.BITUMEN, 0.10), Strength.WEAK, Material.water),
	MEDIUM_OIL(new SimpleCrude(Crudes.FUEL_GAS, 0.05).mix(Crudes.LIGHT_NAPHTHA, 0.15).mix(Crudes.HEAVY_NAPHTHA, 0.30).
		 	   mix(Crudes.KEROSENE, 0.15).mix(Crudes.LIGHT_GAS_OIL, 0.1).mix(Crudes.HEAVY_GAS_OIL, 0.05).
			   mix(Crudes.BITUMEN, 0.2), Strength.MEDIUM, Material.water),
	HEAVY_OIL(new SimpleCrude(Crudes.LIGHT_NAPHTHA, 0.05).mix(Crudes.HEAVY_NAPHTHA, 0.10).
			  mix(Crudes.KEROSENE, 0.20).mix(Crudes.LIGHT_GAS_OIL, 0.25).mix(Crudes.HEAVY_GAS_OIL, 0.1).
			  mix(Crudes.BITUMEN, 0.3), Strength.STRONG, Material.water),
	EXTRA_HEAVY_OIL(new SimpleCrude(Crudes.HEAVY_NAPHTHA, 0.05).
			        mix(Crudes.KEROSENE, 0.05).mix(Crudes.LIGHT_GAS_OIL, 0.10).mix(Crudes.HEAVY_GAS_OIL, 0.2).
				    mix(Crudes.BITUMEN, 0.6), Strength.STRONG, Material.water),
	OIL_SAND(EXTRA_HEAVY_OIL.mix(Aggregates.SAND, 2.0), Strength.WEAK, Material.sand),
	NATURAL_GAS(Compounds.METHANE.mix(Compounds.ETHANE, 0.05).mix(Compounds.PROPANE, 0.002).
			    mix(Compounds.N_BUTANE, 0.0003).mix(Compounds.ISO_BUTANE, 0.0003), 
			    Strength.WEAK, Material.air),
	OIL_SHALE(new SimpleCrude(Crudes.KEROGEN, 0.15).mix(MUDSTONE, 1.0).mix(Crudes.BITUMEN, 0.05), Strength.WEAK),
	// TODO: peat, lignite, anthracite (see notes)
	COAL(new SimpleCrude(Crudes.FIXED_CARBON, 0.65).mix(Aggregates.STONE, 0.15). /* really 'bituminous' coal */
		 mix(EXTRA_HEAVY_OIL, 0.20), Strength.WEAK, Aggregates.STONE),

	BASALTIC_MINERAL_SAND(Ores.MAGNETITE.mix(IndustrialMinerals.GARNET, 0.4)
					      .mix(Ores.CHROMITE, 0.2).mix(Ores.ILMENITE, 0.6).mix(Ores.RUTILE, 0.2).mix(Ores.ZIRCON, 0.2),
	                      Strength.WEAK, Aggregates.SAND),
	CASSITERITE_SAND(Ores.CASSITERITE.mix(Ores.SCHEELITE, 0.2), Strength.WEAK, Aggregates.SAND),
	GARNET_SAND(IndustrialMinerals.GARNET, Strength.WEAK, Material.sand),
	GRANITIC_MINERAL_SAND(Ores.MAGNETITE.mix(IndustrialMinerals.QUARTZ, 0.4).mix(IndustrialMinerals.KYANITE, 0.2)
			              .mix(Ores.ILMENITE, 0.4).mix(Ores.RUTILE, 0.6).mix(Ores.ZIRCON, 0.4).
			              mix(Ores.MONAZITE, 0.4), 
			              Strength.WEAK, Aggregates.SAND),
	QUARTZ_SAND(IndustrialMinerals.QUARTZ, Strength.WEAK, Aggregates.SAND, Material.sand),
	VOLCANIC_ASH(IndustrialMinerals.VOLCANIC_ASH, Strength.WEAK, Material.sand),
	GLAUCONITE_SAND(IndustrialMinerals.GLAUCONITE, Strength.WEAK, Aggregates.SAND),
	
	LATERITE(Aggregates.CLAY, Strength.WEAK, Material.clay),
	
	// FIXME: add niter (KNO3)? 
	BAUXITE(Ores.GIBBSITE.mix(Ores.HEMATITE, 0.45).mix(Ores.CALCITE, 0.10)
			.mix(IndustrialMinerals.KAOLINITE, 0.10).mix(IndustrialMinerals.QUARTZ, 0.05).mix(Ores.ANATASE, 0.05), 
			Strength.WEAK, LATERITE),
	BENTONITE(IndustrialMinerals.BENTONITE, Strength.WEAK, Material.clay),
	FULLERS_EARTH(IndustrialMinerals.FULLERS_EARTH, Strength.WEAK, Material.clay),
	KAOLINITE(IndustrialMinerals.KAOLINITE, Strength.WEAK, Material.clay),
	BROWN_LIMONITE(Ores.LEPIDOCROCITE, Strength.WEAK, LATERITE),
	YELLOW_LIMONITE(Ores.GOETHITE, Strength.WEAK, LATERITE),
	VERMICULITE(IndustrialMinerals.VERMICULITE, Strength.WEAK, Material.clay),
	
	BORAX(Ores.BORAX, Strength.WEAK, null, Material.rock),
	CINNABAR(Ores.CINNABAR.mix(Ores.PYRITE, 0.05).mix(Ores.REALGAR, 0.04).mix(Ores.STIBNITE, 0.02).mix(Ores.BARITE, 0.02), 
			 Strength.WEAK, BASALT),
	GALENA(Ores.GALENA.mix(Ores.SPHALERITE, 0.2).mix(Ores.ACANTHITE, 0.05).mix(Ores.FLUORITE, 0.05).mix(Ores.BISMUTHINITE, 0.05)
		   .mix(Ores.REALGAR, 0.04).mix(Ores.STIBNITE, 0.02).mix(Ores.GREENOCKITE, 0.01).mix(Ores.VANADINITE, 0.01), Strength.WEAK,
		   Aggregates.STONE),
	MOLYBDENITE(Ores.MOLYBDENITE.mix(Ores.PYRITE, 0.05).mix(Ores.CHALCOPYRITE, 0.05)
			    .mix(Ores.FLUORITE, 0.02), Strength.WEAK, BRECCIA),
	PYROLUSITE(Ores.PYROLUSITE.mix(Ores.GOETHITE, 0.1), Strength.WEAK, Aggregates.STONE),
	ROCK_SALT(Ores.HALITE.mix(Ores.SYLVITE, 0.5).mix(Ores.CARNALLITE, 0.1).mix(Ores.GYPSUM, 0.05), Strength.WEAK),
	STIBNITE(Ores.STIBNITE.mix(Ores.PYRITE, 0.05).mix(Ores.GALENA, 0.05).mix(Ores.REALGAR, 0.04).mix(Ores.CINNABAR, 0.02), 
			 Strength.WEAK, GRANITE),
	
	BARITE(Ores.BARITE, Strength.MEDIUM, Aggregates.STONE),
	BASTNASITE(Ores.BASTNASITE, Strength.MEDIUM, CARBONATITE),
	CHALCOPYRITE(Ores.CHALCOPYRITE.mix(Ores.PYRITE, 0.10).mix(Ores.MOLYBDENITE, 0.05)
				 .mix(Ores.COBALTITE, 0.01), Strength.MEDIUM, Aggregates.STONE),
	GARNIERITE(Ores.NEPOUITE, Strength.MEDIUM, SERPENTINITE),
	LEPIDOLITE(Ores.LEPIDOLITE.mix(Ores.SPODUMENE, 0.2), Strength.MEDIUM, PEGMATITE),
	MAGNESITE(Ores.MAGNESITE.mix(IndustrialMinerals.TALC, 0.2), Strength.MEDIUM, Aggregates.STONE),
	PENTLANDITE(Ores.PENTLANDITE.mix(Ores.PYRITE, 0.1), Strength.MEDIUM, SERPENTINITE),
	SCHEELITE(Ores.SCHEELITE.mix(Ores.CASSITERITE, 0.2).mix(Ores.WOLFRAMITE, 0.2), Strength.MEDIUM, Aggregates.STONE),
	SPHALERITE(Ores.SPHALERITE.mix(Ores.GALENA, 0.2).mix(Ores.PYRITE, 0.1), Strength.MEDIUM, Aggregates.STONE),
	WOLFRAMITE(Ores.WOLFRAMITE.mix(Ores.CASSITERITE, 0.2).mix(Ores.SCHEELITE, 0.2), Strength.MEDIUM, PEGMATITE),
	
	BANDED_IRON(Ores.HEMATITE.mix(Ores.MAGNETITE, 0.5), Strength.STRONG, MUDSTONE),
	QUARTZ(IndustrialMinerals.QUARTZ, Strength.STRONG, GRANITE),
	CASSITERITE(Ores.CASSITERITE.mix(Ores.FLUORITE, 0.1).mix(Ores.WOLFRAMITE, 0.1)
			    .mix(IndustrialMinerals.APATITE, 0.05).mix(Ores.MOLYBDENITE, 0.05), Strength.STRONG, Aggregates.STONE),
	CHROMITE(Ores.CHROMITE.mix(SERPENTINITE, 0.5).mix(Ores.MAGNETITE, 0.1), Strength.STRONG, SERPENTINITE),
	ILMENITE(Ores.ILMENITE.mix(Ores.RUTILE, 0.2).mix(Ores.MAGNETITE, 0.1), Strength.STRONG, DIORITE),
	MAGNETITE(Ores.MAGNETITE, Strength.STRONG, GRANITE),
	POLLUCITE(Ores.POLLUCITE.mix(Ores.SPODUMENE, 0.1), Strength.STRONG, PEGMATITE),
	SPODUMENE(Ores.SPODUMENE.mix(Ores.LEPIDOLITE, 0.1), Strength.STRONG, PEGMATITE),
	TANTALITE(Ores.TANTALITE.mix(Ores.COLUMBITE, 2.0), Strength.STRONG, PEGMATITE),
	PITCHBLENDE(Ores.URANINITE.mix(Ores.CARNOTITE, 0.05), Strength.STRONG, Aggregates.STONE),
	VANADIUM_MAGNETITE(Ores.TITANO_MAGNETITE, Strength.STRONG, PERIDOTITE),
	
	CHRYSOTILE(IndustrialMinerals.CHRYSOTILE.mix(SERPENTINITE, 0.5), Strength.WEAK),
	DIATOMITE(IndustrialMinerals.DIATOMITE, Strength.WEAK),
	REALGAR(Ores.REALGAR.mix(Ores.ORPIMENT, 0.1).mix(Ores.STIBNITE, 0.04).mix(Ores.CINNABAR, 0.04), Strength.WEAK, GRANITE),
	GRAPHITE(IndustrialMinerals.GRAPHITE.mix(COAL, 0.5), Strength.WEAK),
	GYPSUM(IndustrialMinerals.GYPSUM.mix(Ores.HALITE, 0.05), Strength.WEAK),
	MIRABILITE(IndustrialMinerals.MIRABILITE
	           .mix(IndustrialMinerals.GYPSUM, 0.2).mix(Ores.HALITE, 0.05), Strength.WEAK),
	MICA(IndustrialMinerals.MICA, Strength.WEAK, PEGMATITE),
	SOAPSTONE(IndustrialMinerals.TALC, Strength.WEAK, SERPENTINITE),
	TRONA(IndustrialMinerals.TRONA
          .mix(IndustrialMinerals.GYPSUM, 0.2).mix(Ores.HALITE, 0.05), Strength.WEAK),
	
	ALUNITE(IndustrialMinerals.ALUNITE.mix(IndustrialMinerals.BENTONITE, 0.1), Strength.MEDIUM),
	CELESTINE(Ores.CELESTINE.mix(IndustrialMinerals.GYPSUM, 0.2).mix(Ores.HALITE, 0.05), Strength.MEDIUM, GYPSUM),
	DOLOMITE(IndustrialMinerals.DOLOMITE.mix(Ores.MAGNESITE, 0.05), Strength.MEDIUM),
	FLUORITE(Ores.FLUORITE.mix(Ores.SPHALERITE, 0.06).mix(Ores.GALENA, 0.02), Strength.MEDIUM, Aggregates.STONE),
	WOLLASTONITE(IndustrialMinerals.WOLLASTONITE.mix(Ores.CALCITE, 0.1), Strength.MEDIUM),
	ZEOLITE(IndustrialMinerals.ZEOLITE, Strength.MEDIUM),
	
	APATITE(IndustrialMinerals.APATITE, Strength.STRONG, Aggregates.STONE),
	KYANITE(IndustrialMinerals.KYANITE, Strength.STRONG, PEGMATITE),
	PERLITE(IndustrialMinerals.PERLITE, Strength.STRONG, Aggregates.OBSIDIAN),
	PUMICE(IndustrialMinerals.PUMICE, Strength.STRONG),
	PYRITE(Ores.PYRITE.mix(Ores.CHALCOPYRITE, 0.05).mix(Ores.SPHALERITE, 0.05), Strength.STRONG, Aggregates.STONE),
	
	GOLD(new SimpleVanillaOre(Ores.GOLD), Strength.STRONG, Aggregates.STONE), // TODO: electrum
	LAPIS(new SimpleVanillaOre(IndustrialMinerals.LAZURITE.mix(Ores.CALCITE, 0.4).
		  mix(IndustrialMinerals.SODALITE, 0.4).mix(Ores.PYRITE, 0.2)), Strength.STRONG, GRANITE),
	DIAMOND(new SimpleVanillaOre(IndustrialMinerals.DIAMOND), Strength.STRONG, PERIDOTITE),
	EMERALD(new SimpleVanillaOre(Ores.BERYL), Strength.STRONG, PEGMATITE), 
	REDSTONE(new SimpleVanillaOre(Ores.CUPRITE.mix(Ores.CHALCOPYRITE, 0.25).
			 mix(IndustrialMinerals.AZURITE, 0.1).mix(Ores.MALACHITE, 0.1)),
			 Strength.STRONG, Aggregates.STONE) 
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
    private IndustrialMaterial host;
		
	GeoMaterial(Mixture composition, Strength strength, IndustrialMaterial host, Material blockMaterial) {
	    this.composition = composition;
		this.strength = strength;
		this.host = host;
		this.blockMaterial = blockMaterial;
	}
	
	GeoMaterial(Mixture composition, Strength strength, Material blockMaterial) {
	    this(composition, strength, null, blockMaterial);
	}
	
	GeoMaterial(Mixture composition, Strength strength, IndustrialMaterial host) {
	    this(composition, strength, host, blockMaterialFromHost(host));
	}
	
	GeoMaterial(Mixture composition, Strength strength) {
        this(composition, strength, Material.rock);
    }
	
	GeoMaterial(OreMineral composition, Strength strength, IndustrialMaterial host, Material blockMaterial) {
		this(new SimpleOre(composition), strength, host, blockMaterial);
	}
	
	GeoMaterial(OreMineral composition, Strength strength, IndustrialMaterial host) {
		this(composition, strength, host, blockMaterialFromHost(host));
	}
	
	GeoMaterial(IndustrialMineral composition, Strength strength, IndustrialMaterial host, Material blockMaterial) {
        this(new SimpleOre(composition), strength, host, blockMaterial);
    }

	GeoMaterial(IndustrialMineral composition, Strength strength, IndustrialMaterial host) {
	    this(composition, strength, host, blockMaterialFromHost(host));
	}

	GeoMaterial(IndustrialMineral composition, Strength strength, Material blockMaterial) {
		this(composition, strength, null, blockMaterial);
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

	public IndustrialMaterial getHost() {
	    return this.host;
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
	public ConditionProperties getProperties(Condition condition) {
		return composition.getProperties(condition);
	}

	@Override
	public List<MixtureComponent> getComponents() {
		return composition.getComponents();
	}

	@Override
	public Mixture mix(IndustrialMaterial material, double weight) {
		return composition.mix(material, weight);
	}
        
	private static Material blockMaterialFromHost(IndustrialMaterial host)
    {
        Material material = Material.rock;
        if (host == Aggregates.SAND) {
            material = Material.sand;
        } else if (host == Aggregates.STONE || host == Aggregates.OBSIDIAN) {
            material = Material.rock;
        } else if (host instanceof GeoMaterial) {
            material = ((GeoMaterial)host).getBlockMaterial();
        }
        return material;
    }
}
