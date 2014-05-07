package org.pfaa.geologica.processing;

import java.util.List;

import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.PhaseProperties;
import org.pfaa.chemica.model.SimpleMixture;

public interface Fuel extends Mixture {
	public enum Fuels implements Fuel {
		;

		private Mixture delegate;
		
		private Fuels(Mixture mixture) {
			this.delegate = mixture;
		}
		
		private Fuels(Chemical chemical) {
			this(new SimpleMixture(chemical));
		}
		
		@Override
		public List<MixtureComponent> getComponents() {
			return delegate.getComponents();
		}

		@Override
		public Mixture mix(IndustrialMaterial material, double weight) {
			return delegate.mix(material, weight);
		}

		@Override
		public String getOreDictKey() {
			return delegate.getOreDictKey();
		}

		@Override
		public PhaseProperties getProperties(Phase phase) {
			return delegate.getProperties(phase);
		}
		
	}
}
