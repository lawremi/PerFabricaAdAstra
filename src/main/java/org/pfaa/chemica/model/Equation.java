package org.pfaa.chemica.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.Arrays;

public class Equation {
	private List<Term> reactants;
	private List<Term> products;
	private Formula catalyst;

	public Equation(List<Term> reactants, List<Term> products,
			Formula catalyst) {
		super();
		this.setReactants(reactants);
		this.setProducts(products);
		this.setCatalyst(catalyst);
	}

	public List<Term> getReactants() {
		return Collections.unmodifiableList(reactants);
	}

	public void setReactants(List<Term> reactants) {
		this.reactants = new ArrayList<Term>(reactants);
	}

	public List<Term> getProducts() {
		return Collections.unmodifiableList(products);
	}

	public void setProducts(List<Term> products) {
		this.products = new ArrayList<Term>(products);
	}

	public Equation yields(Term... products) {
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
		
		public Term(Formula formula) {
			this(formula, 1, State.SOLID); // FIXME: should be the standard state
		}
		
		public Term(Formula formula, int stoichiometry, State state) {
			this.formula = formula;
			this.stoichiometry = stoichiometry;
			this.state = state;
		}
	}
}
