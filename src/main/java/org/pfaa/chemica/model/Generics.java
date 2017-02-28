package org.pfaa.chemica.model;

import org.pfaa.chemica.model.Compound.Compounds;

import com.google.common.collect.ImmutableList;

public enum Generics implements Generic {
	FLUX_SILICA(Compounds.SiO2),
	FLUX_ACIDIC(FLUX_SILICA, Compounds.Na2B4O7),
	FLUX_BASIC(Compounds.CaCO3, Compounds.MgCO3, Compounds.CaO, Compounds.MgO, Compounds.CaF2),
	;
	
	private Generic delegate;
	
	private Generics(IndustrialMaterial... specifics) {
		this.delegate = new SimpleGeneric(name(), specifics);
	}

	@Override
	public ImmutableList<IndustrialMaterial> getSpecifics() {
		return this.delegate.getSpecifics();
	}
}
