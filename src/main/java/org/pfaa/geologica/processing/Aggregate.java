package org.pfaa.geologica.processing;

import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;

public interface Aggregate extends Mixture {
	
	public Aggregate mix(IndustrialMaterial material, double weight);
	
	public enum Aggregates implements Aggregate {
		SAND, GRAVEL, STONE, CLAY, DIRT;

		@Override
		public String getOreDictKey() {
			return name();
		}

		@Override
		public ConditionProperties getProperties(Condition condition) {
			return null;
		}

		@Override
		public List<MixtureComponent> getComponents() {
			return Collections.EMPTY_LIST;
		}

		@Override
		public Aggregate mix(IndustrialMaterial material, double weight) {
			return new SimpleAggregate(new MixtureComponent(this, 1.0), 
					                   new MixtureComponent(material, weight));
		}
	}
}
