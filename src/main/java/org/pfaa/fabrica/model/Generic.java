package org.pfaa.fabrica.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.SimpleMixture;
import org.pfaa.fabrica.model.Intermediate.Intermediates;
import org.pfaa.geologica.processing.IndustrialMineral.IndustrialMinerals;
import org.pfaa.geologica.processing.OreMineral.Ores;

import com.google.common.base.CaseFormat;

public interface Generic extends IndustrialMaterial {
	public List<IndustrialMaterial> getSpecifics();
	public Generic addSpecific(IndustrialMaterial specific);
	
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

		private List<IndustrialMaterial> specifics;
		
		private Generics(IndustrialMaterial... specifics) {
			this.specifics = Arrays.asList(specifics);
		}
		
		@Override
		public String getOreDictKey() {
			return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, this.name());
		}

		@Override
		public ConditionProperties getProperties(Condition condition) {
			return this.specifics.get(0).getProperties(condition);
		}

		@Override
		public Mixture mix(IndustrialMaterial material, double weight) {
			return new SimpleMixture(this).mix(material, weight);
		}

		@Override
		public List<IndustrialMaterial> getSpecifics() {
			return Collections.unmodifiableList(this.specifics);
		}

		@Override
		public Generic addSpecific(IndustrialMaterial specific) {
			this.specifics.add(specific);
			return this;
		}
		
	}
}
