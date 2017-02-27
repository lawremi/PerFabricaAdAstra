package org.pfaa.chemica.registration;

import org.pfaa.chemica.ChemicaBlocks;
import org.pfaa.chemica.ChemicaItems;
import org.pfaa.chemica.item.IndustrialItems;
import org.pfaa.chemica.model.Generics;
import org.pfaa.chemica.model.Aggregate.Aggregates;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.processing.MaterialStack;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreRegistration {
	public static void init() {
		oreDictifyMiscBlocks();
		oreDictifyMaterialBlocks();
		oreDictifyMaterialItems();
		oreDictifyAggregates();
		oreDictifyGenerics();
		registerDyes();
	}

	private static void oreDictifyMiscBlocks() {
		OreDictionary.registerOre("dirtPolluted", ChemicaBlocks.POLLUTED_SOIL);
	}

	private static void oreDictifyMaterialBlocks() {
		OreDictUtils.register(ChemicaBlocks.getConstructionMaterialBlocks());
	}
			
	private static void oreDictifyMaterialItems() {
		OreDictUtils.register(ChemicaItems.getIndustrialMaterialItems());
	}
	
	private static void oreDictifyAggregates() {
		oreDictifyClay();
		oreDictifyLooseAggregates();
		oreDictifyStoneBrick();
	}
	
	private static void oreDictifyClay() {
		OreDictUtils.register(Forms.CLUMP.of(Aggregates.CLAY), new ItemStack(Items.clay_ball));
		OreDictUtils.register(Forms.BLOCK.of(Aggregates.CLAY), new ItemStack(Blocks.clay));
		MaterialStack hardenedClay = Forms.BLOCK.of(Aggregates.HARDENED_CLAY);
		OreDictUtils.register(hardenedClay, new ItemStack(Blocks.hardened_clay));
		OreDictUtils.register(hardenedClay, new ItemStack(Blocks.stained_hardened_clay, 1, OreDictionary.WILDCARD_VALUE));
	}
	
	private static void oreDictifyLooseAggregates() {
		OreDictUtils.register(Forms.BLOCK.of(Aggregates.SAND), new ItemStack(Blocks.sand));
		OreDictUtils.register(Forms.DUST.of(Generics.FLUX_SILICA), new ItemStack(Blocks.sand));
		OreDictUtils.register(Forms.DUST_TINY.of(Generics.FLUX_SILICA), 
				IndustrialItems.getBestItemStack(Forms.PILE.of(Aggregates.SAND)));
		OreDictUtils.register(Forms.BLOCK.of(Aggregates.GRAVEL), new ItemStack(Blocks.gravel));
	}

	private static void oreDictifyStoneBrick() {
		OreDictionary.registerOre("stoneBricks", Blocks.stonebrick);
	}

	private static void oreDictifyGenerics() {
		OreDictUtils.register(Generics.class);
	}

	private static void registerDyes() {
		OreDictUtils.registerDye("black", Compounds.MnO2);
		OreDictUtils.registerDye("gray", Compounds.PbS);
		OreDictUtils.registerDye("gray", Compounds.Sb2S3);
		OreDictUtils.registerDye("red", Compounds.Fe2O3);
		OreDictUtils.registerDye("red", Compounds.HgS);
		OreDictUtils.registerDye("green", Compounds.Cu2CO3OH2);
		OreDictUtils.registerDye("blue", Compounds.Cu3CO32OH2);
		OreDictUtils.registerDye("brown", Compounds.gamma_FeOH3);
		OreDictUtils.registerDye("yellow", Compounds.alpha_FeOH3);
		OreDictUtils.registerDye("yellow", Compounds.As2S3);
		OreDictUtils.registerDye("white", Compounds.CaCO3);
		OreDictUtils.registerDye("white", Compounds.CaSO4_2H2O);
	}
}
