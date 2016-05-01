package org.pfaa.chemica.model;


/* A pure substance, including elements and compounds with fixed stoichiometry */
public interface Chemical extends IndustrialMaterial, Vaporizable, Fusible {
	public ChemicalConditionProperties getProperties(Condition condition);
	public ChemicalConditionProperties getProperties(Condition condition, State state);
	public Formula getFormula();
}
