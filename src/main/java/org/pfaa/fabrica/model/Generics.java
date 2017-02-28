package org.pfaa.fabrica.model;

import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Generic;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.SimpleGeneric;
import org.pfaa.fabrica.model.Intermediate.Intermediates;
import org.pfaa.geologica.processing.IndustrialMineral.IndustrialMinerals;
import org.pfaa.geologica.processing.OreMineral.Ores;

import com.google.common.collect.ImmutableList;

public enum Generics implements Generic {
	POZZOLAN(IndustrialMinerals.VOLCANIC_ASH, IndustrialMinerals.PUMICE, IndustrialMinerals.ZEOLITE,
			 Intermediates.CALCINED_DIATOMITE, Intermediates.METAKAOLIN, Intermediates.FIRED_CLAY,
			 Intermediates.ASH, Intermediates.SLAG),
	CEMENT(Intermediates.PORTLAND_CEMENT, Intermediates.POZZOLANIC_CEMENT),
	BINDER(Compounds.CaOH2, Intermediates.GYPSUM_PLASTER),
	FILLER(IndustrialMinerals.MICA, IndustrialMinerals.DIATOMITE, IndustrialMinerals.PERLITE,
		   IndustrialMinerals.VERMICULITE, IndustrialMinerals.TALC, Ores.CALCITE, IndustrialMinerals.KAOLINITE,
		   Ores.BARITE),
	JOINT_COMPOUND_FILLER(Ores.GYPSUM, Ores.CALCITE),
	FULLERS_EARTH(IndustrialMinerals.CALCIUM_MONTMORILLONITE, IndustrialMinerals.PALYGORSKITE);

	private Generic delegate;
	
	private Generics(IndustrialMaterial... specifics) {
		this.delegate = new SimpleGeneric(name(), specifics);
	}

	@Override
	public ImmutableList<IndustrialMaterial> getSpecifics() {
		return this.delegate.getSpecifics();
	}
}