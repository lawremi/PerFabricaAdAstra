package org.pfaa.chemica.model;

import java.awt.Color;
import java.util.List;

/* A pure substance, including elements and compounds with fixed stoichiometry */
public interface Chemical extends IndustrialMaterial, Vaporizable {
	public Fusion getFusion();
	public ChemicalConditionProperties getProperties(Condition condition);
	public Formula getFormula();
}
