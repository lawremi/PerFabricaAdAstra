package org.pfaa.chemica.model;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.pfaa.chemica.model.Equation.Term;
import org.pfaa.chemica.processing.Combination;
import org.pfaa.chemica.processing.ConditionedConversion;
import org.pfaa.chemica.processing.Conversion;
import org.pfaa.chemica.processing.MaterialStoich;

import com.google.common.collect.Lists;

public class Reaction extends ConditionedConversion {

	private Equation equation;
	private Condition baseCondition = new Condition();
	
	protected Reaction(Equation equation) {
		this.equation = equation;
	}
	
	public double getEnthalpyChange(Condition condition) {
		double enthalpy = 0;
		for (Term product : this.getProducts()) {
			enthalpy += product.stoich * product.material().getProperties(condition, product.state()).enthalpy;
		}
		for (Term reactant : this.getReactants()) {
			enthalpy -= reactant.stoich * reactant.material().getProperties(condition, reactant.state()).enthalpy;
		}
		return enthalpy;
	}
	
	public double getEntropyChange(Condition condition) {
		double entropy = 0;
		for (Term product : this.getProducts()) {
			entropy += product.stoich * product.material().getProperties(condition, product.state()).entropy;
		}
		for (Term reactant : this.getReactants()) {
			entropy -= reactant.stoich * reactant.material().getProperties(condition, reactant.state()).entropy;
		}
		return entropy;
	}
	
	public double getFreeEnergyChange(Condition condition) {
		return this.getEnthalpyChange(condition) * 1000 - condition.temperature * this.getEntropyChange(condition);
	}
	
	public boolean isSpontaneous(Condition condition) {
		return this.getFreeEnergyChange(condition) < 0;
	}
	
	public int getEquilibriumTemperature() {
		int tHat = (int)(this.getEnthalpyChange(Condition.STP)*1000 / this.getEntropyChange(Condition.STP));
		Condition cHat = new Condition(tHat);
		double t = this.getEnthalpyChange(cHat)*1000 / this.getEntropyChange(cHat);
		if (Double.isNaN(t))
			t = tHat;
		return (int)(t);
	}
	
	public double getEquilibriumConstant(Condition condition) {
		return Math.exp(-this.getFreeEnergyChange(condition) / (Constants.R * condition.temperature));
	}
	
	public int getSpontaneousTemperature() {
		if (this.getFreeEnergyChange(Condition.STP) < 0) {
			return Condition.STP.temperature;
		}
		return this.getEquilibriumTemperature();
	}
	
	public List<Term> getProducts() {
		return this.equation.getProducts();
	}
	
	public List<Term> getProducts(State state) {
		return this.equation.getProducts(state);
	}
	
	private Mixture mixTerms(List<Term> terms) {
		Mixture mixture = new SimpleMixture();
		for (Term term : terms) {
			mixture.mix(term.material(), term.stoich);
		}
		return mixture;
	}
	
	public Mixture getProduct(State state) {
		return mixTerms(this.getProducts(state));
	}
	
	public Mixture getProduct() {
		return mixTerms(this.getProducts());
	}
	
	public List<IndustrialMaterial> getCatalysts() {
		return this.equation.getCatalysts();
	}
	
	public List<Term> getReactants() {
		return this.equation.getReactants();
	}
	
	public List<Term> getReactants(State state) {
		return this.equation.getReactants(state);
	}
	
	public Reaction and(int stoichiometry, Chemical product, State state) {
		return this.yields(stoichiometry, product, state);
	}
	
	public Reaction and(int stoichiometry, Chemical product) {
		return this.yields(stoichiometry, product);
	}
	
	public Reaction and(Chemical product) {
		return this.yields(product);
	}
	
	public Reaction yields(int stoichiometry, Chemical product, State state) {
		this.equation.addProduct(new Term(stoichiometry, product, state));
		return this;
	}
	
	public Reaction yields(int stoichiometry, Chemical product) {
		return this.yields(stoichiometry, product, product.getProperties(this.baseCondition).state);
	}
	
	public Reaction yields(Chemical product) {
		return this.yields(1, product);
	}
	
	public Reaction with(int stoichiometry, Chemical reactant, State state) {
		this.equation.addReactant(new Term(stoichiometry, reactant, state));
		return this;
	}
	
	public Reaction with(int stoichiometry, Chemical reactant) {
		return this.with(stoichiometry, reactant, reactant.getProperties(this.baseCondition).state);
	}
	
	public Reaction with(Chemical reactant, State state) {
		return this.with(1, reactant, state);
	}
	
	public Reaction with(Chemical reactant) {
		return this.with(1, reactant);
	}
	
	public Reaction via(IndustrialMaterial... catalysts) {
		this.equation.addCatalysts(catalysts);
		return this;
	}

	@Override
	public Reaction at(Condition condition) {
		return (Reaction)super.at(condition);
	}

