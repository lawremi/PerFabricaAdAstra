package org.pfaa.fabrica.registration;

import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.ChemicaItems;
import org.pfaa.chemica.fluid.IndustrialFluids;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.item.MaterialStack;
import org.pfaa.chemica.model.Aggregate.Aggregates;
import org.pfaa.chemica.model.Constants;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.registration.MaterialRecipeRegistry;
import org.pfaa.chemica.registration.RecipeRegistry;
import org.pfaa.chemica.registration.RecipeUtils;
import org.pfaa.chemica.util.ChanceStack;
import org.pfaa.fabrica.FabricaItems;
import org.pfaa.fabrica.model.Generic.Generics;
import org.pfaa.fabrica.model.Intermediate.Intermediates;
import org.pfaa.geologica.processing.IndustrialMineral.IndustrialMinerals;
import org.pfaa.geologica.processing.OreMineral.Ores;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeRegistration {
	private static final RecipeRegistry recipes = 
			org.pfaa.chemica.registration.RecipeRegistration.getTarget();
	private static final MaterialRecipeRegistry materialRecipes = 
			org.pfaa.chemica.registration.RecipeRegistration.getMaterialTarget();;
	
	public static void init() {
		grindIntermediates();
		hydrateHardenedClay();
		useFeldsparAsFlux();
		makePortlandCement();
		makeConcrete();
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
		materialRecipes.registerAbsorptionRecipe(
				new MaterialStackList(new MaterialStack(Forms.DUST, Aggregates.HARDENED_CLAY)),
				new FluidStack(FluidRegistry.WATER, IndustrialFluids.getAmount(Forms.DUST)),
				new ItemStack(Items.clay_ball),
				Constants.STANDARD_TEMPERATURE);
	}

	private static void grindIntermediates() {
		for (Intermediates material : Intermediates.values()) {
			ItemStack input = FabricaItems.INTERMEDIATE_CRUSHED.getItemStack(material);
			ItemStack output = FabricaItems.INTERMEDIATE_DUST.getItemStack(material);
			recipes.registerGrindingRecipe(input, output, Collections.<ChanceStack>emptyList(), Strength.WEAK);
		}
	}

	private static void makePortlandCement() {
		ItemStack clinker = FabricaItems.INTERMEDIATE_CRUSHED.getItemStack(Intermediates.PORTLAND_CLINKER, 4);	
		MaterialStackList inputs = new MaterialStackList(
				new MaterialStack(Forms.DUST, Ores.CALCITE, 3),
				new MaterialStack(Forms.CLUMP, Aggregates.CLAY));
		materialRecipes.registerRoastingRecipe(inputs, clinker, 1700);
		mixIntermediate(FabricaItems.INTERMEDIATE_CRUSHED, Intermediates.PORTLAND_CEMENT);
	}

	private static void makeConcrete() {
		/*
		 * We will support two types of cement:
		 * - Portland
		 * - Pozzolanic: mixing one of the below materials with Ca(OH)2 and water
		 *   - Volcanic ash, pumice, zeolite 
		 *   - Calcined diatomite
		 *   - Ground clay bricks (need clay dust!), calcined kaolinite
		 *   - Ground slag
		 *   - Organic ash from coal and bone (need a way to obtain fly ash!)
		 *     - Could detect furnace output by registering a handler via Container.addCraftingToCrafters()
		 *   - Silica fume (SiO) released during silicon production in arc furnace
		 */
		List<ItemStack> concretes = OreDictionary.getOres("concrete");
		if (concretes.size() > 0) {
			ItemStack concrete = concretes.get(0).copy();
			concrete.stackSize = 8;
			MaterialStackList inputs = new MaterialStackList(
					new MaterialStack(Aggregates.GRAVEL, 2),
					new MaterialStack(Aggregates.SAND),
					new MaterialStack(Forms.DUST, Generics.CEMENT));
			FluidStack water = new FluidStack(FluidRegistry.WATER, IndustrialFluids.getAmount(Forms.DUST));
			materialRecipes.registerAbsorptionRecipe(inputs, water, concrete, Constants.STANDARD_TEMPERATURE);
		}
		// TODO: geopolymer concrete
	}

	private static void useFeldsparAsFlux() {
		ItemStack output = new ItemStack(Blocks.glass);
		ItemStack input = new ItemStack(Blocks.sand);
		MaterialStack feldspar = new MaterialStack(Forms.DUST_TINY, IndustrialMinerals.FELDSPAR);
		materialRecipes.registerCastingRecipe(input, output, feldspar, 1500);
		
		output = new ItemStack(Items.brick);
		input = new ItemStack(Items.clay_ball);
		materialRecipes.registerCastingRecipe(input, output, feldspar, 1500);
	}

	private static void mixIntermediate(IndustrialMaterialItem<Intermediates> item, Intermediates material) {
		MaterialStackList inputs = RecipeUtils.getMixtureInputs(item.getForm(), material);
		materialRecipes.registerMixingRecipe(inputs, item.getItemStack(material));
	}
}
