package org.pfaa.chemica.registration;

import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.Compound;
import org.pfaa.chemica.model.Element;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Formula;
import org.pfaa.chemica.model.Ion;
import org.pfaa.chemica.model.Ion.Ions;
import org.pfaa.chemica.model.Reaction;
import org.pfaa.chemica.model.State;

public class ReactionFactory {
	
	public static Reaction makeSaltMetathesisReaction(Compound saltA, Compound saltB) {
		Ion cationA = saltA.getFormula().getCation();
		Ion cationB = saltB.getFormula().getCation();
		Ion anionA = saltA.getFormula().getAnion();
		Ion anionB = saltB.getFormula().getAnion();
		
		Compound productA = Compounds.forFormula(new Formula(cationA, anionB).balanceSalt());
		Compound productB = Compounds.forFormula(new Formula(cationB, anionA).balanceSalt());
		
		if (productA != null && productB != null) {
			Reaction reaction = Reaction.inWaterOf(saltA).with(saltB).yields(productA).and(productB);
			if (reaction.getProducts().get(0).state() != State.AQUEOUS || reaction.getProducts().get(1).state() != State.AQUEOUS) {
				return reaction;
			}
		}
		return null;
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
	
	private static Chemical oxide(Formula formula) {
		int ratio = Math.abs(Element.O.getDefaultOxidationState()) / formula.getCation().getFormula().getCharge();
		Formula oxideFormula = new Formula(formula.getFirstPart()._(ratio), Element.O);
		return Compounds.valueOf(oxideFormula.toString());
	}
	
	public static Reaction makeStandardSaltDecompositionReaction(Compound compound) {
		Formula formula = compound.getFormula();
		boolean secondaryCarbonate = 
				formula.getParts().size() == 3 && 
				formula.getParts().get(1).hasComposition(Ions.CO3);
		if (!secondaryCarbonate && formula.getParts().size() > 2) {
			return null;
		}
		Ion anion = formula.getAnion();
		int stoich = formula.getLastPart().stoichiometry;
		Reaction reaction = null;
		if (anion == Ions.CO3) {
			reaction = Reaction.of(compound).yields(oxide(formula)).and(Compounds.CO2);
		} else if (anion == Ions.NO3 && stoich == 1) {
			if (formula.getCation() == Ions.Li) {
				reaction = Reaction.of(4, compound).yields(2, oxide(formula)).and(4, Compounds.NO2).and(Compounds.O2);
			} else {
				Formula metalNitriteFormula = formula.withAnion(Ions.NO2);
				Compound metalNitrite = Compounds.valueOf(metalNitriteFormula.toString());
				reaction = Reaction.of(2, compound).yields(2, metalNitrite).and(Compounds.O2);
			}
		} else if (anion == Ions.NO3 && stoich == 2) {
			reaction = Reaction.of(2, compound).yields(2, oxide(formula)).and(4, Compounds.NO2).and(Compounds.O2);
		} else if (anion == Ions.OH && stoich == 2) {
			int oxideStoich = formula.getFirstPart().stoichiometry;
			reaction = Reaction.of(compound).yields(oxideStoich, oxide(formula)).and(1, Compounds.H2O, State.GAS);
		} else if (anion == Ions.OH) {
			reaction = Reaction.of(2, compound).yields(oxide(formula)).and(1, Compounds.H2O, State.GAS);
		} else if (anion == Ions.HCO3) {
			Formula metalCarbonateFormula = formula.withAnion(Ions.CO3).withCation(formula.getCation());
			Compound metalCarbonate = Compounds.valueOf(metalCarbonateFormula.toString());
			reaction = Reaction.of(2, compound).yields(metalCarbonate).and(1, Compounds.H2O, State.GAS).and(Compounds.CO2);
		}
		if (reaction != null && secondaryCarbonate) {
			reaction = reaction.and(formula.getParts().get(1).stoichiometry, Compounds.CO2);
		}
		return reaction;
	}
}
