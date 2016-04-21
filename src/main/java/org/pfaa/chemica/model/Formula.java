package org.pfaa.chemica.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
	
	public Formula withFirstPart(Part newPart) {
		Formula copy = new Formula(this);
		copy.parts.set(0, newPart);
		return copy;
	}
	
	public Formula substituteFirstPart(Element substitute) {
		return this.withFirstPart(substitute._(this.getFirstPart().stoichiometry));
	}
	
	public Formula withLastPart(Part newPart) {
		Formula copy = new Formula(this);
		copy.parts.set(copy.parts.size()-1, newPart);
		return copy;
	}

	public Formula withoutLastPart() {
		Formula copy = new Formula(this);
		copy.parts.remove(copy.parts.size()-1);
		return copy;
	}
	
	public String toString() {
		String name = "";
		for (Part part : this.parts) {
			name += part.toString();
		}
		return name;
	}

	public double getMolarMass() {
		double mass = 0;
		for (Part part : this.parts) {
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
		return Collections.unmodifiableList(this.parts);
	}
	
	public Formula appendPart(Part part) {
		Formula copy = new Formula(this);
		copy.parts.add(part);
		return copy;
	}
	
	public int getHydration() {
		return this.hydration;
	}
	
	public Formula setSmiles(String smiles) {
		this.smiles = smiles;
		return this;
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
			}
			if (stoichiometry > 1) {
				if (parts.size() > 1)
					name = "(" + name + ")";
				name += stoichiometry;
			}
			return name;
		}
		
		public boolean equals(Object other) {
			return other instanceof Part && other.toString().equals(this.toString());
		}
		
		@Override
		public Part getPart() {
			return this;
		}
		
		public boolean hasComposition(Part part) {
			return this.equals(part._(this.stoichiometry));
		}
	}
	
	public static interface PartFactory {
		public Part getPart();
	}
}
