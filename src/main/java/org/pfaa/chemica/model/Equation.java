package org.pfaa.chemica.model;

import java.util.List;

import org.pfaa.chemica.model.IndustrialMaterial.Phase;

public class Equation {
	private List<Formula> reactants;
	private List<Formula> products;
	private Formula catalyst;
	
	public static class Term {
		public final Formula formula;
		public final int stoichiometry;
		public final IndustrialMaterial.Phase phase;
		
		public Term(Formula formula, int stoichiometry, IndustrialMaterial.Phase phase) {
			this.formula = formula;
			this.stoichiometry = stoichiometry;
			this.phase = phase;
		}
	}
}
