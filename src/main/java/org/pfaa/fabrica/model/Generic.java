package org.pfaa.fabrica.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.fabrica.model.Intermediate.Intermediates;
import org.pfaa.geologica.processing.IndustrialMineral.IndustrialMinerals;

import com.google.common.base.CaseFormat;

public interface Generic extends IndustrialMaterial {
	public List<IndustrialMaterial> getSpecifics();
	public Generic addSpecific(IndustrialMaterial specific);
	
	public enum Generics implements Generic {
		POZZOLAN(IndustrialMinerals.VOLCANIC_ASH, IndustrialMinerals.PUMICE, IndustrialMinerals.ZEOLITE,
				 Intermediates.CALCINED_DIATOMITE),
		CEMENT(Intermediates.PORTLAND_CEMENT, Intermediates.POZZOLANIC_CEMENT);

		private List<IndustrialMaterial> specifics;
		
		private Generics(IndustrialMaterial... specifics) {
			this.specifics = Arrays.asList(specifics);
		}
		
		@Override
		public String getOreDictKey() {
			return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, this.name());
		}

		@Override
		public ConditionProperties getProperties(Condition condition) {
			return null;
		}

		@Override
		public Mixture mix(IndustrialMaterial material, double weight) {
			return null;
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
