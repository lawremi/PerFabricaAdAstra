package org.pfaa.chemica.registration;

import org.pfaa.chemica.ChemicaBlocks;
import org.pfaa.chemica.ChemicaItems;
import org.pfaa.chemica.block.ConstructionMaterialBlock;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.model.Compound.Compounds;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreRegistration {
	public static void init() {
		oreDictifyMiscBlocks();
		oreDictifyMaterialItems();
		oreDictifyMaterialBlocks();
		oreDictifyHardenedClay();
		registerDyes();
	}

	private static void oreDictifyMiscBlocks() {
		OreDictionary.registerOre("dirtPolluted", ChemicaBlocks.POLLUTED_SOIL);
	}

	private static void oreDictifyMaterialBlocks() {
		for (ConstructionMaterialBlock block : ChemicaBlocks.getConstructionMaterialBlocks()) {
			OreDictUtils.register(block);
		}
	}

	private static void oreDictifyMaterialItems() {
		for (IndustrialMaterialItem<?> item : ChemicaItems.getIndustrialMaterialItems()) {
			OreDictUtils.register(item);
		}
	}

	private static void oreDictifyHardenedClay() {
		String KEY = "blockHardenedClay";
		OreDictionary.registerOre(KEY, Blocks.hardened_clay);
		OreDictionary.registerOre(KEY, new ItemStack(Blocks.stained_hardened_clay, 1, OreDictionary.WILDCARD_VALUE));
	}

	private static void registerDyes() {
		OreDictUtils.registerDye("black", Compounds.MnO2);
		OreDictUtils.registerDye("gray", Compounds.PbS);
		OreDictUtils.registerDye("gray", Compounds.Sb2S3);
		OreDictUtils.registerDye("red", Compounds.Fe2O3);
		OreDictUtils.registerDye("red", Compounds.HgS);
		OreDictUtils.registerDye("green", Compounds.Cu2CO3OH2);
		OreDictUtils.registerDye("brown", Compounds.gamma_FeOH3);
		OreDictUtils.registerDye("yellow", Compounds.alpha_FeOH3);
		OreDictUtils.registerDye("yellow", Compounds.As2S3);
		OreDictUtils.registerDye("white", Compounds.CaCO3);
		OreDictUtils.registerDye("white", Compounds.CaSO4_2H2O);
	}
}
