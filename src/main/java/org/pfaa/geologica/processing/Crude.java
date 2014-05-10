package org.pfaa.geologica.processing;

import java.util.List;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.PhaseProperties;
import org.pfaa.chemica.model.SimpleMixture;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.IndustrialMaterial.Phase;

public interface Crude extends Mixture {
	public Crude mix(IndustrialMaterial material, double weight);
	
	public enum Crudes implements Crude {
		PARAFFINS,
		NAPHTHAS,
		AROMATICS,
		ASPHALTENES,
		KEROGEN,
		ORGANOSULFURS,
		ASPHALT(new SimpleCrude(PARAFFINS, 0.1).mix(NAPHTHAS, 0.20).mix(AROMATICS, 0.5).
		        mix(ASPHALTENES, 0.20).mix(ORGANOSULFURS, 0.04))
		;

		private Crude delegate;
		
		private Crudes() {
			this(new SimpleCrude());
		}
		
		private Crudes(Crude delegate) {
			this.delegate = delegate;
		}
		
		@Override
		public List<MixtureComponent> getComponents() {
			return delegate.getComponents();
		}

		@Override
		public Crude mix(IndustrialMaterial material, double weight) {
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
