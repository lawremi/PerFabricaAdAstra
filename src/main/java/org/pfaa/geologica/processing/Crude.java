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
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.model.Vaporizable;
import org.pfaa.chemica.model.Vaporization;

import com.google.common.base.CaseFormat;

/* Crude mixtures of organic substances */
// TODO: varied support for thermodynamics, particularly heat capacity and heat of vaporization, 
//       see https://en.wikipedia.org/wiki/Petroleum#Empirical_equations_for_thermal_properties.
public interface Crude extends Mixture, Vaporizable {
	public Crude mix(IndustrialMaterial material, double weight);
	public Crude oreDictify(String oreDictKey);
	public Crude extract(IndustrialMaterial... materials);
	public Crude removeAll();
	public double getHeat(); /* kJ/g */
	public double getSulfurFraction();
	public List<Vaporizable> fractions();
	
	public enum Crudes implements Crude {
		VOLATILES(new SimpleCrude(Compounds.METHANE, 0.5).mix(Compounds.ETHANE, 0.3).mix(Compounds.PROPANE, 0.15).
				      mix(Compounds.N_BUTANE, 0.024).mix(Compounds.ISO_BUTANE, 0.024).mix(Compounds.H2S, 0.002)),
		LIGHT_NAPHTHA(State.LIQUID, new Color(165, 145, 40), 0.6, new Hazard(1, 4, 0), 0.9,  250, 0.002, 45),
		HEAVY_NAPHTHA(State.LIQUID, new Color(130, 115, 25), 0.7, new Hazard(1, 3, 0), 1.1, 500, 0.004, 44),
		KEROSENE(State.LIQUID, new Color(100, 90, 10), 0.8, new Hazard(2, 2, 0), 2, 600, 0.007, 46),
		LIGHT_GAS_OIL(State.LIQUID, new Color(90, 70, 20), 0.9, new Hazard(1, 2, 0), 2.25, 700, 0.02, 47),
		HEAVY_GAS_OIL(State.LIQUID, new Color(50, 25, 0), 1.0, new Hazard(1, 2, 0), 30, 900, 0.03, 48),
		BITUMEN(State.LIQUID, Color.black, 1.2, new Hazard(1, 2, 0), 4000, 1000, 0.04, 39),
		KEROGEN(State.SOLID, new Color(100, 90, 60), 1.2, new Hazard(), 0, 20),
		FIXED_CARBON(State.SOLID, new Color(20, 20, 20), 1.1, new Hazard(), 0.006, 33),
		COAL_TAR(new SimpleCrude(Crudes.HEAVY_NAPHTHA, 0.05).mix(Crudes.KEROSENE, 0.05).
				 mix(Crudes.LIGHT_GAS_OIL, 0.10).mix(Crudes.HEAVY_GAS_OIL, 0.2).
				 mix(Crudes.BITUMEN, 0.6)),
		COKE(FIXED_CARBON),
		COMPOST(State.SOLID, new Color(60, 40, 0), 0.8, new Hazard(0, 1, 0), 0, 15),
		DIESEL(State.LIQUID, new Color(100, 80, 15), 0.85, new Hazard(1, 2, 0), 2.4, 650, 0.002, 45), // fuel oil #2, "bunker A"
		LIGHT_FUEL_OIL(State.LIQUID, new Color(70, 50, 10), 0.95, new Hazard(1, 2, 0), 35, 800, 0.03, 50), // fuel oil #4/5 "bunker B"
		HEAVY_FUEL_OIL(State.LIQUID, new Color(25, 10, 0), 1.0, new Hazard(1, 2, 0), 3500, 950, 0.03, 53), // fuel oil #6, "bunker C"
		;

		private Crude delegate;
		
		private Crudes(State state, Color color, double density, Hazard hazard, double sulfurFraction, double heat) {
			this(state, color, density, hazard, Double.NaN, Integer.MIN_VALUE, sulfurFraction, heat);
		}
		
		private Crudes(State state, Color color, double density, Hazard hazard, double viscosity, 
				       int boilingTemperature, double sulfurFraction, double heat) {
			this(new SimpleCrude(new ConditionProperties(state, color, density, hazard, viscosity, 0, true), 
				 boilingTemperature == Integer.MIN_VALUE ? null : new Vaporization(boilingTemperature), 
				 sulfurFraction, heat));
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
			Crude self = new SimpleCrude(this, 1.0);
			return self.mix(material, weight);
		}

		@Override
		public String getOreDictKey() {
			return delegate.getOreDictKey() == null ? 
					CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, this.name()) : 
					delegate.getOreDictKey();
		}

		@Override
		public ConditionProperties getProperties(Condition condition) {
			return delegate.getProperties(condition);
		}

		@Override
		public Vaporization getVaporization() {
			return delegate.getVaporization();
		}

		@Override
		public double getHeat() {
			return delegate.getHeat();
		}

		@Override
		public double getSulfurFraction() {
			return delegate.getSulfurFraction();
		}

		@Override
		public Crude oreDictify(String oreDictKey) {
			return delegate.oreDictify(oreDictKey);
		}

		@Override
		public Crude extract(IndustrialMaterial... materials) {
			return delegate.extract(materials);
		}

		@Override
		public Crude removeAll() {
			return delegate.removeAll();
		}

		@Override
		public List<Vaporizable> fractions() {
			return delegate.fractions();
		}
	}
}
