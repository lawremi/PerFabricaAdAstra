package org.pfaa.fabrica.registration;

import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.ChemicaItems;
import org.pfaa.chemica.fluid.IndustrialFluids;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.item.ItemIngredientStack;
import org.pfaa.chemica.model.Aggregate.Aggregates;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.processing.MaterialStack;
import org.pfaa.chemica.registration.BaseRecipeRegistration;
import org.pfaa.chemica.registration.IngredientList;
import org.pfaa.chemica.registration.RecipeUtils;
import org.pfaa.core.item.ChanceStack;
import org.pfaa.fabrica.FabricaBlocks;
import org.pfaa.fabrica.FabricaItems;
import org.pfaa.fabrica.model.Generics;
import org.pfaa.fabrica.model.Intermediate.Intermediates;
import org.pfaa.geologica.GeologicaItems;
import org.pfaa.geologica.processing.IndustrialMineral.IndustrialMinerals;
import org.pfaa.geologica.processing.OreMineral.Ores;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeRegistration extends BaseRecipeRegistration {
	
	public static void init() {
		grindIntermediates();
		grindBricks();
		grindSlag();
		hydrateHardenedClay();
		useFeldsparAsFlux();
		calcineMaterials();
		makeAsh();
		makePortlandCement();
		makePozzolanicCement();
		makeConcrete();
		makeDrywall();
		makeDrywallJointCompound();
		fillPigments();
		makePhosphoricAcid();
		makeHood();
	}

	private static void registerCrushingRecipe(Aggregates aggregate, int nPerBlock) {
		MaterialStack materialStack = Forms.BLOCK.of(aggregate);
		ItemStack output = ChemicaItems.AGGREGATE_LARGE_DUST.getItemStack(aggregate, nPerBlock);
		for (ItemStack input : materialStack.getItemStacks()) {
			RECIPES.registerCrushingRecipe(input, output, null, Strength.WEAK);
		}
	}

	private static void hydrateHardenedClay() {
		// FIXME: Aggregate crushing should be in Chemica, i.e., blockHardenedClay => dustLargeHardenedClay
		registerCrushingRecipe(Aggregates.HARDENED_CLAY, 4);
		FluidStack water = Forms.CLUMP.of(State.LIQUID.of(Compounds.H2O)).getFluidStack();
		GENERICS.registerMixingRecipe(
				Forms.DUST.of(Aggregates.HARDENED_CLAY).with(),
				water,
				null,
				new ItemStack(Items.clay_ball),
				null, null, Condition.STP, null);
	}

	private static void grindIntermediates() {
		for (Intermediates material : FabricaItems.INTERMEDIATE_LUMP.getIndustrialMaterials()) {
			ItemStack input = FabricaItems.INTERMEDIATE_LUMP.getItemStack(material);
			ItemStack output = FabricaItems.INTERMEDIATE_DUST.getItemStack(material);
			RECIPES.registerGrindingRecipe(input, output, Collections.<ChanceStack>emptyList(), Strength.WEAK);
		}
	}

	private static void grindBricks() {
		ItemStack output = FabricaItems.INTERMEDIATE_DUST.getItemStack(Intermediates.FIRED_CLAY);
		RECIPES.registerGrindingRecipe(new ItemStack(Items.brick), output, 
				Collections.<ChanceStack>emptyList(), Strength.MEDIUM);
	}

	private static void grindSlag() {
		ItemStack output = FabricaItems.INTERMEDIATE_DUST.getItemStack(Intermediates.SLAG);
		GENERICS.registerGrindingRecipe(Forms.LUMP.of(Intermediates.SLAG), output, 
				Collections.<ChanceStack>emptyList(), Strength.STRONG);
	}
	
	private static void calcineMaterial(IndustrialMaterial mineral, IndustrialMaterial calcined, int temp) {
		IngredientList<MaterialStack> inputs = IngredientList.of(Forms.DUST.of(mineral));
		ItemStack output = Forms.DUST.of(calcined).getBestItemStack();
		GENERICS.registerRoastingRecipe(inputs, output, null, temp);
	}
	
	private static void calcineMaterials() {
		calcineMaterial(IndustrialMinerals.DIATOMITE, Intermediates.CALCINED_DIATOMITE, 1300);
		calcineMaterial(IndustrialMinerals.KAOLINITE, Intermediates.METAKAOLIN, 1000);
		calcineMaterial(Intermediates.METAKAOLIN, Intermediates.SPINEL, 1200);
		calcineMaterial(Intermediates.SPINEL, Intermediates.MULLITE, 1600);
		calcineMaterial(Ores.GYPSUM, Intermediates.GYPSUM_PLASTER, 600);
		// FIXME: move to Geologica
		calcineMaterial(IndustrialMinerals.TRONA, Compounds.Na2CO3, 600);
	}

	private static void makePortlandCement() {
		ItemStack clinker = FabricaItems.INTERMEDIATE_LUMP.getItemStack(Intermediates.PORTLAND_CLINKER, 4);	
		IngredientList<MaterialStack> inputs = IngredientList.of(
				Forms.DUST.of(3, Ores.CALCITE),
				Forms.CLUMP.of(Aggregates.CLAY));
		GENERICS.registerRoastingRecipe(inputs, clinker, null, 1700);
		mixIntermediate(FabricaItems.INTERMEDIATE_LUMP, Intermediates.PORTLAND_CEMENT);
	}

	private static void makePozzolanicCement() {
		mixIntermediate(FabricaItems.INTERMEDIATE_DUST, Intermediates.POZZOLANIC_CEMENT);
	}

	private static void makeConcrete() {
		/*
		 * TODO: 
		 * - Support coal fly ash and silica fume as pozzalans, once we can capture them
		 * - Support geopolymer concrete
		 */
		List<ItemStack> concretes = OreDictionary.getOres("concrete");
		if (concretes.size() > 0) {
			ItemStack concrete = concretes.get(0).copy();
			concrete.stackSize = 8;
			IngredientList<?> inputs = MaterialStack.of(Aggregates.GRAVEL).
					with(MaterialStack.of(Aggregates.SAND), Forms.DUST.of(Generics.CEMENT));
			FluidStack water = Forms.DUST.of(Compounds.H2O).getFluidStack();
			GENERICS.registerMixingRecipe(inputs, water, null, concrete, null, null, Condition.STP, null);
		}
	}

	private static void makeDrywall() {
		RecipeUtils.addShapedRecipe(new ItemStack(FabricaBlocks.DRYWALL, 8),
				"pgp",
				"pwp",
				"pgp",
				'p', Items.paper,
				'g', Intermediates.GYPSUM_PLASTER,
				'w', Items.water_bucket);
	}
	
	private static void makeDrywallJointCompound() {
		IngredientList<MaterialStack> jointCompoundSolids = IngredientList.of(
				Forms.DUST.of(Generics.JOINT_COMPOUND_FILLER),
				Forms.DUST_TINY.of(Generics.BINDER),
				Forms.DUST_TINY.of(IndustrialMinerals.PALYGORSKITE),
				Forms.DUST_TINY.of(Generics.FILLER)
		);
		FluidStack water = Forms.DUST.of(Compounds.H2O).getFluidStack();
		GENERICS.registerMixingRecipe(jointCompoundSolids, water, 
				null, new ItemStack(FabricaItems.JOINT_COMPOUND, 4), null, null, Condition.STP, null);
	}
	
	private static void useFeldsparAsFlux() {
		ItemStack output = new ItemStack(Blocks.glass);
		ItemStack input = new ItemStack(Blocks.sand);
		MaterialStack feldspar = Forms.DUST_TINY.of(IndustrialMinerals.FELDSPAR);
		GENERICS.registerCastingRecipe(input, output, feldspar, 1500);
		
		output = new ItemStack(Items.brick);
		input = new ItemStack(Items.clay_ball);
		GENERICS.registerCastingRecipe(input, output, feldspar, 1500);
	}

	private static void mixIntermediate(IndustrialMaterialItem<Intermediates> item, Intermediates material) {
		IngredientList<MaterialStack> inputs = RecipeUtils.getMixtureInputs(item.getForm(), material);
		GENERICS.registerMixingRecipe(inputs, item.getItemStack(material));
	}
	
	private static void makeAsh() {
		ItemStack bonemeal = new ItemStack(Items.dye, 1, 15);
		RECIPES.registerRoastingRecipe(Collections.singletonList(bonemeal), 
				FabricaItems.INTERMEDIATE_DUST.getItemStack(Intermediates.ASH), null, 1000);
	}
	
	private static void fillPigments() {
		for (ItemStack dye : OreDictionary.getOres("dye")) {
			ItemStack doubleDye = dye.copy();
			doubleDye.stackSize = 2;
			GENERICS.registerMixingRecipe(
					IngredientList.of(new ItemIngredientStack(dye), Forms.DUST.of(Generics.FILLER)), 
					doubleDye);	
		}
	}

	private static void makePhosphoricAcid() {
		IngredientList<MaterialStack> solidInputs = IngredientList.of(Forms.DUST.of(IndustrialMinerals.APATITE));
		FluidStack sulfuricAcid = IndustrialFluids.getFluidStack(Compounds.H2SO4, State.AQUEOUS);
		FluidStack phosphoricAcid = IndustrialFluids.getFluidStack(Compounds.H3PO4, State.AQUEOUS);
		ItemStack gypsum = GeologicaItems.ORE_MINERAL_DUST.getItemStack(Ores.GYPSUM);
		GENERICS.registerMixingRecipe(solidInputs, sulfuricAcid, null, gypsum, phosphoricAcid, null, Condition.STP, null);
	}

	private static void makeHood() {
		GameRegistry.addShapedRecipe(new ItemStack(FabricaBlocks.HOOD), 
				"sss",
				"sts",
				"sgs",
				's', Blocks.cobblestone,
				't', Blocks.trapdoor,
				'g', Blocks.glass);
	}
}
