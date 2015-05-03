package org.pfaa.chemica.model;

import java.util.HashMap;
import java.util.Map;

public class CompoundDictionary {
	private static Map<Formula, Compound> compounds = new HashMap<Formula, Compound>();
	
	public static Compound lookup(Formula formula) {
		return compounds.get(formula);
	}
	
	public static Compound register(Formula formula, Compound compound) {
		compounds.put(formula, compound);
		return compound;
	}
}
