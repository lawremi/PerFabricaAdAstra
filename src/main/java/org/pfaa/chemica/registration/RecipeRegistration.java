package org.pfaa.chemica.registration;

import java.util.Arrays;

import org.pfaa.chemica.model.Aggregate.Aggregates;
import org.pfaa.chemica.model.Alloy;
import org.pfaa.chemica.model.Alloy.Alloys;
import org.pfaa.chemica.model.Catalysts;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Constants;
import org.pfaa.chemica.model.Element;
import org.pfaa.chemica.model.Generics;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.MaterialState;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.Reaction;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.processing.Separation;
import org.pfaa.chemica.processing.TemperatureLevel;

public class RecipeRegistration extends BaseRecipeRegistration {

	public static void init() {
		registerStackings();
		registerStateChanges();
		registerSolutionChanges();
		registerMetallurgicalRecipes();
		registerCatalystRecipes();
		registerReactions();
		registerElementRecipes();
	}
	
	private static void registerStackings() {
		REGISTRANT.stack(Aggregates.class);
		REGISTRANT.stack(Element.class);
		REGISTRANT.stack(Compounds.class);
		REGISTRANT.stack(Alloys.class);
	}

	private static void registerStateChanges() {
		registerElementStateChanges();
		registerAlloyStateChanges();
	}

	private static void registerElementStateChanges() {
		Arrays.stream(Element.values()).map(Element::getStandardChemical).forEach((chemical) -> {
			REGISTRANT.meltAndFreeze(chemical);
			if (chemical.getStandardState().isFluid()) {
				REGISTRANT.vaporizeAndCondense(chemical);
			}
		});
	}
	
	private static void registerAlloyStateChanges() {
		REGISTRANT.meltAndFreeze(Alloys.class);
	}
	
	private static void registerMetallurgicalRecipes() {
		registerCastingAndGrindingRecipes();
		registerSmeltingRecipes();
		registerAlloyingRecipes();
	}
	
	private static void registerCastingAndGrindingRecipes() {
		REGISTRANT.communite(Element.class);
		REGISTRANT.compact(Element.class);
		REGISTRANT.communite(Alloys.class);
		REGISTRANT.compact(Alloys.class);
	}
	
	private static void registerSmeltingRecipes() {
		REGISTRANT.smelt(TemperatureLevel.LOW,
				Compounds.Ag2S, Compounds.Bi2S3, Compounds.SnO2, Compounds.PbS);
		REGISTRANT.smelt(TemperatureLevel.MEDIUM,
				Compounds.CuO, Compounds.Cu2O,
				Compounds.Cu2S, Compounds.Cu12As4S13, Compounds.Cu12Sb4S13);
		REGISTRANT.smelt(TemperatureLevel.HIGH,
				Compounds.CoO,  Compounds.Fe2O3, Compounds.Fe3O4, 
				Compounds.alpha_FeOH3, Compounds.gamma_FeOH3,
				Compounds.NiO, Compounds.Sb2S3);
		REGISTRANT.smelt(TemperatureLevel.VERY_HIGH, Compounds.Ni9S8, Compounds.SiO2);
		REGISTRANT.smeltWithFlux(TemperatureLevel.HIGH, Generics.FLUX_SILICA, Compounds.CuFeS2);
	}

	private static <T extends Enum<?> & Alloy> void registerAlloyingRecipes() {
		REGISTRANT.alloy(Alloys.class);
	}
	
	private static void registerCatalystRecipes() {
		REGISTRANT.mix(Catalysts.class);
		// TODO: V2O5 (roast @1100K V ore with NaCl or Na2CO3 => NaVO3, then either:
		//             mix with H2SO4/HCl to yield V2O5 hydrate, roast to dehydrate
		//             or: dissolve and mix with NH4Cl => NH4VO3, roast to V2O5) 
	}
	
	private static void registerSolutionChanges() {
		REGISTRANT.dissolveAndUndissolve(Compounds.class);
	}
	
	private static void registerReactions() {
		registerDecompositionReactions();
		registerSynthesisReactions();
		registerRedoxReactions();
		registerSingleDisplacementReactions();
		registerDoubleDisplacementReactions();
	}
	
