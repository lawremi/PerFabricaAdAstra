package org.pfaa.chemica.registration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.ChemicaBlocks;
import org.pfaa.chemica.ChemicaItems;
import org.pfaa.chemica.block.ConstructionMaterialBlock;
import org.pfaa.chemica.fluid.IndustrialFluids;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.item.IngredientStack;
import org.pfaa.chemica.item.MaterialStack;
import org.pfaa.chemica.model.Aggregate.Aggregates;
import org.pfaa.chemica.model.Alloy;
import org.pfaa.chemica.model.Catalysts;
import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Constants;
import org.pfaa.chemica.model.ConstructionMaterial;
import org.pfaa.chemica.model.Element;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.MaterialState;
import org.pfaa.chemica.model.Metal;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.Reaction;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.processing.Separation;
import org.pfaa.chemica.processing.TemperatureLevel;
import org.pfaa.chemica.util.ChanceStack;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeRegistration extends BaseRecipeRegistration {

	public static void init() {
		registerAgglomerationRecipes();
		registerMetallurgicalRecipes();
		registerFreezingRecipes();
		registerCatalystRecipes();
		registerDissolutionRecipes();
		registerDecompositionRecipes();
		registerSynthesisRecipes();
		registerRedoxRecipes();
		registerSingleDisplacementRecipes();
		registerDoubleDisplacementRecipes();
		registerCrystallizationRecipes();
		
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
					registerCastingRecipes(blockStack, (Metal)material, Forms.BLOCK);
				} else {
					registerPartitionRecipes(blockStack, ingotStack, 4);
				}
			}
		}
	}
	
	private static void registerMetallurgicalRecipes() {
		registerCastingAndGrindingRecipes();
		registerSmeltingRecipes();
		registerAlloyingRecipes();
	}
	
	private static void registerCastingRecipes(ItemStack itemStack, Metal metal, Form form) {
		if (metal.getFusion() != null) {
			FluidStack fluid = IndustrialFluids.getCanonicalFluidStack(metal, State.LIQUID, form);
			RECIPES.registerMeltingRecipe(itemStack, fluid, fluid.getFluid().getTemperature());
			if (form == Forms.INGOT || form == Forms.BLOCK) {
				RECIPES.registerCastingRecipe(fluid, itemStack);
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
			RECIPES.registerCastingRecipe(dust, intact, null, castingTemp);
			RECIPES.registerGrindingRecipe(intact, dust, Collections.<ChanceStack> emptyList(), castable.getStrength());
			registerCastingRecipes(intact, castable, output.getForm());
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
		registerFluxedSmeltingRecipes(temp, null, null, compounds);
	}
	
	private static void registerFluxedSmeltingRecipes(TemperatureLevel temp, Compounds flux, Compounds... compounds) {
		registerFluxedSmeltingRecipes(temp, MaterialStack.of(flux, Forms.DUST), 
				MaterialStack.of(flux, Forms.DUST_TINY), compounds);
		if (flux == Compounds.SiO2) {
			registerFluxedSmeltingRecipes(temp, MaterialStack.of(Aggregates.SAND, null), 
					MaterialStack.of(Aggregates.SAND, Forms.PILE), compounds);
		}
	}
	
	private static void registerFluxedSmeltingRecipes(TemperatureLevel level, 
			IngredientStack fluxStack, IngredientStack tinyFluxStack, Compounds... compounds) {
		int temp = level.getReferenceTemperature();
		for (Compounds compound : compounds) {
			Element metal = compound.getFormula().getFirstPart().element;
			int amount = compound.getFormula().getFirstPart().stoichiometry;
			GENERICS.registerSmeltingRecipe(
					ChemicaItems.COMPOUND_DUST.getItemStack(compound), 
					ChemicaItems.ELEMENT_INGOT.getItemStack(metal, amount), fluxStack, temp);
			GENERICS.registerSmeltingRecipe(
					ChemicaItems.COMPOUND_TINY_DUST.getItemStack(compound), 
					ChemicaItems.ELEMENT_NUGGET.getItemStack(metal, amount), tinyFluxStack, temp);
			int fluidAmount = IndustrialFluids.getAmount(Forms.INGOT) * amount;
			FluidStack molten = IndustrialFluids.getCanonicalFluidStack(metal, State.LIQUID, fluidAmount);
			GENERICS.registerSmeltingRecipe(ChemicaItems.COMPOUND_DUST.getItemStack(compound), molten, fluxStack, temp);
			molten = molten.copy();
			molten.amount = IndustrialFluids.getAmount(Forms.NUGGET) * amount;
			GENERICS.registerSmeltingRecipe(ChemicaItems.COMPOUND_TINY_DUST.getItemStack(compound), molten, tinyFluxStack, temp);
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
		RECIPES.registerAlloyingRecipe(output, base, solutes, avgMeltingTemp);
		base = ChemicaItems.ELEMENT_INGOT.getItemStack((Element)comps.get(0).material, inputs.get(0).stackSize);
		RECIPES.registerAlloyingRecipe(output, base, solutes, avgMeltingTemp);
		if (canMixFluids) {
			RECIPES.registerAlloyingRecipe(fluidOutput, fluidInputs);
		}
		return totalIngots;
	}
	
	private static void registerAlloyingRecipes() {
		registerAlloyingRecipes(ChemicaItems.ALLOY_INGOT);
	}
	
	private static void registerFreezingRecipes() {
		registerFreezingRecipes(ChemicaItems.ELEMENT_TINY_DUST);
		// Would add a lot of unneeded recipes...
		//registerFreezingRecipes(ChemicaItems.COMPOUND_TINY_DUST);
	}
	
	private static <T extends Enum<?> & Chemical> void registerFreezingRecipes(IndustrialMaterialItem<T> item) {
		for (T chemical : item.getIndustrialMaterials()) {
			if (chemical instanceof Metal && ((Metal) chemical).getStrength() != null) {
				continue;
			}
			FluidStack molten = IndustrialFluids.getCanonicalFluidStack(chemical, State.LIQUID, item.getForm());
			// FIXME: should be total enthalpy to freeze from canonical liquid condition
			RECIPES.registerCoolingRecipe(molten, item.getItemStack(chemical), (int)chemical.getEnthalpyOfFusion());
		}
	}

	private static void registerCatalystRecipes() {
		for (Catalysts catalyst : ChemicaItems.CATALYST_DUST.getIndustrialMaterials()) {
			mixCatalyst(ChemicaItems.CATALYST_DUST, catalyst);
		}
		// TODO: V2O5 (roast with Na2CO3 => NaVO3, mix with H2SO4/HCl to yield V2O5 hydrate, roast to dehydrate) 
	}
	
	private static void mixCatalyst(IndustrialMaterialItem<Catalysts> item, Catalysts material) {
		IngredientList inputs = RecipeUtils.getMixtureInputs(item.getForm(), material);
		GENERICS.registerMixingRecipe(inputs, item.getItemStack(material));
	}

	private static void registerDissolutionRecipes() {
		registerDissolutionRecipes(ChemicaItems.COMPOUND_DUST);
		registerDissolutionRecipes(ChemicaItems.COMPOUND_TINY_DUST);
	}

	private static <T extends Enum<?> & Chemical> void registerDissolutionRecipes(IndustrialMaterialItem<T> item) {
		int amount = IndustrialFluids.getAmount(State.AQUEOUS, item.getForm());
		FluidStack water = IndustrialFluids.getCanonicalFluidStack(Compounds.H2O, State.LIQUID, amount);
		for (T chemical : item.getIndustrialMaterials()) {
			FluidStack solution = IndustrialFluids.getCanonicalFluidStack(chemical, State.AQUEOUS, amount);
			ItemStack solute = item.getItemStack(chemical);
			Condition condition = chemical.getDissociation().getCondition();
			RECIPES.registerMixingRecipe(Arrays.asList(solute), water, null, null, solution, null, condition, null);
		}
	}
	
	private static void registerDecompositionRecipes() {
		registerStandardSaltDecompositionRecipes();
		CONVERSIONS.register(
				Reaction.of(4, Compounds.KNO2).yields(2, Compounds.K2O).and(2, Compounds.N2).and(3, Compounds.O2));
		CONVERSIONS.register(
				Reaction.of(2, Compounds.NaNO2).yields(Compounds.Na2O).and(Compounds.NO).and(Compounds.NO2));
		CONVERSIONS.register(
				Reaction.of(Compounds.NH4Cl).yields(Compounds.NH3).and(Compounds.HCl));
	}

	private static void registerStandardSaltDecompositionRecipes() {
		for (Compounds compound : Compounds.values()) {
			Reaction reaction = ReactionFactory.makeStandardSaltDecompositionReaction(compound);
			if (reaction == null) {
				continue;
			}
			CONVERSIONS.register(reaction);
		}
	}

	private static void registerSynthesisRecipes() {
		CONVERSIONS.register(Reaction.of(Compounds.CaO).with(Compounds.H2O).yields(Compounds.CaOH2));
		CONVERSIONS.register(Reaction.of(Compounds.N2).with(3, Compounds.H2).yields(2, Compounds.NH3, State.GAS).
				at(new Condition(725, 101*Constants.STANDARD_PRESSURE)).via(Element.Fe));
		CONVERSIONS.register(Reaction.of(Compounds.SO3).with(Compounds.H2SO4).yields(2, Compounds.H2S2O7));
		CONVERSIONS.register(Reaction.of(Compounds.H2S2O7).with(Compounds.H2O).yields(2, Compounds.H2SO4));
		CONVERSIONS.register(Reaction.inAirOf(2, Compounds.ETHENE).with(Compounds.O2).
				yields(2, Compounds.OXIRANE).via(Element.Ag).at(500));
		CONVERSIONS.register(Reaction.of(Compounds.OXIRANE).with(Compounds.NH3).
				yields(Compounds.ETHANOLAMINE).at(323));
		CONVERSIONS.register(
				Reaction.inAirOf(2, Compounds.H2O).with(4, Compounds.NO).with(2, Compounds.SO2).with(Compounds.O2).
				yields(2, Compounds.H2SO4, State.AQUEOUS).and(4, Compounds.NO));
		CONVERSIONS.register(
				Reaction.of(2, Compounds.H2O).with(2, Compounds.NO).with(2, Compounds.NO2).with(2, Compounds.SO2).
				yields(2, Compounds.H2SO4, State.AQUEOUS).and(4, Compounds.NO));
	}
	
	private static void registerRedoxRecipes() {
		makeHydrogen();
		makeSulfur();
		makeOxidationRecipes();
		CONVERSIONS.register(Reaction.of(Compounds.NaNO3).with(Element.Pb).
				yields(Compounds.NaNO2).and(Compounds.PbO).at(600));
	}
	
	private static void makeSulfur() {
		// Claus reaction
		CONVERSIONS.register(Reaction.of(2, Compounds.H2S).with(Compounds.SO2).
				yields(3, Element.S).and(2, Compounds.H2O).
				via(Compounds.Al2O3, Compounds.TiO2).at(Element.S.getFusion().getTemperature()));
	}

	private static void makeCombustionRecipes() {
		CONVERSIONS.register(Reaction.of(1, Element.S, State.LIQUID).with(Compounds.O2).yields(Compounds.SO2).at(480));
		CONVERSIONS.register(Reaction.of(2, Compounds.H2S).with(3, Compounds.O2).
				yields(2, Compounds.SO2).and(2, Compounds.H2O).at(505));
	}

	private static void makeOxidationRecipes() {
		makeCombustionRecipes();
		CONVERSIONS.register(Reaction.of(2, Compounds.SO2).with(Compounds.O2).
				yields(2, Compounds.SO3).via(Compounds.V2O5).at(650));
		CONVERSIONS.register(Reaction.of(4, Compounds.FeCr2O4).
				with(Compounds.Na2CO3).with(7, Compounds.O2).
				yields(8, Compounds.Na2CrO4).and(2, Compounds.Fe2O3).and(8, Compounds.CO2).at(1300));
		CONVERSIONS.register(Reaction.of(2, Compounds.Na2Cr2O7_2H2O).with(2, Element.S).with(3, Compounds.O2).
				yields(4, Compounds.Cr2O3).and(2, Compounds.Na2SO4_10H2O).and(4, Compounds.H2O).at(850));
		CONVERSIONS.register(Reaction.of(4, Compounds.NH3).with(5, Compounds.O2).
				yields(4, Compounds.NO).and(6, Compounds.H2O).via(Element.Pt).at(1100));
	}
	
	private static void makeHydrogen() {
		Reaction makeSyngas = Reaction.of(Compounds.METHANE).with(Compounds.H2O, State.GAS).
				yields(Compounds.CO).and(3, Compounds.H2).via(Element.Ni);
		CONVERSIONS.register(makeSyngas);
		Reaction makeWaterGas = Reaction.of(Compounds.CO).with(Compounds.H2O, State.GAS).
				yields(Compounds.CO2).and(Compounds.H2).via(Catalysts.HTS);
		CONVERSIONS.register(makeWaterGas);
		absorbCO2(makeWaterGas.getProduct());
		makeWaterGas = Reaction.of(Compounds.H2O, State.GAS).with(Compounds.CO).with(3, Compounds.H2).
				yields(Compounds.CO2).and(4, Compounds.H2).via(Catalysts.HTS);
		absorbCO2(makeWaterGas.getProduct());
	}
	
	public static MaterialState<?> absorbCO2(Mixture mixture) 
	{
		MaterialState<IndustrialMaterial> ethanolamine = State.AQUEOUS.of(Compounds.ETHANOLAMINE); 
		Separation abs = Separation.of(mixture).
				with(ethanolamine).
				extracts(State.GAS.of(Compounds.CO2)).
				by(Separation.Axis.SOLUBILITY);
		CONVERSIONS.register(abs);
		MaterialState<Mixture> richAmine = abs.getSeparated();
		Separation regen = Separation.of(richAmine).
				at(400).
				extracts(State.GAS.of(Compounds.CO2)).
				by(Separation.Axis.SOLUBILITY);
		CONVERSIONS.register(regen);
		return abs.getResiduum();
	}
	
	private static void registerSingleDisplacementRecipes() {
		CONVERSIONS.register(Reaction.of(2, Compounds.NaOH).with(Compounds.NO).with(Compounds.NO2).
				yields(2, Compounds.NaNO2).and(Compounds.H2O));
		CONVERSIONS.register(Reaction.of(2, Compounds.Na2CO3).with(Compounds.NO).with(Compounds.NO2).
				yields(2, Compounds.NaNO2).and(Compounds.CO2));
	}
	
	private static void registerDoubleDisplacementRecipes() {
		registerHydrolysisRecipes();
		registerPrecipitationRecipes();
	}

	private static void registerHydrolysisRecipes() {
		CONVERSIONS.register(Reaction.of(3, Compounds.NO2).with(Compounds.H2O).yields(2, Compounds.HNO3).and(Compounds.NO));
	}

	private static void registerPrecipitationRecipes() {
		CONVERSIONS.register(Reaction.inWaterOf(Compounds.H2SO4).
				with(2, Compounds.Na2CrO4).with(10, Compounds.H2O).
				yields(Compounds.Na2Cr2O7_2H2O).and(1, Compounds.Na2SO4_10H2O, State.SOLID).and(Compounds.H2O));
		CONVERSIONS.register(Reaction.inWaterOf(2, Compounds.Na2CrO4).
				with(2, Compounds.CO2).with(Compounds.H2O).
				yields(Compounds.Na2Cr2O7_2H2O).and(2, Compounds.NaHCO3, State.SOLID));
	}
	
	private static void registerCrystallizationRecipes() {
		registerCrystallizationRecipe(Compounds.Na2Cr2O7_2H2O);
	}

	private static void registerCrystallizationRecipe(Compounds compound) {
		FluidStack input = IndustrialFluids.getCanonicalFluidStack(compound, State.AQUEOUS);
		ItemStack output = ChemicaItems.COMPOUND_TINY_DUST.getItemStack(compound);
		RECIPES.registerPrecipitationRecipe(input, output, (int)compound.getSolubility());
	}

}
