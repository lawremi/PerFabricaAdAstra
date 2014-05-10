package org.pfaa.chemica.model;

import java.awt.Color;
import java.util.List;

/* A pure substance, including elements and compounds with fixed stoichiometry */
public interface Chemical extends IndustrialMaterial {
	public Fusion getFusion();
	public Vaporization getVaporization();
	public ChemicalPhaseProperties getProperties(Phase phase);
	public Formula getFormula();
}
