package org.pfaa.chemica.registration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.ChemicaBlocks;
import org.pfaa.chemica.ChemicaItems;
import org.pfaa.chemica.block.ConstructionMaterialBlock;
import org.pfaa.chemica.fluid.IndustrialFluids;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.model.Aggregate.Aggregates;
import org.pfaa.chemica.model.Alloy;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.ConstructionMaterial;
import org.pfaa.chemica.model.Element;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Metal;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.util.ChanceStack;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeRegistration {
	
	private static final CombinedRecipeRegistry target = new CombinedRecipeRegistry();
	private static final CombinedMaterialRecipeRegistry materialTarget = new CombinedMaterialRecipeRegistry();
	
	public static void addRegistry(String key, RecipeRegistry registry) {
		target.addRegistry(key, registry);
		materialTarget.addRegistry(key, registry.getMaterialRecipeRegistry());
	}
	
	public static RecipeRegistry getTarget() {
		return target;
	}
	
	public static MaterialRecipeRegistry getMaterialTarget() {
		return materialTarget;
	}
	
	public static void init() {
		registerSmeltingRecipes();
		registerAlloyingRecipes();
		registerAgglomerationRecipes();
		
		// TODO: add metal tool recipes
		// - Valid metals/alloys: all types of steel, magnesium
		// - When GT loaded, tools are automatic, based on material system
		// - Should probably try to make tools balanced with Metallurgy/Railcraft tools
	}
	
	private static void registerAgglomerationRecipes() {
		registerBlockAndQuarterRecipes(ChemicaItems.AGGREGATE_PILE);
		registerItemAndNinthRecipes(ChemicaItems.AGGREGATE_DUST, ChemicaItems.AGGREGATE_TINY_DUST);
		registerItemAndNinthRecipes(ChemicaItems.COMPOUND_DUST, ChemicaItems.COMPOUND_TINY_DUST);
		registerItemAndNinthRecipes(ChemicaItems.ELEMENT_DUST, ChemicaItems.ELEMENT_TINY_DUST);
		registerItemAndNinthRecipes(ChemicaItems.ALLOY_DUST, ChemicaItems.ALLOY_TINY_DUST);
		registerItemAndNinthRecipes(ChemicaItems.ELEMENT_INGOT, ChemicaItems.ELEMENT_NUGGET);
		registerItemAndNinthRecipes(ChemicaItems.ALLOY_INGOT, ChemicaItems.ALLOY_NUGGET);
		registerBlockAndIngotRecipes();
		registerCastingAndGrindingRecipes();
	}

	private static <T extends Enum<?> & IndustrialMaterial> void registerItemAndNinthRecipes(IndustrialMaterialItem<T> item, 
			IndustrialMaterialItem<T> ninth) {
		for(T material : ninth.getIndustrialMaterials()) {
			ItemStack itemStack = item.getItemStack(material);
			ItemStack ninthStack = ninth.getItemStack(material);
			registerItemAndNinthRecipe(itemStack, ninthStack);
			registerFluidCastingRecipes(itemStack, material, item.getForm());
			registerFluidCastingRecipes(ninthStack, material, ninth.getForm());
		}
	}

	private static void registerItemAndNinthRecipe(ItemStack itemStack, ItemStack ninthStack) {
		GameRegistry.addRecipe(itemStack, "###", "###", "###", '#', ninthStack);
		ninthStack = ninthStack.copy();
		ninthStack.stackSize = 9;
		GameRegistry.addShapelessRecipe(ninthStack, itemStack);
	}

	private static void registerItemAndQuarterRecipe(ItemStack blockStack, ItemStack quarterStack) {
		GameRegistry.addRecipe(blockStack, "##", "##", '#', quarterStack);
		quarterStack = quarterStack.copy();
		quarterStack.stackSize = 4;
		GameRegistry.addShapelessRecipe(quarterStack, blockStack);
	}

	private static void registerBlockAndQuarterRecipes(IndustrialMaterialItem<Aggregates> input) {
		for(Aggregates material : input.getIndustrialMaterials()) {
			ItemStack blockStack = new ItemStack(ChemicaBlocks.getBlock(material));
			ItemStack quarterStack = input.getItemStack(material);
			registerItemAndQuarterRecipe(blockStack, quarterStack);
		}
	}

	private static void registerBlockAndIngotRecipes() {
		for(ConstructionMaterialBlock block : ChemicaBlocks.getConstructionMaterialBlocks()) {
			for (ConstructionMaterial material : block.getConstructionMaterials()) {
				ItemStack blockStack = block.getItemStack(material);
				ItemStack ingotStack = OreDictUtils.lookupBest(Forms.INGOT, material);	
				if (material instanceof Metal) {
					registerItemAndNinthRecipe(blockStack, ingotStack);
					registerFluidCastingRecipes(blockStack, material, Forms.BLOCK);
				} else {
					registerItemAndQuarterRecipe(blockStack, ingotStack);
				}
			}
		}
	}
	
	private static void registerFluidCastingRecipes(ItemStack itemStack, IndustrialMaterial material, Form form) {
		if (material instanceof Metal && ((Metal) material).getFusion() != null) {
			FluidStack fluid = IndustrialFluids.getCanonicalFluidStack(material, State.LIQUID, form);
			target.registerMeltingRecipe(itemStack, fluid, fluid.getFluid().getTemperature());
			if (form == Forms.INGOT || form == Forms.BLOCK) {
				target.registerCastingRecipe(fluid, itemStack);
			}
		}
	}
	
	private static void registerCastingAndGrindingRecipes() {
		registerCastingAndGrindingRecipes(ChemicaItems.ELEMENT_DUST, ChemicaItems.ELEMENT_INGOT);
		registerCastingAndGrindingRecipes(ChemicaItems.ELEMENT_TINY_DUST, ChemicaItems.ELEMENT_NUGGET);
		registerCastingAndGrindingRecipes(ChemicaItems.ALLOY_DUST, ChemicaItems.ALLOY_INGOT);
		registerCastingAndGrindingRecipes(ChemicaItems.ALLOY_TINY_DUST, ChemicaItems.ALLOY_NUGGET);
	}
	
	private static <T extends Enum<?> & Metal> void registerCastingAndGrindingRecipes(
			IndustrialMaterialItem<T> input, IndustrialMaterialItem<T> output)
	{
		for (T castable : output.getIndustrialMaterials()) {
			if (castable.getFusion() == null) {
				continue;
			}
			int castingTemp = castable.getFusion().getTemperature();
			ItemStack dust = input.getItemStack(castable);
			ItemStack intact = output.getItemStack(castable);
			target.registerCastingRecipe(dust, intact, null, castingTemp);
			target.registerGrindingRecipe(intact, dust, Collections.<ChanceStack> emptyList(), castable.getStrength());
		}
	}

	private static void registerSmeltingRecipes() {
		registerSmeltingRecipes(TemperatureLevel.LOW, 
				Compounds.Ag2S, Compounds.Bi2S3, Compounds.SnO2, Compounds.PbS);
		registerSmeltingRecipes(TemperatureLevel.MEDIUM,
				Compounds.Cu2O, Compounds.Cu2CO3OH2, Compounds.Cu2S, Compounds.Cu12As4S13, Compounds.Cu12Sb4S13);
		registerSmeltingRecipes(TemperatureLevel.HIGH,
				Compounds.CoO,  Compounds.Fe2O3, Compounds.Fe3O4, 
				Compounds.alpha_FeOH3, Compounds.gamma_FeOH3,
				Compounds.NiO, Compounds.Sb2S3);
		registerSmeltingRecipes(TemperatureLevel.VERY_HIGH, Compounds.Ni9S8);
		registerFluxedSmeltingRecipes(TemperatureLevel.HIGH, Compounds.SiO2, Compounds.CuFeS2);
	}
	
	private static void registerSmeltingRecipes(TemperatureLevel temp, Compounds... compounds) {
		registerFluxedSmeltingRecipes(temp, (ItemStack)null, compounds);
	}
	
	private static void registerFluxedSmeltingRecipes(TemperatureLevel temp, Compounds flux, Compounds... compounds) {
		ItemStack fluxStack = ChemicaItems.COMPOUND_DUST.getItemStack(flux); // TODO: maybe change to tiny dust?
		registerFluxedSmeltingRecipes(temp, fluxStack, compounds);
		if (flux == Compounds.SiO2) {
			registerFluxedSmeltingRecipes(temp, new ItemStack(Blocks.sand), compounds);
		}
	}
	
	private static void registerFluxedSmeltingRecipes(TemperatureLevel temp, ItemStack flux, Compounds... compounds) {
		for (Compounds compound : compounds) {
			Element metal = compound.getFormula().getFirstPart().element;
			target.registerSmeltingRecipe(
					ChemicaItems.COMPOUND_DUST.getItemStack(compound), 
					ChemicaItems.ELEMENT_INGOT.getItemStack(metal), flux, temp);
			target.registerSmeltingRecipe(
					ChemicaItems.COMPOUND_TINY_DUST.getItemStack(compound), 
					ChemicaItems.ELEMENT_NUGGET.getItemStack(metal), flux, temp);
			FluidStack molten = IndustrialFluids.getCanonicalFluidStack(metal, State.LIQUID, Forms.INGOT);
			target.registerSmeltingRecipe(ChemicaItems.COMPOUND_DUST.getItemStack(compound), molten, flux, temp);
			molten = molten.copy();
			molten.amount = IndustrialFluids.getAmount(Forms.NUGGET);
			target.registerSmeltingRecipe(ChemicaItems.COMPOUND_TINY_DUST.getItemStack(compound), molten, flux, temp);
		}
	}
	

	/*
	 * Up to two recipes per alloy.
	 * 
	 * Ingot-based: All inputs of fraction >= 0.1 are taken as (M*freq) ingots, where M=1/min_freq_above_0.1 
	 *              Lower fraction inputs are taken as (10*M*freq) nuggets, rounding up to 1 nugget.
	 *              Output is same as total input ingot count.
	 * 
	 * Nugget-based: Primary component is taken as 1 ingot, others are (10*freq) nugget. Output is one ingot.
	 */

	private static <T extends Enum<?> & Alloy> void registerAlloyingRecipes(IndustrialMaterialItem<T> item) {
		for (T alloy : item.getIndustrialMaterials()) {
			int numIngots = registerAlloyingRecipes(item, alloy, 0.1F);
			if (numIngots > 1) {
				registerAlloyingRecipes(item, alloy, 1.0F);
			}
		}
	}
	
	private static <T extends Enum<?> & Alloy> int registerAlloyingRecipes(IndustrialMaterialItem<T> item, T alloy, float minIngotWeight) {
		int totalMeltingTemp = 0;
		double totalWeight = 0, minSigFreq = Double.MAX_VALUE;
		List<MixtureComponent> comps = alloy.getComponents();
		for (MixtureComponent comp : comps) {
			Metal metal = (Metal)comp.material;
			if (metal.getFusion() != null) { // otherwise we assume it just dissolves (like C)
				totalMeltingTemp += metal.getFusion().getTemperature() * comp.weight;
				totalWeight += comp.weight;
			}
			if (comp.weight >= minIngotWeight && comp.weight < minSigFreq) {
				minSigFreq = comp.weight;
			}
		}
		double scale = 1 / minSigFreq;
		int avgMeltingTemp = (int) (totalMeltingTemp / totalWeight);
		List<ItemStack> inputs = new ArrayList<ItemStack>(comps.size());
		List<FluidStack> fluidInputs = new ArrayList<FluidStack>(comps.size());
		FluidStack fluidOutput = IndustrialFluids.getCanonicalFluidStack(alloy, State.LIQUID, Forms.INGOT);
		boolean canMixFluids = fluidOutput != null;
		int ingotAmount = IndustrialFluids.getAmount(Forms.INGOT);
		int totalIngots = 0;
		for (MixtureComponent comp : comps) {
			if (comp.weight >= minIngotWeight) {
				int numIngots = (int)Math.rint(scale * comp.weight);
				inputs.add(ChemicaItems.ELEMENT_DUST.getItemStack((Element)comp.material, numIngots));
				totalIngots += numIngots;
			} else {
				int numNuggets = Math.max((int)Math.rint(scale * 10 * comp.weight), 1);
				inputs.add(ChemicaItems.ELEMENT_TINY_DUST.getItemStack((Element)comp.material, numNuggets));
			}
			if (canMixFluids) {
				FluidStack fluid = IndustrialFluids.getCanonicalFluidStack(comp.material, State.LIQUID, 
						(int)Math.rint(ingotAmount * comp.weight / totalWeight));
				if (fluid == null) {
					canMixFluids = false;
				} else {
					fluidInputs.add(fluid);
				}
			}
		}
		ItemStack output = item.getItemStack(alloy, totalIngots);
		List<ItemStack> solutes = inputs.subList(1, inputs.size());
		ItemStack base = inputs.get(0);
		target.registerAlloyingRecipe(output, base, solutes, avgMeltingTemp);
		base = ChemicaItems.ELEMENT_INGOT.getItemStack((Element)comps.get(0).material, inputs.get(0).stackSize);
		target.registerAlloyingRecipe(output, base, solutes, avgMeltingTemp);
		if (canMixFluids) {
			target.registerAlloyingRecipe(fluidOutput, fluidInputs);
		}
		return totalIngots;
	}
	
	private static void registerAlloyingRecipes() {
		registerAlloyingRecipes(ChemicaItems.ALLOY_INGOT);
	}
}
