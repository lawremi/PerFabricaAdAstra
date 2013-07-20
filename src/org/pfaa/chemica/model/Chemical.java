package org.pfaa.chemica.model;

import java.awt.Color;
import java.util.List;

public interface Chemical extends IndustrialMaterial, ChemicalPhaseProperties {
	public Fusion getFusion();
	public Vaporization getVaporization();
	
	public Chemical solid();
	public Chemical liquid();
	public Chemical gas();
}
