package org.pfaa.chemica.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.Equation.Term;

public final class Formula {
	private List<Part> parts;
	private int hydration;
	private String smiles;
	
	public Formula(PartFactory... parts) {
		this(Arrays.asList(parts));
	}
	
	private Formula(List<? extends PartFactory> parts) {
		List<Part> reifiedParts = new ArrayList<Part>();
		for (PartFactory part : parts) {
			reifiedParts.add(part.getPart());
		}
		this.parts = reifiedParts;
	}
	
	private Formula(Formula other) {
		this.parts = new ArrayList<Part>(other.parts);
		this.hydration = other.hydration;
		this.smiles = other.smiles;
	}
	
	/* First/last seems a pragmatic way of interpreting simple formulae,
	 * i.e., those representing simple salts and coordination compounds.
	 */
	
	public Part getFirstPart() {
		return parts.get(0);
	}
	
	public Part getLastPart() {
		return parts.get(parts.size()-1);
	}
	
	public Formula setFirstPart(Part newPart) {
		Formula copy = new Formula(this);
		copy.parts.set(0, newPart);
		return copy;
	}
	
	public Formula substituteFirstPart(Element substitute) {
		return this.setFirstPart(substitute._(this.getFirstPart().stoichiometry));
	}
	
	public Formula setLastPart(Part newPart) {
		Formula copy = new Formula(this);
		copy.parts.set(copy.parts.size()-1, newPart);
		return copy;
	}
	
	public String toString() {
		String name = "";
		for (Part part : parts) {
			name += part.toString();
		}
		return name;
	}

	public double getMolarMass() {
		double mass = 0;
		for (Part part : parts) {
			mass += part.getMolarMass();
		}
		return mass;
	}

	public Formula hydrate(int hydration) {
		Formula copy = new Formula(this);
		copy.hydration = copy.hydration + hydration;
		return copy;
	}

	public List<Part> getParts() {
		return parts;
	}
	
	public int getHydration() {
		return hydration;
	}
	
	public Formula setSmiles(String smiles) {
		this.smiles = smiles;
		return this;
	}
	
	public Equation plus(Formula... reactants) {
		List<Term> terms = new ArrayList<Term>(reactants.length);
		for (Formula reactant : reactants) {
			terms.add(new Term(reactant));
		}
		return new Equation(terms, Collections.<Term>emptyList(), null);
	}
	
	public static final class Part implements PartFactory {
		public final Element element;
		public final int stoichiometry;
		public final List<Part> parts;
		
		private Part(Element element, int stoichiometry, List<Part> parts) {
			this.parts = Collections.unmodifiableList(parts);
			this.stoichiometry = stoichiometry;
			this.element = element;
		}
		
		public Part(Element element, int stoichiometry) {
			this.element = element;
			this.stoichiometry = stoichiometry;
			this.parts = Collections.emptyList();
		}
		
		public Part(PartFactory... parts) {
			List<Part> reifiedParts = new ArrayList<Part>();
			for (PartFactory part : parts) {
				reifiedParts.add(part.getPart());
			}
			this.parts = Collections.unmodifiableList(reifiedParts);
			this.stoichiometry = 1;
			this.element = null;
		}
		
		public Part _(int stoichiometry) {
			return new Part(element, stoichiometry, parts); 
		}
		
		public double getMolarMass() {
			double mass = 0;
			if (element != null) {
				mass = element.getAtomicWeight();
			} else {
				for (Part part : parts) {
					mass += part.getMolarMass();
				}
			}
			mass *= stoichiometry;
			return mass;
		}

		public String toString() {
			String name = "";
			if (element != null)
				name = element.name();
			else {
				for (Part part : parts) {
					name += part.toString();
				}
				if (parts.size() > 1)
					name = "(" + name + ")";
			}
			if (stoichiometry > 1)
				name += stoichiometry;
			return name;
		}
		
		public boolean equals(Object other) {
			return other instanceof Part && other.toString() == this.toString();
		}
		
		@Override
		public Part getPart() {
			return this;
		}
	}
	
	public static interface PartFactory {
		public Part getPart();
	}
}
