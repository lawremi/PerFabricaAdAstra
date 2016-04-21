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
import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.Compound;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.ConstructionMaterial;
import org.pfaa.chemica.model.Element;
import org.pfaa.chemica.model.Formula;
import org.pfaa.chemica.model.Formula.Part;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Metal;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.Reaction;
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
	private static final MaterialRecipeRegistry materialTarget = target.getMaterialRecipeRegistry();
	private static final ReactionRegistry reactionTarget = new ReactionRegistry(materialTarget);
	
	public static void addRegistry(String key, RecipeRegistry registry) {
		target.addRegistry(key, registry);
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
		registerDecompositionRecipes();
		registerSynthesisRecipes();
		
		// TODO: add metal tool recipes
		// - Valid metals/alloys: all types of steel, magnesium
		// - When GT loaded, tools are automatic, based on material system
		// - Should probably try to make tools balanced with Metallurgy/Railcraft tools
	}
	
	private static void registerAgglomerationRecipes() {
		registerBlockAndQuarterRecipes(ChemicaItems.AGGREGATE_PILE);
		registerPartitionRecipes(ChemicaItems.AGGREGATE_DUST, ChemicaItems.AGGREGATE_TINY_DUST);
		registerPartitionRecipes(ChemicaItems.COMPOUND_DUST, ChemicaItems.COMPOUND_TINY_DUST);
		registerPartitionRecipes(ChemicaItems.ELEMENT_DUST, ChemicaItems.ELEMENT_TINY_DUST);
		registerPartitionRecipes(ChemicaItems.ALLOY_DUST, ChemicaItems.ALLOY_TINY_DUST);
		registerPartitionRecipes(ChemicaItems.ELEMENT_INGOT, ChemicaItems.ELEMENT_NUGGET);
		registerPartitionRecipes(ChemicaItems.ALLOY_INGOT, ChemicaItems.ALLOY_NUGGET);
		registerBlockAndIngotRecipes();
		registerCastingAndGrindingRecipes();
	}

	public static <T extends Enum<?> & IndustrialMaterial> void registerPartitionRecipes(IndustrialMaterialItem<T> item, 
			IndustrialMaterialItem<T> partition) {
		int numPartitions = partition.getForm().getNumberPerBlock() / item.getForm().getNumberPerBlock();
		for(T material : partition.getIndustrialMaterials()) {
			ItemStack itemStack = item.getItemStack(material);
			ItemStack partitionStack = partition.getItemStack(material);
			registerPartitionRecipes(itemStack, partitionStack, numPartitions);
			registerFluidCastingRecipes(itemStack, material, item.getForm());
			registerFluidCastingRecipes(partitionStack, material, partition.getForm());
		}
	}

	private static void registerPartitionRecipes(ItemStack itemStack, ItemStack partitionStack, int numPartitions) {
		partitionStack = partitionStack.copy();
		partitionStack.stackSize = numPartitions;
		target.registerMixingRecipe(Collections.singletonList(partitionStack), itemStack);
		GameRegistry.addShapelessRecipe(partitionStack, itemStack);
	}

	private static void registerBlockAndQuarterRecipes(IndustrialMaterialItem<Aggregates> input) {
		for(Aggregates material : input.getIndustrialMaterials()) {
			ItemStack blockStack = new ItemStack(ChemicaBlocks.getBlock(material));
			ItemStack quarterStack = input.getItemStack(material);
			registerPartitionRecipes(blockStack, quarterStack, input.getForm().getNumberPerBlock());
		}
	}

	private static void registerBlockAndIngotRecipes() {
		for(ConstructionMaterialBlock block : ChemicaBlocks.getConstructionMaterialBlocks()) {
			for (ConstructionMaterial material : block.getConstructionMaterials()) {
				ItemStack blockStack = block.getItemStack(material);
				ItemStack ingotStack = OreDictUtils.lookupBest(Forms.INGOT, material);	
				if (material instanceof Metal) {
					registerPartitionRecipes(blockStack, ingotStack, Forms.INGOT.getNumberPerBlock());
					registerFluidCastingRecipes(blockStack, material, Forms.BLOCK);
				} else {
					registerPartitionRecipes(blockStack, ingotStack, 4);
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
				Compounds.CuO, Compounds.Cu2O, 
				Compounds.Cu2S, Compounds.Cu12As4S13, Compounds.Cu12Sb4S13);
		registerSmeltingRecipes(TemperatureLevel.HIGH,
				Compounds.CoO,  Compounds.Fe2O3, Compounds.Fe3O4, 
				Compounds.alpha_FeOH3, Compounds.gamma_FeOH3,
				Compounds.NiO, Compounds.Sb2S3);
		registerSmeltingRecipes(TemperatureLevel.VERY_HIGH, Compounds.Ni9S8, Compounds.SiO2);
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

	/* 
	 * Decomposition of salts:
	 *    * Hydroxide => oxide + water vapor
	 *    * Carbonate => oxide + carbon dioxide
	 *    * Nitrate => oxide + NO2 
	 *              => nitrite for alkali metals (except Li) 
	 *    * Nitrite => complex, special case
	 *  TODO:
	 *    * Chlorate => chloride + oxygen
	 *    * Bromate => bromide + oxygen
	 *    * Iodate => iodide + oxygen
	 *    * Sulfate => oxide + SO3
	 *    * Sulfite => oxide + SO2 ?
	 *    
	 * Easy to figure out the product, but tricky to balance the equation.
	 * Is it worth creating an equation solver? Or can we use adhoc approaches for now? 
	 */
	
	private static Reaction makeStandardSaltDecompositionReaction(Compound compound) {
		Part CO3 = new Part(Element.C, Element.O._(3)),
		     OH2 = new Part(Element.O, Element.H)._(2),
		     OH = new Part(Element.O, Element.H),
		     NO3 = new Part(Element.N, Element.O._(3)),
		     NO32 = new Part(Element.N, Element.O._(3))._(2),
		     NO2 = new Part(Element.N, Element.O._(2));
		Formula formula = compound.getFormula();
		boolean secondaryCarbonate = 
				formula.getParts().size() == 3 && 
				formula.getParts().get(1).hasComposition(CO3);
		if (!secondaryCarbonate && formula.getParts().size() > 2) {
			return null;
		}
		Part last = formula.getLastPart();
		Reaction reaction = null;
		if (last.equals(CO3)) {
			reaction = Reaction.of(compound).yields(oxide(formula)).and(Compounds.CO2);
		} else if (last.equals(NO3)) {
			if (formula.getFirstPart().element == Element.Li) {
				reaction = Reaction.of(4, compound).yields(2, oxide(formula)).and(4, Compounds.NO2).and(Compounds.O2);
			} else {
				Formula metalNitriteFormula = formula.withLastPart(NO2);
				Compound metalNitrite = Compounds.valueOf(metalNitriteFormula.toString());
				reaction = Reaction.of(2, compound).yields(2, metalNitrite).and(Compounds.O2);
			}
		} else if (last.equals(NO32)) {
			reaction = Reaction.of(2, compound).yields(2, oxide(formula)).and(4, Compounds.NO2).and(Compounds.O2);
		} else if (last.equals(OH2)) {
			int oxideStoich = formula.getFirstPart().stoichiometry;
			reaction = Reaction.of(compound).yields(oxideStoich, oxide(formula)).and(1, Compounds.H2O, State.GAS);
		} else if (last.equals(OH)) {
			reaction = Reaction.of(2, compound).yields(oxide(formula)).and(1, Compounds.H2O, State.GAS);
		}
		if (reaction != null && secondaryCarbonate) {
			reaction = reaction.and(formula.getParts().get(1).stoichiometry, Compounds.CO2);
		}
		return reaction;
	}
	
	private static Chemical oxide(Formula formula) {
		int ratio = Math.abs(Element.O.getDefaultOxidationState()) / formula.getFirstPart().element.getDefaultOxidationState();
		Formula oxideFormula = new Formula(formula.getFirstPart()._(ratio), Element.O);
		return Compounds.valueOf(oxideFormula.toString());
	}
	
	private static void registerDecompositionRecipes() {
		registerStandardSaltDecompositionRecipes();
		reactionTarget.registerThermalDecomposition(
				Reaction.of(4, Compounds.KNO2).yields(2, Compounds.K2O).and(2, Compounds.N2).and(3, Compounds.O2));
		reactionTarget.registerThermalDecomposition(
				Reaction.of(2, Compounds.NaNO2).yields(Compounds.Na2O).and(Compounds.NO).and(Compounds.NO2));
	}

	private static void registerStandardSaltDecompositionRecipes() {
		for (Compounds compound : Compounds.values()) {
			Reaction reaction = makeStandardSaltDecompositionReaction(compound);
			if (reaction == null) {
				continue;
			}
			reactionTarget.registerThermalDecomposition(reaction);
		}
	}
	
	private static void registerSynthesisRecipes() {
		reactionTarget.registerSynthesis(Reaction.of(Compounds.CaO).with(Compounds.H2O).yields(Compounds.CaOH2));
	}
}
