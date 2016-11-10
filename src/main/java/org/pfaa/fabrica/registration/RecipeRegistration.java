package org.pfaa.fabrica.registration;

import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.ChemicaItems;
import org.pfaa.chemica.fluid.IndustrialFluids;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.item.ItemIngredientStack;
import org.pfaa.chemica.item.MaterialStack;
import org.pfaa.chemica.model.Aggregate.Aggregates;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Constants;
import org.pfaa.chemica.model.Element;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.Reaction;
import org.pfaa.chemica.model.SimpleMixture;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.registration.GenericRecipeRegistry;
import org.pfaa.chemica.registration.IngredientList;
import org.pfaa.chemica.registration.OreDictUtils;
import org.pfaa.chemica.registration.ReactionRegistry;
import org.pfaa.chemica.registration.RecipeRegistry;
import org.pfaa.chemica.registration.RecipeUtils;
import org.pfaa.chemica.util.ChanceStack;
import org.pfaa.fabrica.FabricaBlocks;
import org.pfaa.fabrica.FabricaItems;
import org.pfaa.fabrica.model.Generic.Generics;
import org.pfaa.fabrica.model.Intermediate.Intermediates;
import org.pfaa.geologica.GeologicaItems;
import org.pfaa.geologica.processing.IndustrialMineral.IndustrialMinerals;
import org.pfaa.geologica.processing.OreMineral.Ores;
import org.pfaa.geologica.processing.Solutions;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeRegistration {
	private static final RecipeRegistry recipes = 
			org.pfaa.chemica.registration.RecipeRegistration.getTarget();
	private static final GenericRecipeRegistry genericRecipes = 
			org.pfaa.chemica.registration.RecipeRegistration.getGenericTarget();;
	private static final ReactionRegistry reactionRecipes = new ReactionRegistry(genericRecipes);
	
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
		//activateCarbon();
		makeSodaAsh();
		makeHood();
	}

	private static void registerCrushingRecipe(Aggregates aggregate) {
		MaterialStack materialStack = new MaterialStack(Forms.BLOCK, aggregate);
		ItemStack output = ChemicaItems.AGGREGATE_DUST.getItemStack(aggregate, 4);
		for (ItemStack input : materialStack.getItemStacks()) {
			recipes.registerCrushingRecipe(input, output, null, Strength.WEAK);
		}
	}

	private static void hydrateHardenedClay() {
		registerCrushingRecipe(Aggregates.HARDENED_CLAY);
		FluidStack water = new FluidStack(FluidRegistry.WATER, IndustrialFluids.getAmount(Forms.DUST));
		genericRecipes.registerMixingRecipe(
				new IngredientList(Aggregates.HARDENED_CLAY),
				water,
				null,
				new ItemStack(Items.clay_ball),
				null, null, Condition.STP, null);
	}

	private static void grindIntermediates() {
		for (Intermediates material : FabricaItems.INTERMEDIATE_LUMP.getIndustrialMaterials()) {
			ItemStack input = FabricaItems.INTERMEDIATE_LUMP.getItemStack(material);
			ItemStack output = FabricaItems.INTERMEDIATE_DUST.getItemStack(material);
			recipes.registerGrindingRecipe(input, output, Collections.<ChanceStack>emptyList(), Strength.WEAK);
		}
	}

	private static void grindBricks() {
		ItemStack output = FabricaItems.INTERMEDIATE_DUST.getItemStack(Intermediates.FIRED_CLAY);
		recipes.registerGrindingRecipe(new ItemStack(Items.brick), output, 
				Collections.<ChanceStack>emptyList(), Strength.MEDIUM);
	}

	private static void grindSlag() {
		ItemStack output = FabricaItems.INTERMEDIATE_DUST.getItemStack(Intermediates.SLAG);
		genericRecipes.registerGrindingRecipe(new MaterialStack(Forms.LUMP, Intermediates.SLAG), output, 
				Collections.<ChanceStack>emptyList(), Strength.STRONG);
	}
	
	private static void calcineMaterial(IndustrialMaterial mineral, IndustrialMaterial calcined, int temp) {
		IngredientList inputs = new IngredientList(new MaterialStack(mineral));
		ItemStack output = new MaterialStack(calcined).getBestItemStack();
		genericRecipes.registerRoastingRecipe(inputs, output, null, temp);
	}
	
	private static void calcineMaterials() {
		calcineMaterial(IndustrialMinerals.DIATOMITE, Intermediates.CALCINED_DIATOMITE, 1300);
		calcineMaterial(IndustrialMinerals.KAOLINITE, Intermediates.METAKAOLIN, 1000);
		calcineMaterial(Intermediates.METAKAOLIN, Intermediates.SPINEL, 1200);
		calcineMaterial(Intermediates.SPINEL, Intermediates.MULLITE, 1600);
		calcineMaterial(Ores.GYPSUM, Intermediates.GYPSUM_PLASTER, 600);
		calcineMaterial(IndustrialMinerals.TRONA, Compounds.Na2CO3, 600);
	}

	private static void makePortlandCement() {
		ItemStack clinker = FabricaItems.INTERMEDIATE_LUMP.getItemStack(Intermediates.PORTLAND_CLINKER, 4);	
		IngredientList inputs = new IngredientList(
				new MaterialStack(Ores.CALCITE, 3),
				new MaterialStack(Forms.CLUMP, Aggregates.CLAY));
		genericRecipes.registerRoastingRecipe(inputs, clinker, null, 1700);
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
			IngredientList inputs = new IngredientList(
					new MaterialStack(null, Aggregates.GRAVEL),
					new MaterialStack(null, Aggregates.SAND),
					new MaterialStack(Generics.CEMENT));
			FluidStack water = new FluidStack(FluidRegistry.WATER, IndustrialFluids.getAmount(Forms.DUST));
			genericRecipes.registerMixingRecipe(inputs, water, null, concrete, null, null, Condition.STP, null);
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
		IngredientList jointCompoundSolids = new IngredientList(
				new MaterialStack(Forms.DUST, Generics.JOINT_COMPOUND_FILLER),
				new MaterialStack(Forms.DUST_TINY, Generics.BINDER),
				new MaterialStack(Forms.DUST_TINY, IndustrialMinerals.PALYGORSKITE),
				new MaterialStack(Forms.DUST_TINY, Generics.FILLER)
		);
		FluidStack water = new FluidStack(FluidRegistry.WATER, IndustrialFluids.getAmount(Forms.DUST));
		genericRecipes.registerMixingRecipe(jointCompoundSolids, water, 
				null, new ItemStack(FabricaItems.JOINT_COMPOUND, 4), null, null, Condition.STP, null);
	}
	
	private static void useFeldsparAsFlux() {
		ItemStack output = new ItemStack(Blocks.glass);
		ItemStack input = new ItemStack(Blocks.sand);
		MaterialStack feldspar = new MaterialStack(Forms.DUST_TINY, IndustrialMinerals.FELDSPAR);
		genericRecipes.registerCastingRecipe(input, output, feldspar, 1500);
		
		output = new ItemStack(Items.brick);
		input = new ItemStack(Items.clay_ball);
		genericRecipes.registerCastingRecipe(input, output, feldspar, 1500);
	}

	private static void mixIntermediate(IndustrialMaterialItem<Intermediates> item, Intermediates material) {
		IngredientList inputs = RecipeUtils.getMixtureInputs(item.getForm(), material);
		genericRecipes.registerMixingRecipe(inputs, item.getItemStack(material));
	}
	
	private static void makeAsh() {
		ItemStack bonemeal = new ItemStack(Items.dye, 1, 15);
		recipes.registerRoastingRecipe(Collections.singletonList(bonemeal), 
				FabricaItems.INTERMEDIATE_DUST.getItemStack(Intermediates.ASH), null, 1000);
	}
	
	private static void fillPigments() {
		for (ItemStack dye : OreDictionary.getOres("dye")) {
			ItemStack doubleDye = dye.copy();
			doubleDye.stackSize = 2;
			genericRecipes.registerMixingRecipe(
					new IngredientList(new ItemIngredientStack(dye), new MaterialStack(Generics.FILLER)), 
					doubleDye);	
		}
	}

	private static void makeSodaAsh() {
		// TODO: Chemica needs to decompose NaHCO3 to Na2CO3
		// TODO: It also needs a reaction to convert NH4Cl back to NH3 (see notes) 
		float brineConcentration = (float)Solutions.PURIFIED_BRINE.getComponents().get(1).weight;
		Reaction reaction = Reaction.inSolutionOf(Compounds.NaCl, brineConcentration).
				         			 with(Compounds.CO2).
				         			 with(Compounds.NH3).
				         			 with(Compounds.H2O).
				         			 yields(Compounds.NaHCO3).
				         			 and(Compounds.NH4Cl);
		reactionRecipes.registerReaction(reaction);
	}

	private static void makePhosphoricAcid() {
		IngredientList solidInputs = new IngredientList(new MaterialStack(Forms.DUST, IndustrialMinerals.APATITE));
		FluidStack sulfuricAcid = IndustrialFluids.getCanonicalFluidStack(Compounds.H2SO4, State.AQUEOUS);
		FluidStack phosphoricAcid = IndustrialFluids.getCanonicalFluidStack(Compounds.H3PO4, State.AQUEOUS);
		ItemStack gypsum = GeologicaItems.ORE_MINERAL_DUST.getItemStack(Ores.GYPSUM);
		genericRecipes.registerMixingRecipe(solidInputs, sulfuricAcid, null, gypsum, phosphoricAcid, null, Condition.STP, null);
	}

	private static void activateCarbon() {
		// TODO: Carbon activation:
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
