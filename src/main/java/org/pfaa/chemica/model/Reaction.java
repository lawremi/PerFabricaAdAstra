package org.pfaa.chemica.model;

import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.Equation.Term;

import com.google.common.collect.Lists;

public class Reaction {

	private Equation equation;
	private Condition baseCondition = new Condition();
	private Condition canonicalCondition;
	
	public Reaction(Equation equation) {
		this.equation = equation;
	}
	
	public double getEnthalpyChange(Condition condition) {
		double enthalpy = 0;
		for (Term product : this.getProducts()) {
			enthalpy += product.stoichiometry * product.chemical.getProperties(condition, product.state).enthalpy;
		}
		for (Term reactant : this.getReactants()) {
			enthalpy -= reactant.stoichiometry * reactant.chemical.getProperties(condition, reactant.state).enthalpy;
		}
		return enthalpy;
	}
	
	public double getEntropyChange(Condition condition) {
		double entropy = 0;
		for (Term product : this.getProducts()) {
			entropy += product.stoichiometry * product.chemical.getProperties(condition, product.state).entropy;
		}
		for (Term reactant : this.getReactants()) {
			entropy -= reactant.stoichiometry * reactant.chemical.getProperties(condition, reactant.state).entropy;
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
			mixture.mix(term.chemical, term.stoichiometry);
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
	
	public String toString() {
		return this.equation.toString();
	}

	public Reaction scale(float scale) {
		return new Reaction(this.equation.scale(scale));
	}

	public boolean isAqueous() {
		return this.baseCondition.aqueous;
	}
	
	public Condition getCanonicalCondition() {
		if (this.canonicalCondition == null) {
			this.at(this.getSpontaneousTemperature());
		}
		return this.canonicalCondition;
	}
	
	public boolean hasSolidReactants() {
		for (Term reactant : this.getReactants()) {
			if (reactant.state == State.SOLID)
				return true;
		}
		return false;
	}
	
	public Reaction at(int temperature, double pressure) {
		this.canonicalCondition = new Condition(temperature, pressure, this.isAqueous());
		return this;
	}
	
	public Reaction at(int temperature) {
		return this.at(temperature, Constants.STANDARD_PRESSURE);
	}
	
	public static Reaction of(Chemical reactant) {
		return of(1, reactant);
	}
	
	public static Reaction of(int stoichiometry, Chemical reactant) {
		return of(stoichiometry, reactant, reactant.getProperties(Condition.STP).state);
	}
	
	public static Reaction of(int stoichiometry, Chemical reactant, State state) {
		return of(stoichiometry, reactant, state, 1.0F);
	}
	
	private static Reaction of(int stoichiometry, Chemical reactant, State state, float concentration) {
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
	
	public static Reaction inSolutionOf(Chemical reactant, float concentration) {
		return inSolutionOf(1, reactant, concentration);
	}
	
	public static Reaction inSolutionOf(int stoichiometry, Chemical reactant, float concentration) {
		return inWaterOf(stoichiometry, reactant, reactant.getProperties(Condition.AQUEOUS_STP).state, concentration);
	}
}
