package org.pfaa.chemica.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.Arrays;

public class Equation {
	private List<Formula> reactants;
	private List<Formula> products;
	private Formula catalyst;

	public Equation(List<Formula> reactants, List<Formula> products,
			Formula catalyst) {
		super();
		this.setReactants(reactants);
		this.setProducts(products);
		this.setCatalyst(catalyst);
	}

	public List<Formula> getReactants() {
		return Collections.unmodifiableList(reactants);
	}

	public void setReactants(List<Formula> reactants) {
		this.reactants = new ArrayList<Formula>(reactants);
	}

	public List<Formula> getProducts() {
		return Collections.unmodifiableList(products);
	}

	public void setProducts(List<Formula> products) {
		this.products = new ArrayList<Formula>(products);
	}

	public Equation yields(Formula... products) {
		this.setProducts(Arrays.asList(products));
		return this;
	}
	
	public Formula getCatalyst() {
		return catalyst;
	}

	public void setCatalyst(Formula catalyst) {
		this.catalyst = catalyst;
	}
	
	public Equation via(Formula catalyst) {
		this.setCatalyst(catalyst);
		return this;
	}

	public static class Term {
		public final Formula formula;
		public final int stoichiometry;
		public final State state;
		
		public Term(Formula formula, int stoichiometry, State state) {
			this.formula = formula;
			this.stoichiometry = stoichiometry;
			this.state = state;
		}
	}
}
