package org.pfaa.chemica.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.pfaa.chemica.processing.MaterialStoich;

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

	public List<Term> getReactants(State state) {
		return Collections.unmodifiableList(this.getReactants().stream().
					filter((p) -> p.state() == state).
					collect(Collectors.toList()));
	}

	public void setReactants(List<Term> reactants) {
		this.reactants = new ArrayList<Term>(reactants);
	}

	public List<Term> getProducts() {
		return Collections.unmodifiableList(this.products);
	}
	
	public List<Term> getProducts(State state) {
		return Collections.unmodifiableList(this.getProducts().stream().
					filter((p) -> p.state() == state).
					collect(Collectors.toList()));
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
	
	public Equation scale(float scale) {
		Function<Term, Term> scaleTerm = (t) -> t.scale(scale);
		return new Equation(
				this.reactants.stream().map(scaleTerm).collect(Collectors.toList()),
				this.products.stream().map(scaleTerm).collect(Collectors.toList()),
				this.catalysts);
	}

	public static class Term extends MaterialStoich<Chemical> {
		public static final Collector<Term, ?, Mixture> TO_MIXTURE = 
				Collector.of(SimpleMixture::new, (m, t) -> m.mix(t.material(), t.stoich), Mixture::mixAll);
		
		public final float concentration;
		
		public Term(Chemical chemical) {
			this(1, chemical);
		}
		
		public Term(float stoich, Chemical chemical) {
			this(stoich, chemical, chemical.getProperties(Condition.STP).state);
		}
		
		public Term(float stoich, Chemical chemical, State state) {
			this(stoich, chemical, state, 1.0F);
		}
		
		public Term(float stoich, Chemical chemical, State state, float concentration) {
			super(state.of(chemical), stoich);
			this.concentration = concentration;
		}
		
		public Term(MaterialStoich<Chemical> stoich) {
			this(stoich.stoich, stoich.material(), stoich.state());
		}
		
		public Term scale(float scale) {
			return new Term(this.stoich * scale, this.material(), this.state(), this.concentration);
		}

		public String toString() {
			String str = this.material().toString() + "(" + this.state().name().substring(0, 1).toLowerCase() + ")";
			if (this.stoich > 1) {
				str = (int)(this.stoich) + str;
			}
			return str;
		}
	}
}
