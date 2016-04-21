package org.pfaa.chemica.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Equation {
	private List<Term> reactants;
	private List<Term> products;
	private Chemical catalyst;

	public Equation(List<Term> reactants, List<Term> products,
			Chemical catalyst) {
		super();
		this.setReactants(reactants);
		this.setProducts(products);
		this.setCatalyst(catalyst);
	}

	public List<Term> getReactants() {
		return Collections.unmodifiableList(this.reactants);
	}

	public void setReactants(List<Term> reactants) {
		this.reactants = new ArrayList<Term>(reactants);
	}

	public List<Term> getProducts() {
		return Collections.unmodifiableList(this.products);
	}

	public void setProducts(List<Term> products) {
		this.products = new ArrayList<Term>(products);
	}

	public void addReactant(Term reactant) {
		this.reactants.add(reactant);
	}
	
	public void addProduct(Term product) {
		this.products.add(product);
	}
	
	public Chemical getCatalyst() {
		return catalyst;
	}

	public void setCatalyst(Chemical catalyst) {
		this.catalyst = catalyst;
	}
	
	public String toString() {
		String str = this.reactants.get(0).toString();
		for (Term reactant : this.reactants.subList(1, this.reactants.size())) {
			str += "+" + reactant;
		}
		str += " => " + this.products.get(0).toString();
		for (Term product : this.products.subList(1, this.products.size())) {
			str += "+" + product;
		}
		return str;
	}
	
	public static class Term {
		public final Chemical chemical;
		public final int stoichiometry;
		public final State state;
		
		public Term(Chemical chemical) {
			this(1, chemical);
		}
		
		public Term(int stoichiometry, Chemical chemical) {
			this(stoichiometry, chemical, chemical.getProperties(Condition.STP).state);
		}
		
		public Term(int stoichiometry, Chemical chemical, State state) {
			this.stoichiometry = stoichiometry;
			this.chemical = chemical;
			this.state = state;
		}
		
		public String toString() {
			String str = this.chemical.toString() + "(" + this.state.name().substring(0, 1).toLowerCase() + ")";
			if (this.stoichiometry > 1) {
				str = this.stoichiometry + str;
			}
			return str;
		}
	}
}
