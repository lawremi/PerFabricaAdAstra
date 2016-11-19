package org.pfaa.chemica.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class Equation {
	private List<Term> reactants;
	private List<Term> products;
	private List<IndustrialMaterial> catalysts = new ArrayList<IndustrialMaterial>();

	public Equation(List<Term> reactants, List<Term> products,
			List<IndustrialMaterial> catalysts) {
		super();
		this.setReactants(reactants);
		this.setProducts(products);
		this.setCatalysts(catalysts);
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
	
	public List<IndustrialMaterial> getCatalysts() {
		return Collections.unmodifiableList(this.catalysts);
	}

	public void addCatalysts(IndustrialMaterial... catalysts) {
		this.catalysts.addAll(Arrays.asList(catalysts));
	}
	
	public void setCatalysts(List<IndustrialMaterial> catalysts) {
		this.catalysts = new ArrayList<IndustrialMaterial>(catalysts);
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

	private static class ScaleTerm implements Function<Term,Term> {
		private float scale;
		
		public ScaleTerm(float scale) {
			this.scale = scale;
		}
		
		@Override
		public Term apply(Term input) {
			return input.scale(this.scale);
		}
	}
	
	public Equation scale(float scale) {
		Function<Term,Term> scaleTerm = new ScaleTerm(scale);
		return new Equation(
				Lists.transform(this.reactants, scaleTerm),
				Lists.transform(this.products, scaleTerm),
				this.catalysts);
	}

	public static class Term {
		public final Chemical chemical;
		public final float stoichiometry;
		public final State state;
		public final float concentration;
		
		public Term(Chemical chemical) {
			this(1, chemical);
		}
		
		public Term(float stoichiometry, Chemical chemical) {
			this(stoichiometry, chemical, chemical.getProperties(Condition.STP).state);
		}
		
		public Term(float stoichiometry, Chemical chemical, State state) {
			this(stoichiometry, chemical, state, 1.0F);
		}
		
		public Term(float stoichiometry, Chemical chemical, State state, float concentration) {
			this.stoichiometry = stoichiometry;
			this.chemical = chemical;
			this.state = state;
			this.concentration = concentration;
		}
		
		public Term scale(float scale) {
			return new Term(this.stoichiometry * scale, this.chemical, this.state, this.concentration);
		}

		public String toString() {
			String str = this.chemical.toString() + "(" + this.state.name().substring(0, 1).toLowerCase() + ")";
			if (this.stoichiometry > 1) {
				str = (int)(this.stoichiometry) + str;
			}
			return str;
		}
	}
}
