package org.pfaa.chemica.registration;

import java.util.Collections;

import org.pfaa.chemica.ChemicaBlocks;
import org.pfaa.chemica.ChemicaItems;
import org.pfaa.chemica.block.ConstructionMaterialBlock;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.model.Aggregate.Aggregates;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Element;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Metal;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.util.ChanceStack;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

public class RecipeRegistration {
	
	private static final CombinedRecipeRegistry target = new CombinedRecipeRegistry();

	public static CombinedRecipeRegistry getTarget() {
		return target;
	}
	
	public static void init() {
		registerAgglomerationRecipes();
		registerCastingAndGrindingRecipes();
		registerSmeltingRecipes();
		
		// TODO: add metal tool recipes, once we have ingots
		// - Valid metals/alloys: all types of steel, magnesium
		// - When GT loaded, tools are automatic, based on material system
		// - Should probably try to make tools balanced with Metallurgy/Railcraft tools
		// - No need for a plain steel pickaxe when those mods are loaded
		//   - Perhaps we could just check if the oredict-based recipe already exists?
		// - When Tinker's Construct is loaded, we skip this (but our materials are registered)
	}
	
	private static void registerAgglomerationRecipes() {
		registerBlockAndQuarterRecipes(ChemicaItems.AGGREGATE_PILE);
		registerDustAndTinyDustRecipes(ChemicaItems.AGGREGATE_DUST, ChemicaItems.AGGREGATE_TINY_DUST);
		registerDustAndTinyDustRecipes(ChemicaItems.COMPOUND_DUST, ChemicaItems.COMPOUND_TINY_DUST);
		registerDustAndTinyDustRecipes(ChemicaItems.ELEMENT_DUST, ChemicaItems.ELEMENT_TINY_DUST);
		registerDustAndTinyDustRecipes(ChemicaItems.ALLOY_DUST, ChemicaItems.ALLOY_TINY_DUST);
		registerBlockAndIngotRecipes();
	}

	private static <T extends Enum<?> & IndustrialMaterial> void registerDustAndTinyDustRecipes(IndustrialMaterialItem<T> dust, 
			IndustrialMaterialItem<T> tinyDust) {
		for(T material : tinyDust.getIndustrialMaterials()) {
			ItemStack dustStack = dust.getItemStack(material);
			ItemStack tinyStack = tinyDust.getItemStack(material);
			GameRegistry.addRecipe(dustStack, "###", "###", "###", '#', tinyStack.copy());
			tinyStack.stackSize = 9;
			GameRegistry.addShapelessRecipe(tinyStack, dustStack);
		}
	}

	private static void registerBlockAndQuarterRecipes(IndustrialMaterialItem<Aggregates> input) {
		for(Aggregates material : input.getIndustrialMaterials()) {
			ItemStack blockStack = new ItemStack(ChemicaBlocks.getBlock(material));
			ItemStack quarterStack = input.getItemStack(material);
			GameRegistry.addRecipe(blockStack, "##", "##", '#', quarterStack.copy());
			quarterStack.stackSize = 4;
			GameRegistry.addShapelessRecipe(quarterStack, blockStack);
		}
	}

	private static void registerBlockAndIngotRecipes() {
		for(ConstructionMaterialBlock block : ChemicaBlocks.getConstructionMaterialBlocks()) {
			for (IndustrialMaterial material : block.getConstructionMaterials()) {
				ItemStack blockStack = new ItemStack(block);
				ItemStack ingotStack = OreDictUtils.lookupBest(Forms.INGOT, material);
				if (material instanceof Metal) {
					GameRegistry.addRecipe(blockStack, "###", "###", "###", '#', ingotStack.copy());
					ingotStack.stackSize = 9;
				} else {
					GameRegistry.addRecipe(blockStack, "##", "##", '#', ingotStack.copy());
					ingotStack.stackSize = 4;
				}
				GameRegistry.addShapelessRecipe(ingotStack, blockStack);
			}
		}
	}
	
	private static void registerCastingAndGrindingRecipes() {
		registerCastingAndGrindingRecipes(ChemicaItems.ELEMENT_DUST, ChemicaItems.METAL_INGOT);
		registerCastingAndGrindingRecipes(ChemicaItems.ELEMENT_TINY_DUST, ChemicaItems.METAL_NUGGET);
		// TODO: once we have melting points for alloys...
		//registerCastingRecipes(ChemicaItems.ALLOY_DUST, ChemicaItems.ALLOY_INGOT);
		//registerCastingRecipes(ChemicaItems.ALLOY_TINY_DUST, ChemicaItems.ALLOY_NUGGET);
	}
	
	private static <T extends Enum<?> & Metal> void registerCastingAndGrindingRecipes(
			IndustrialMaterialItem<T> input, IndustrialMaterialItem<T> output)
	{
		for (T metal : output.getIndustrialMaterials()) {
			int castingTemp = metal.getFusion().getTemperature();
			ItemStack dust = input.getItemStack(metal);
			ItemStack intact = output.getItemStack(metal);
			target.registerCastingRecipe(dust, intact, castingTemp);
			target.registerGrindingRecipe(intact, dust, Collections.<ChanceStack> emptyList(), metal.getStrength());
		}
	}

	private static void registerSmeltingRecipes() {
		registerSmeltingRecipes(TemperatureLevel.LOW, null, 
				Compounds.Ag2S, Compounds.Bi2S3, Compounds.SnO2, Compounds.PbS);
		registerSmeltingRecipes(TemperatureLevel.MEDIUM, null,
				Compounds.Cu2O, Compounds.Cu2CO3OH2, Compounds.Cu2S, Compounds.Cu12As4S13, Compounds.Cu12Sb4S13);
		registerSmeltingRecipes(TemperatureLevel.HIGH, null,
				Compounds.CoO,  Compounds.Fe2O3, Compounds.Fe3O4, 
				Compounds.alpha_FeOH3, Compounds.gamma_FeOH3,
				Compounds.NiO, Compounds.Sb2S3);
		registerSmeltingRecipes(TemperatureLevel.VERY_HIGH, null, Compounds.Ni9S8);
		registerSmeltingRecipes(TemperatureLevel.HIGH, Compounds.SiO2, Compounds.CuFeS2);
	}
	
	private static void registerSmeltingRecipes(TemperatureLevel temp, Compounds flux, Compounds... compounds) {
		ItemStack fluxStack = ChemicaItems.COMPOUND_DUST.getItemStack(flux); // TODO: maybe change to tiny dust?
		for (Compounds compound : compounds) {
			Element metal = compound.getFormula().getFirstPart().element;
			target.registerSmeltingRecipe(
					ChemicaItems.COMPOUND_DUST.getItemStack(compound), 
					ChemicaItems.METAL_INGOT.getItemStack(metal), fluxStack, temp);
			target.registerSmeltingRecipe(
					ChemicaItems.COMPOUND_TINY_DUST.getItemStack(compound), 
					ChemicaItems.METAL_NUGGET.getItemStack(metal), fluxStack, temp);
		}
	}
}
