package org.pfaa.geologica.processing;

import org.pfaa.chemica.model.Generic;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.SimpleGeneric;

import com.google.common.collect.ImmutableList;

public enum Generics implements Generic {
	FLUX_SILICA();
	
	private Generic delegate;
	
	private Generics(IndustrialMaterial... specifics) {
		this.delegate = new SimpleGeneric(name(), specifics);
	}

	@Override
	public ImmutableList<IndustrialMaterial> getSpecifics() {
		return this.delegate.getSpecifics();
	}
}
