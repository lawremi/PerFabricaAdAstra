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
import org.pfaa.geologica.processing.Crude.Crudes;
import org.pfaa.geologica.processing.IndustrialMineral.IndustrialMinerals;
import org.pfaa.geologica.processing.OreMineral.Ores;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreRegistration {
	
	public static void init() {
		registerRawMaterials();
		useVanillaItemsForVanillaOres();
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
			registerDye(GeologicaItems.ORE_DUST.getItemStack(material), material.getConcentrate());
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

	private static void useVanillaItemsForVanillaOres() {
		// FIXME: should this be based on ore drops? Can we put the quantity on the item stack? 
		OreDictUtils.register(Forms.CRUSHED.of(GeoMaterial.LAPIS), "gemLapis");
		OreDictUtils.register(Forms.CRUSHED.of(GeoMaterial.DIAMOND), Items.diamond);
		OreDictUtils.register(Forms.CRUSHED.of(GeoMaterial.EMERALD), Items.emerald);
		OreDictUtils.register(Forms.DUST_IMPURE.of(GeoMaterial.REDSTONE), Items.redstone);
	}
}
