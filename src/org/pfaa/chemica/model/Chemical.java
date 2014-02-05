package org.pfaa.chemica.model;

import java.awt.Color;
import java.util.List;

public interface Chemical extends IndustrialMaterial {
	public Fusion getFusion();
	public Vaporization getVaporization();
	public ChemicalPhaseProperties getProperties(Phase phase);
	public Formula getFormula();
}
