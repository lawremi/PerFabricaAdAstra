package org.pfaa.chemica.model;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

public interface Aggregate extends Mixture {
	public Aggregate mix(IndustrialMaterial material, double weight);
	public Aggregate removeAll();

	enum Category {
		LOOSE,
		HARD,
		SOFT
	}
	Category getCategory();
	default boolean isLoose() {
		return this.getCategory() == Category.LOOSE;
	}
	default boolean isHard() {
		return this.getCategory() == Category.HARD;
	}
	default boolean isSoft() {
		return this.getCategory() == Category.SOFT;
	}
	
	@Override
	default boolean isGranular() {
		return this.isLoose();
	}
	
	public enum Aggregates implements Aggregate {
		SAND(new Color(230, 220, 175), 1.6), 
		GRAVEL(Color.lightGray, 1.2), 
		STONE(Color.lightGray, 1.4),
		CLAY(Color.lightGray, 1.1),
		HARDENED_CLAY(new Color(220, 185, 160), 1.0),
		DIRT(new Color(150, 75, 0), 1.1),
		COARSE_DIRT(new Color(150, 75, 0), 1.0),
		OBSIDIAN(new Color(16, 16, 25), 2.5);

		private ConditionProperties properties;
		
		private Aggregates(Color color, double density) {
			this.properties = new ConditionProperties(State.SOLID, color, density, new Hazard());
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
			return new SimpleAggregate(this.getCategory(), 
					                   new MixtureComponent(this, 1.0), 
					                   new MixtureComponent(material, weight));
		}

		@Override
		public Aggregate removeAll() {
			return this;
		}

		@Override
		public Category getCategory() {
			if (this == Aggregates.SAND || this == Aggregates.GRAVEL || this == Aggregates.COARSE_DIRT)
				return Category.LOOSE;
			if (this == Aggregates.STONE || this == Aggregates.HARDENED_CLAY || this == Aggregates.OBSIDIAN)
				return Category.HARD;
			return Category.SOFT;
		}
	}
}
