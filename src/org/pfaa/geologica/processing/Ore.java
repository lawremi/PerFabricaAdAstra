package org.pfaa.geologica.processing;

import java.util.List;

import org.pfaa.chemica.model.Element.Elements;
import static org.pfaa.chemica.model.Element.Elements.Th;
import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.PhaseProperties;

/*
 * An Ore is a mixture from which we are trying to extract one or more chemicals.
 */
public interface Ore extends Mixture {
	public Chemical getConcentrate();
	public Ore add(IndustrialMaterial material, double weight);
	
	public static enum SmeltingTemperature {
		LOW, MEDIUM, HIGH, VERY_HIGH
	}
	
	/* Ores that are not do not form a GeoMaterial, i.e., ores not found directly in the world. */
	public enum Ores implements Ore {
		MONAZITE(new SimpleOre(Compounds.CePO4).add(Compounds.LaPO4, 0.5).add(Compounds.NdPO4, 0.3)
				.add(Compounds.PrPO4, 0.15).add(Elements.Th, 0.5))
		;

		private Ore delegate;
		
		private Ores(Ore delegate) {
			this.delegate = delegate;
		}
		
		public String getOreDictKey() {
			return delegate.getOreDictKey();
		}

		@Override
		public List<MixtureComponent> getComponents() {
			return delegate.getComponents();
		}

		@Override
		public PhaseProperties getProperties(Phase phase) {
			return delegate.getProperties(phase);
		}

		@Override
		public Chemical getConcentrate() {
			return delegate.getConcentrate();
		}

		@Override
		public Ore add(IndustrialMaterial material, double weight) {
			return delegate.add(material, weight);
		}
		
	}
}
