package org.pfaa.fabrica.model;

import java.awt.Color;
import java.util.List;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.Hazard;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.SimpleMixture;
import org.pfaa.chemica.model.State;
import org.pfaa.geologica.processing.OreMineral.Ores;

import com.google.common.base.CaseFormat;

public interface Intermediate extends Mixture {
	public enum Intermediates implements Intermediate {
		PORTLAND_CLINKER(Color.lightGray, 1.3),
		PORTLAND_CEMENT(PORTLAND_CLINKER.mix(Ores.GYPSUM, 0.1));

		private Mixture mixture;
		private ConditionProperties properties;
		
		private Intermediates(Mixture mixture) {
			this.mixture = mixture;
		}
		
		private Intermediates(Color color, double density) {
			this(new SimpleMixture());
			this.properties = new ConditionProperties(State.SOLID, color, density, new Hazard());
		}
		
		@Override
		public List<MixtureComponent> getComponents() {
			return this.mixture.getComponents();
		}

		@Override
		public String getOreDictKey() {
			return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
		}

		@Override
		public ConditionProperties getProperties(Condition condition) {
			return this.properties != null ? this.properties : this.mixture.getProperties(condition);
		}

		@Override
		public Mixture mix(IndustrialMaterial material, double weight) {
			return new SimpleMixture(this).mix(material, weight);
		}
	}
}
