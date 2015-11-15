package org.pfaa.chemica.model;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import com.google.common.base.CaseFormat;

public interface Aggregate extends Mixture {
	
	public Aggregate mix(IndustrialMaterial material, double weight);
	
	public enum Aggregates implements Aggregate {
		SAND(new Color(230, 220, 175), 1.6), 
		GRAVEL(Color.lightGray, 1.2), 
		STONE(Color.lightGray, 1.4),
		CLAY(Color.lightGray, 1.1),
		HARDENED_CLAY(new Color(220, 185, 160), 1.0),
		DIRT(new Color(150, 75, 0), 1.1),
		OBSIDIAN(new Color(16, 16, 25), 2.5);

		private ConditionProperties properties;
		
		private Aggregates(Color color, double density) {
			this.properties = new ConditionProperties(State.SOLID, color, density, new Hazard());
		}
		
		@Override
		public String getOreDictKey() {
			return name();
		}

		@Override
		public ConditionProperties getProperties(Condition condition) {
			return this.properties;
		}

		@Override
		public List<MixtureComponent> getComponents() {
			return Collections.emptyList();
		}

		@Override
		public Aggregate mix(IndustrialMaterial material, double weight) {
			return new SimpleAggregate(new MixtureComponent(this, 1.0), 
					                   new MixtureComponent(material, weight));
		}
	}
}
