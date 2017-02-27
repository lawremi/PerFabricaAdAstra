package org.pfaa.fabrica.model;

import java.awt.Color;
import java.util.List;

import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.Hazard;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.SimpleMixture;
import org.pfaa.chemica.model.State;
import org.pfaa.geologica.processing.OreMineral.Ores;

public interface Intermediate extends Mixture {
	public enum Intermediates implements Intermediate {
		ASH(Color.darkGray, 0.7),
		CALCINED_DIATOMITE(new Color(250, 185, 140), 2.3),
		FIRED_CLAY(new Color(190, 110, 80), 2.0),
		METAKAOLIN(new Color(255, 245, 245), 0.9),
		SPINEL(new Color(255, 250, 250), 3.6),
		MULLITE(new Color(255, 255, 255), 3.1),
		PORTLAND_CLINKER(Color.lightGray, 1.3),
		PORTLAND_CEMENT(PORTLAND_CLINKER.mix(Ores.GYPSUM, 0.1)),
		POZZOLANIC_CEMENT,
		SLAG(new Color(200, 180, 140), 0.9),
		GYPSUM_PLASTER(Color.white, 0.881);

		static { // Cannot refer to Generics during construction (circular dependency)
			POZZOLANIC_CEMENT.setComposition(Generics.POZZOLAN.mix(Compounds.CaOH2, 0.1));
		}
		
		private Mixture mixture;
		private ConditionProperties properties;
		
		private Intermediates() {
		}
		
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
		public ConditionProperties getProperties(Condition condition) {
			return this.properties != null ? this.properties : this.mixture.getProperties(condition);
		}

		@Override
		public Mixture mix(IndustrialMaterial material, double weight) {
			return new SimpleMixture(this).mix(material, weight);
		}
		
		private void setComposition(Mixture mixture) {
			this.mixture = mixture;
		}

		@Override
		public Mixture removeAll() {
			return this.mixture.removeAll();
		}
	}
}
