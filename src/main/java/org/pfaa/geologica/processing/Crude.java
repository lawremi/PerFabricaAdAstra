package org.pfaa.geologica.processing;

import java.awt.Color;
import java.util.List;

import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.Hazard;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.Phase;
import org.pfaa.chemica.model.PhaseProperties;
import org.pfaa.chemica.model.Vaporizable;
import org.pfaa.chemica.model.Vaporization;

/* Crude mixtures of organic substances */
public interface Crude extends Mixture, Vaporizable {
	public Crude mix(IndustrialMaterial material, double weight);
	
	public enum Crudes implements Crude {
		FUEL_GAS(new SimpleCrude(Compounds.PROPANE, 0.3).mix(Compounds.N_BUTANE, 0.35).mix(Compounds.ISO_BUTANE, 0.35).
				 mix(Compounds.H2S, 0.001)),
		LIGHT_NAPHTHA(Phase.LIQUID, new Color(165, 145, 40), 0.6, new Hazard(1, 4, 0), 0.9,  250, 0.002),
		HEAVY_NAPHTHA(Phase.LIQUID, new Color(130, 115, 25), 0.7, new Hazard(1, 3, 0), 1.1, 500, 0.004),
		KEROSENE(Phase.LIQUID, new Color(100, 90, 10), 0.8, new Hazard(0, 2, 0), 1.64, 600, 0.007),
		LIGHT_GAS_OIL(Phase.LIQUID, new Color(90, 70, 20), 0.9, new Hazard(0, 2, 0), 2.25, 700, 0.02),
		HEAVY_GAS_OIL(Phase.LIQUID, new Color(50, 25, 0), 1.0, new Hazard(1, 2, 0), 30, 900, 0.03),
		BITUMEN(Phase.LIQUID, Color.black, 1.2, new Hazard(1, 2, 0), 240, 1000, 0.04),
		// no better place to put this one
		KEROGEN(Phase.SOLID, new Color(165, 125, 30), 1.2, new Hazard(), Double.NaN, Double.NaN, Double.NaN),
		FIXED_CARBON(Phase.SOLID, Color.darkGray, 1.1, new Hazard(), Double.NaN, Double.NaN, 0.006)
		;

		private Crude delegate;
		
		private Crudes(Phase phase, Color color, double density, Hazard hazard, double viscosity, 
				       double boilingTemperature, double sulfurFraction) {
			this(new SimpleCrude(new ConditionProperties(phase, color, density, hazard, viscosity, 0, true), 
				 Double.isNaN(boilingTemperature) ? null : new Vaporization(boilingTemperature), 
				 sulfurFraction));
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
		public ConditionProperties getProperties(Condition condition) {
			return delegate.getProperties(condition);
		}

		@Override
		public Vaporization getVaporization() {
			return delegate.getVaporization();
		}
	}
}
