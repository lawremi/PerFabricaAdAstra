package org.pfaa.chemica.model;

import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.Equation.Term;

import com.google.common.collect.Lists;

public class Reaction {

	// Currently completely described by equation, but could include e.g. kinetics...
	private Equation equation;
	
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
	
	public List<Term> getReactants() {
		return this.equation.getReactants();
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
		return this.yields(stoichiometry, product, product.getProperties(Condition.STP).state);
	}
	
	public Reaction yields(Chemical product) {
		return this.yields(1, product);
	}
	
	public Reaction with(int stoichiometry, Chemical reactant, State state) {
		this.equation.addReactant(new Term(stoichiometry, reactant, state));
		return this;
	}
	
	public Reaction with(int stoichiometry, Chemical reactant) {
		return this.with(stoichiometry, reactant, reactant.getProperties(Condition.STP).state);
	}
	
	public Reaction with(Chemical reactant) {
		return this.with(1, reactant);
	}
	
	public Reaction via(Chemical catalyst) {
		this.equation.setCatalyst(catalyst);
		return this;
	}
	
	public String toString() {
		return this.equation.toString();
	}

	public Reaction scale(float scale) {
		return new Reaction(this.equation.scale(scale));
	}

	public static Reaction of(Chemical reactant) {
		return of(1, reactant);
	}
	
	public static Reaction of(int stoichiometry, Chemical reactant) {
		return of(stoichiometry, reactant, reactant.getProperties(Condition.STP).state);
	}
	
	public static Reaction of(int stoichiometry, Chemical reactant, State state) {
		Term term = new Term(stoichiometry, reactant, state);
		Equation equation = new Equation(Lists.newArrayList(term), Collections.<Term>emptyList(), null);
		return new Reaction(equation);
	}
}
