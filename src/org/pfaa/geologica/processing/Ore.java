package org.pfaa.geologica.processing;

import static org.pfaa.chemica.model.Element.Elements.Ba;
import static org.pfaa.chemica.model.Element.Elements.Cd;
import static org.pfaa.chemica.model.Element.Elements.Co;
import static org.pfaa.chemica.model.Element.Elements.Cs;
import static org.pfaa.chemica.model.Element.Elements.Fe;
import static org.pfaa.chemica.model.Element.Elements.Ga;
import static org.pfaa.chemica.model.Element.Elements.La;
import static org.pfaa.chemica.model.Element.Elements.Mn;
import static org.pfaa.chemica.model.Element.Elements.Nd;
import static org.pfaa.chemica.model.Element.Elements.Ni;
import static org.pfaa.chemica.model.Element.Elements.Pr;
import static org.pfaa.chemica.model.Element.Elements.Rb;
import static org.pfaa.chemica.model.Element.Elements.Re;
import static org.pfaa.chemica.model.Element.Elements.Sr;
import static org.pfaa.chemica.model.Element.Elements.Th;
import static org.pfaa.chemica.model.Element.Elements.Y;

import java.util.List;

import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.ChemicalPhaseProperties.Solid;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.CompoundDictionary;
import org.pfaa.chemica.model.Element;
import org.pfaa.chemica.model.Formula;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.PhaseProperties;
import org.pfaa.chemica.model.SimpleChemical;


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
		ACANTHITE(Compounds.Ag2S),
		ANATASE(Compounds.TiO2),
		BARITE(Compounds.BaSO4, new Substitution(Sr, 0.05)),
		BASTNASITE(Compounds.CeCO3F, new Substitution(La, 0.2), new Substitution(Y, 0.1)),
		BISMUTHINITE(Compounds.Bi2S3),
		BORAX(Compounds.Na2B4O7),
		CALCITE(Compounds.CaCO3),
		CARNALLITE(new SimpleOre(Compounds.KCl).add(Compounds.MgCl2, 1.0)),
		CASSITERITE(Compounds.SnO2),
		CELESTINE(Compounds.SrSO4, new Substitution(Ba, 0.2)),
		CHALCOPYRITE(Compounds.CuFeS2),
		CHROMITE(Compounds.FeCr2O4),
		CINNABAR(Compounds.HgS),
		COBALTITE(Compounds.CoAsS),
		FLUORITE(Compounds.CaF2),
		GALENA(Compounds.PbS),
		GIBBSITE(Compounds.AlOH3, new Substitution(Ga, 0.001)),
		GOETHITE(Compounds.alpha_FeOH3, new Substitution(Ni, 0.05), new Substitution(Co, 0.01)),
		GREENOCKITE(Compounds.CdS),
		GYPSUM(Compounds.CaSO4_2H2O),
		HALITE(Compounds.NaCl),
		LEPIDOCROCITE(Compounds.gamma_FeOH3),
		HEMATITE(Compounds.Fe2O3),
		ILMENITE(Compounds.FeTiO3),
		LEPIDOLITE(Compounds.Li3KSi4O10OH2, new Substitution(Rb, 0.01), new Substitution(Cs, 0.005)),
		MAGNETITE(Compounds.Fe3O4),
		MAGNESITE(Compounds.MgCO3),
		MOLYBDENITE(Compounds.MoS2, new Substitution(Re, 0.01)),
		MONAZITE(Compounds.CePO4, new Substitution(La, 0.5), new Substitution(Nd, 0.3),
				 new Substitution(Pr, 0.15), new Substitution(Th, 0.5)),
		NEPOUITE(Compounds.Ni3Si2O5OH4),
		PENTLANDITE(Compounds.Ni9S8, new Substitution(Fe, 1.0), new Substitution(Co, 0.01)),
		PYRITE(Compounds.FeS2, new Substitution(Ni, 0.1), new Substitution(Co, 0.05)),
		PYROLUSITE(Compounds.MnO2),
		REALGAR(Compounds.AsS),
		RUTILE(Compounds.TiO2),
		SCHEELITE(Compounds.CaWO4),
		SPHALERITE(Compounds.ZnS, new Substitution(Cd, 0.01), new Substitution(Ga, 0.001)),
		SPODUMENE(Compounds.LiAlSiO32),
		STIBNITE(Compounds.Sb2S3),
		SYLVITE(Compounds.KCl),
		WOLFRAMITE(Compounds.FeWO4, new Substitution(Mn, 1.0)),
		ZIRCON(Compounds.ZrSiO4)
		;

		private Ore delegate;
		
		private Ores(Ore delegate) {
			this.delegate = delegate;
		}
		
		private Ores(Chemical material, Substitution... substitution) {
			this(createDelegate(material, substitution));
		}
		
		private static Ore createDelegate(Chemical material, Substitution[] substitutions) {
			Ore delegate = new SimpleOre(material);
			for (Substitution substitution : substitutions) {
				Formula formula = material.getFormula().substituteFirstPart(substitution.substitute);
				Chemical compound = CompoundDictionary.lookup(formula);
				if (compound == null) {
					compound = new SimpleChemical(formula, substitution.substitute.getOreDictKey(), new Solid());
				}
				delegate.add(compound, substitution.rate);
			}
			return delegate;
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
	
	public static class Substitution {
		public final Element substitute;
		public final double rate;
		
		public Substitution(Element substitute, double rate) {
			super();
			this.substitute = substitute;
			this.rate = rate;
		}
	}
}
