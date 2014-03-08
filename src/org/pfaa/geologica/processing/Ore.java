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
 * Minerals from which valuable compounds and elements are extracted.
 * An ore can consist of multiple compounds, due to non-stoichiometry.
 */
public interface Ore extends Mixture {
	public Chemical getConcentrate();
	public Ore add(IndustrialMaterial material, double weight);
	
	public static enum SmeltingTemperature {
		LOW, MEDIUM, HIGH, VERY_HIGH
	}
	
	
	public enum Ores implements Ore {
		ANATASE(Compounds.TiO2),
		CALCITE(Compounds.CaCO3),
		CASSITERITE(Compounds.SnO2), 
		CHROMITE(Compounds.FeCr2O4),
		GIBBSITE(new SimpleOre(Compounds.AlOH3).add(Compounds.GaOH3, 0.001)),
		GOETHITE(Compounds.alpha_FeOH3),
		LEPIDOCROCITE(Compounds.gamma_FeOH3),
		HEMATITE(Compounds.Fe2O3),
		ILMENITE(Compounds.FeTiO3),
		MAGNETITE(Compounds.Fe3O4),
		MONAZITE(new SimpleOre(Compounds.CePO4).add(Compounds.LaPO4, 0.5).add(Compounds.NdPO4, 0.3)
				.add(Compounds.PrPO4, 0.15).add(Elements.Th, 0.5)),
		RUTILE(Compounds.TiO2),
		ZIRCON(Compounds.ZrSiO4)
		;

		private Ore delegate;
		
		private Ores(Ore delegate) {
			this.delegate = delegate;
		}
		
		private Ores(Chemical material) {
			this(new SimpleOre(material));
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
