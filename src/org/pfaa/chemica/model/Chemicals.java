package org.pfaa.chemica.model;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.Hazard.SpecialCode;

public enum Chemicals implements Chemical {
	AlOH3("bauxite", new Solid(Color.WHITE, 2.42, -1277, 71)),
	BaSO4("barium", new Fusion(1345, 40.6), new Solid(Color.WHITE, 4.5, -1465, 132, new Hazard(0, 0, 0))),
	CaCO3("calcite", new Fusion(1339, 53.1), new Solid(Color.WHITE, 2.7, -1207, 93)),
	//CaWO4("tungstate", new Solid(Color.ORANGE, 6.1, -1645, 126)),
	//CeCO3F("bastnasite", new Solid(new Color(177, 81, 39), 4.4, )), 
	//CuFeS2("copper", new Fusion(1223, Double.NaN), new Solid(new Color(195, 163, 104), 4.2, -194, 125)),
	Fe2O3("iron", new Solid(new Color(200, 42, 42), 5.2, -826, 90)),
	Fe3O4("iron", new Fusion(1870, Double.NaN), new Solid(Color.BLACK, 5.17, -1118, 145)),
	FeOOH("goethite", new Solid(new Color(229, 60, 0), 4.25, -559, 60.4)),
	//FeCr2O4("chromium", new Solid(new Color(25, 10, 10), 4.7, -1438, 152)),
	//FeTiO3("titanium", new Solid(new Color(25, 10, 10), 4.6)),
	//FeWO4("tungstate", new Solid(new Color(15, 10, 10), 7.5, -1154, 132)),
	//NiS("nickel", new Fusion(1070, 30.1), new Solid(Color.BLACK, 5.8, -82, 53)),
	//HgS("cinnabar", new Solid(new Color(139, 0, 0), 8.1, -58, 78, new Hazard(2, 1, 0))),
	MgCO3("magnesite", new Solid(Color.WHITE, 3.0, -1113, 66)),
	//Mg2SiO4("olivine", new Fusion(2170, 71), new Solid(new Color(154, 205, 50), 3.2, -2175, 95)),
	MnO2("manganese", new Solid(new Color(15, 10, 10), 5.0, -520, 53, new Hazard(1, 1, 2, SpecialCode.OXIDIZER))),
	MoS2("molybdenum", new Solid(new Color(26, 26, 26), 5.1, -234, 62, new Hazard(1, 1, 0))),
	Na2B4O7("borax", new Fusion(1016, 81), new Solid(Color.WHITE, 1.7, -3276, 189)),
	NaCl("salt", new Fusion(1074, 30), new Solid(Color.WHITE, 2.2, -411, 72)),
	//NiO("nickel", new Fusion(2228, Double.NaN), new Solid(Color.GREEN, 2.7, -240, 38, new Hazard(2, 0, 0))),
	PbS("lead", new Fusion(1391, 17), new Solid(new Color(26, 26, 26), 7.6, -100, 91, new Hazard(2, 0, 0))),
	Sb2S3("antimony", new Fusion(823, 40), new Solid(Color.GRAY, 4.64, -140, 182, new Hazard(2, 1, 0))),
	SiO2("quartz", new Fusion(1900, 14), new Solid(Color.WHITE, 2.6, -911, 42)),
	SnO2("tin", new Fusion(1903, Double.NaN), new Solid(Color.WHITE, 7.0, -581, 52, new Hazard(2, 0, 0))),
	ZnS("zinc", new Solid(Color.WHITE, 4.1, -206, 58));
	
	private Chemical delegate;
	
	private Chemicals(String oreDictKey, Fusion fusion, Vaporization vaporization, AbstractPhaseProperties... states) {
		this.delegate = new SimpleSubstance(name(), oreDictKey, fusion, vaporization, states);
	}
	
	private Chemicals(String oreDictKey, Fusion fusion, AbstractPhaseProperties... states) {
		this(oreDictKey, fusion, null, states);
	}
	
	private Chemicals(String oreDictKey, AbstractPhaseProperties... states) {
		this(oreDictKey, null, states);
	}
	
	private Chemicals(AbstractPhaseProperties... states) {
		this(null, states);
	}

	public Color getColor() {
		return delegate.getColor();
	}

	public double getDensity() {
		return delegate.getDensity();
	}

	public double getEnthalpy() {
		return delegate.getEnthalpy();
	}

	public double getEntropy() {
		return delegate.getEntropy();
	}

	public Hazard getHazard() {
		return delegate.getHazard();
	}

	public String getOreDictKey() {
		return delegate.getOreDictKey();
	}

	public Chemical solid() {
		return delegate.solid();
	}

	public Chemical liquid() {
		return delegate.liquid();
	}

	public Chemical gas() {
		return delegate.gas();
	}

	@Override
	public Fusion getFusion() {
		return delegate.getFusion();
	}

	@Override
	public Vaporization getVaporization() {
		// TODO Auto-generated method stub
		return delegate.getVaporization();
	}

	@Override
	public Phase getPhase() {
		return delegate.getPhase();
	}

}