	private static void registerDecompositionReactions() {
		REGISTRANT.decompose(Compounds.class);
		CONVERSIONS.register(
				Reaction.of(4, Compounds.KNO2).yields(2, Compounds.K2O).and(2, Compounds.N2).and(3, Compounds.O2));
		CONVERSIONS.register(
				Reaction.of(2, Compounds.NaNO2).yields(Compounds.Na2O).and(Compounds.NO).and(Compounds.NO2));
		CONVERSIONS.register(
				Reaction.of(Compounds.NH4Cl).yields(Compounds.NH3).and(Compounds.HCl));
	}

	private static void registerSynthesisReactions() {
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
	
	private static void registerRedoxReactions() {
		registerCombustionReactions();
		CONVERSIONS.register(Reaction.of(2, Compounds.SO2).with(Compounds.O2).
				yields(2, Compounds.SO3).via(Compounds.V2O5).at(650));
		CONVERSIONS.register(Reaction.of(4, Compounds.FeCr2O4).
				with(Compounds.Na2CO3).with(7, Compounds.O2).
				yields(8, Compounds.Na2CrO4).and(2, Compounds.Fe2O3).and(8, Compounds.CO2).at(1300));
		CONVERSIONS.register(Reaction.of(2, Compounds.Na2Cr2O7_2H2O).with(2, Element.S).with(3, Compounds.O2).
				yields(4, Compounds.Cr2O3).and(2, Compounds.Na2SO4_10H2O).and(4, Compounds.H2O).at(850));
		CONVERSIONS.register(Reaction.of(4, Compounds.NH3).with(5, Compounds.O2).
				yields(4, Compounds.NO).and(6, Compounds.H2O).via(Element.Pt).at(1100));
		CONVERSIONS.register(Reaction.of(Compounds.NaNO3).with(Element.Pb).
				yields(Compounds.NaNO2).and(Compounds.PbO).at(600));
	}

	private static void registerElementRecipes() {
		makeHydrogen();
		makeSulfur();
	}
	
	private static void makeSulfur() {
		// Claus reaction
		CONVERSIONS.register(Reaction.of(2, Compounds.H2S).with(Compounds.SO2).
				yields(3, Element.S).and(2, Compounds.H2O).
				via(Compounds.Al2O3, Compounds.TiO2).at(Element.S.getFusion().getTemperature()));
	}

	private static void registerCombustionReactions() {
		CONVERSIONS.register(Reaction.of(1, Element.S, State.LIQUID).with(Compounds.O2).yields(Compounds.SO2).at(480));
		CONVERSIONS.register(Reaction.of(2, Compounds.H2S).with(3, Compounds.O2).
				yields(2, Compounds.SO2).and(2, Compounds.H2O).at(505));
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
		MaterialState<Mixture> richAmine = abs.getSeparatedMixture(0);
		Separation regen = Separation.of(richAmine).
				at(400).
				extracts(State.GAS.of(Compounds.CO2)).
				by(Separation.Axis.SOLUBILITY);
		CONVERSIONS.register(regen);
		return abs.getResiduum();
	}
	
	private static void registerSingleDisplacementReactions() {
		CONVERSIONS.register(Reaction.of(2, Compounds.NaOH).with(Compounds.NO).with(Compounds.NO2).
				yields(2, Compounds.NaNO2).and(Compounds.H2O));
		CONVERSIONS.register(Reaction.of(2, Compounds.Na2CO3).with(Compounds.NO).with(Compounds.NO2).
				yields(2, Compounds.NaNO2).and(Compounds.CO2));
	}
	
	private static void registerDoubleDisplacementReactions() {
		registerHydrolysisReactions();
		registerPrecipitationReactions();
	}

	private static void registerHydrolysisReactions() {
		CONVERSIONS.register(Reaction.of(3, Compounds.NO2).with(Compounds.H2O).yields(2, Compounds.HNO3).and(Compounds.NO));
	}

	private static void registerPrecipitationReactions() {
		CONVERSIONS.register(Reaction.inWaterOf(Compounds.H2SO4).
				with(2, Compounds.Na2CrO4).with(10, Compounds.H2O).
				yields(Compounds.Na2Cr2O7_2H2O).and(1, Compounds.Na2SO4_10H2O, State.SOLID).and(Compounds.H2O));
		CONVERSIONS.register(Reaction.inWaterOf(2, Compounds.Na2CrO4).
				with(2, Compounds.CO2).with(Compounds.H2O).
				yields(Compounds.Na2Cr2O7_2H2O).and(2, Compounds.NaHCO3, State.SOLID));
	}
}
