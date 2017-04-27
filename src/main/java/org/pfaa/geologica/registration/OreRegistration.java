package org.pfaa.geologica.registration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.pfaa.chemica.item.IndustrialItems;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.Generic;
import org.pfaa.chemica.model.Generics;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.SimpleGeneric;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.registration.OreDictUtils;
import org.pfaa.geologica.GeoMaterial;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.GeologicaItems;
import org.pfaa.geologica.block.VanillaOreOverrideBlock;
import org.pfaa.geologica.processing.Crude.Crudes;
import org.pfaa.geologica.processing.IndustrialMineral.IndustrialMinerals;
import org.pfaa.geologica.processing.OreMineral.Ores;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreRegistration {
	
	public static void init() {
		registerRawMaterials();
		oreDictifyMaterialItems();
		oreDictifyBlocks();
		registerDyes();
		oreDictifyFuels();
		inheritChemicaGenerics();
	}

	private static void registerRawMaterials() {
		Arrays.stream(GeoMaterial.values()).forEach(RawMaterials::register);
		Arrays.stream(Ores.values()).forEach(RawMaterials::register);
	}

	private static void oreDictifyMaterialItems() {
		OreDictUtils.register(GeologicaItems.getIndustrialMaterialItems());
	}

	private static void registerDyes() {
		for (Ores material : GeologicaItems.ORE_MINERAL_DUST.getIndustrialMaterials()) {
			registerDye(GeologicaItems.ORE_MINERAL_DUST.getItemStack(material), material.getConcentrate());
		}
		for (GeoMaterial material : GeologicaItems.ORE_CRUSHED.getIndustrialMaterials()) {
			registerDye(GeologicaItems.ORE_DUST.getItemStack(material), material.getOreConcentrate());
		}
		OreDictUtils.registerDye("blue", IndustrialMinerals.LAZURITE);
		OreDictUtils.registerDye("gray", IndustrialMinerals.GRAPHITE);
	}

	private static void registerDye(ItemStack dye, IndustrialMaterial concentrate) {
		ItemStack itemStack = IndustrialItems.getBestItemStack(Forms.DUST, concentrate);
		for (int id : OreDictionary.getOreIDs(itemStack)) {
			String name = OreDictionary.getOreName(id);
			if (name.startsWith("dye")) {
				OreDictionary.registerOre(name, dye);
			}
		}
	}

	private static void oreDictifyBlocks() {
		OreDictUtils.register(GeologicaBlocks.getIndustrialBlocks());
		GeologicaBlocks.getVanillaOreOverrideBlocks().forEach(OreRegistration::oreDictify);
	}
	
	private static void oreDictify(VanillaOreOverrideBlock block) {
		String name = Block.blockRegistry.getNameForObject(block);
		if (name != null) {
			String material = name.substring(name.indexOf(':') + 1, name.length() - 3);
			OreDictionary.registerOre(OreDictUtils.makeKey("ore", material), new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE));
		}
	}

	private static void oreDictifyFuels() {
		IndustrialMaterialItem<Crudes> fuel = GeologicaItems.CRUDE_LUMP;
		for (Crudes material : fuel.getIndustrialMaterials()) {
			String key = OreDictUtils.makeKey("fuel", material.getOreDictKey());
			OreDictionary.registerOre(key, fuel.getItemStack(material));
		}
	}

	private static void inheritChemicaGenerics() {
		inheritGeneric(Generics.FLUX_SILICA);
		inheritGeneric(Generics.FLUX_ACIDIC);
		inheritGeneric(Generics.FLUX_BASIC);
	}

	private static void inheritGeneric(Generic generic) {
		List<IndustrialMaterial> rawMaterials = generic.getSpecifics().stream().map((specific) -> {
			return RawMaterials.get((Chemical)specific);
		}).collect(Collectors.toList()); 
		OreDictUtils.register(new SimpleGeneric(generic.name(), rawMaterials));
	}

}
