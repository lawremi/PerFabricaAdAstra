package org.pfaa.geologica.processing;

import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.PhaseProperties;
import org.pfaa.chemica.model.SimpleMixture;
import org.pfaa.chemica.model.IndustrialMaterial.Phase;

public interface Aggregate extends Mixture {
	
	public Aggregate add(IndustrialMaterial material, double weight);
	
	public enum Aggregates implements Aggregate {
		SAND, GRAVEL, STONE, CLAY, DIRT;

		@Override
		public String getOreDictKey() {
			return name();
		}

		@Override
		public PhaseProperties getProperties(Phase phase) {
			return null;
		}

		@Override
		public List<MixtureComponent> getComponents() {
			return Collections.EMPTY_LIST;
		}

		@Override
		public Aggregate add(IndustrialMaterial material, double weight) {
			return new SimpleAggregate(new MixtureComponent(this, 1.0), 
					                   new MixtureComponent(material, weight));
		}

	}
}
