package org.pfaa.chemica.model;

import java.util.List;

public class Equation {
	private List<Formula> reactants;
	private List<Formula> products;
	private Formula catalyst;
	
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