	@Override
	public Reaction at(int temp) {
		return (Reaction)super.at(temp);
	}

	public String toString() {
		return this.equation.toString();
	}

	public Reaction scale(float scale) {
		return new Reaction(this.equation.scale(scale));
	}

	public boolean isAqueous() {
		return this.baseCondition.aqueous;
	}
	
	public boolean isAtmospheric() {
		return this.baseCondition.atmospheric;
	}
	
	@Override
	protected Condition deriveCondition() {
		return new Condition(this.getSpontaneousTemperature());
	}

	public boolean hasSolidReactants() {
		for (Term reactant : this.getReactants()) {
			if (reactant.state() == State.SOLID)
				return true;
		}
		return false;
	}
	
	public boolean hasOnlySolidReactants() {
		for (Term reactant : this.getReactants()) {
			if (reactant.state() != State.SOLID)
				return false;
		}
		return true;
	}
	
	public Combination asCombinationForSolution(Mixture solution) {
		Mixture solutes = solution.getFraction(Condition.STP, State.SOLID);
		if (solutes.getComponents().size() != 1)
			return null;
		MixtureComponent solute = solutes.getComponents().get(0);
		List<MaterialStoich<?>> inputs = this.getReactants().stream().map((t) -> {
			MaterialStoich<?> stoich = t;
			if (t.material() == solute.material) {
				float weight = (float)(t.stoich * (Constants.STANDARD_SOLUTE_WEIGHT / solute.weight));
				stoich = MaterialStoich.of(weight, t.materialState);
			}
			return stoich;
		}).collect(Collectors.toList());
		return Combination.of(inputs).yields(Lists.newArrayList(this.getProducts())).at(this.getCondition());
	}
	
	public static Reaction of(Chemical reactant) {
		return of(1, reactant);
	}
	
	public static Reaction of(Chemical reactant, State state) {
		return of(1, reactant, state);
	}
	
	public static Reaction of(int stoichiometry, Chemical reactant) {
		return of(stoichiometry, reactant, reactant.getProperties(Condition.STP).state);
	}
	
	public static Reaction of(int stoichiometry, Chemical reactant, State state) {
		return of(stoichiometry, reactant, state, 1.0F);
	}
	
	private static Reaction of(int stoichiometry, Chemical reactant, State state, float concentration) {
		return of(null, stoichiometry, reactant, state, concentration);
	}

	private static Reaction of(Type type, int stoichiometry, Chemical reactant, State state, float concentration) {
		Term term = new Term(stoichiometry, reactant, state, concentration);
		Equation equation = new Equation(Lists.newArrayList(term), Collections.<Term>emptyList(), null);
		return new Reaction(equation);
	}

	public static Reaction inWaterOf(Chemical reactant) {
		return inWaterOf(1, reactant);
	}
	
	public static Reaction inWaterOf(Chemical reactant, float concentration) {
		return inWaterOf(1, reactant, State.AQUEOUS, concentration);
	}
	
	public static Reaction inWaterOf(int stoichiometry, Chemical reactant) {
		return inWaterOf(stoichiometry, reactant, reactant.getProperties(Condition.AQUEOUS_STP).state);
	}
	
	public static Reaction inWaterOf(int stoichiometry, Chemical reactant, State state) {
		return inWaterOf(stoichiometry, reactant, state, Constants.STANDARD_SOLUTE_WEIGHT);
	}
	
	private static Reaction inWaterOf(int stoichiometry, Chemical reactant, State state, float concentration) {
		Reaction reaction = of(stoichiometry, reactant, state, concentration);
		reaction.baseCondition.aqueous = true;
		return reaction;
	}
	
	public static Reaction inAirOf(Chemical reactant) {
		return inAirOf(1, reactant);
	}
	
	public static Reaction inAirOf(int stoichiometry, Chemical reactant) {
		return inAirOf(1, reactant, reactant.getProperties(Condition.STP).state);
	}
	
	public static Reaction inAirOf(int stoichiometry, Chemical reactant, State state) {
		Reaction reaction = of(stoichiometry, reactant, state);
		reaction.baseCondition.atmospheric = true;
 		return reaction;
	}

	@Override
	public double getEnergy() {
		return this.getEnthalpyChange(this.getCondition());
	}

	@Override
	public List<MaterialStoich<?>> getInputs() {
		return Collections.unmodifiableList(this.getReactants());
	}

	@Override
	public List<MaterialStoich<?>> getOutputs() {
		return Collections.unmodifiableList(this.getProducts());
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static interface Type extends Conversion.Type {

		public enum ReactionTypes implements Type {
			SYNTHESIS,
			DECOMPOSITION,
			SINGLE_DISPLACEMENT,
			DOUBLE_DISPLACEMENT,
			NEUTRALIZATION,
			COMBUSTION,
			ORGANIC,
			REDOX;
		}

	}

}
