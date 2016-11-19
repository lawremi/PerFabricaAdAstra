package org.pfaa.geologica.processing;

import static org.pfaa.chemica.model.Element.Ag;
import static org.pfaa.chemica.model.Element.Au;
import static org.pfaa.chemica.model.Element.Ba;
import static org.pfaa.chemica.model.Element.Bi;
import static org.pfaa.chemica.model.Element.Cd;
import static org.pfaa.chemica.model.Element.Ce;
import static org.pfaa.chemica.model.Element.Co;
import static org.pfaa.chemica.model.Element.Cs;
import static org.pfaa.chemica.model.Element.Fe;
import static org.pfaa.chemica.model.Element.Ga;
import static org.pfaa.chemica.model.Element.Ge;
import static org.pfaa.chemica.model.Element.In;
import static org.pfaa.chemica.model.Element.Ir;
import static org.pfaa.chemica.model.Element.La;
import static org.pfaa.chemica.model.Element.Nb;
import static org.pfaa.chemica.model.Element.Nd;
import static org.pfaa.chemica.model.Element.Ni;
import static org.pfaa.chemica.model.Element.Pd;
import static org.pfaa.chemica.model.Element.Pr;
import static org.pfaa.chemica.model.Element.Pt;
import static org.pfaa.chemica.model.Element.Rb;
import static org.pfaa.chemica.model.Element.Re;
import static org.pfaa.chemica.model.Element.Rh;
import static org.pfaa.chemica.model.Element.Ru;
import static org.pfaa.chemica.model.Element.S;
import static org.pfaa.chemica.model.Element.Se;
import static org.pfaa.chemica.model.Element.Sm;
import static org.pfaa.chemica.model.Element.Sr;
import static org.pfaa.chemica.model.Element.Ta;
import static org.pfaa.chemica.model.Element.Te;
import static org.pfaa.chemica.model.Element.Th;
import static org.pfaa.chemica.model.Element.U;
import static org.pfaa.chemica.model.Element.Y;

import java.awt.Color;
import java.util.List;

import org.pfaa.chemica.model.Alloy.Alloys;
import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.Element;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;

import com.google.common.base.CaseFormat;


/*
 * Minerals from which valuable compounds and elements are extracted.
 * Like any mineral, an ore can consist of multiple compounds, due to non-stoichiometry.
 */
public interface OreMineral extends Mineral {
	public Chemical getConcentrate();
	
	public enum Ores implements OreMineral {
		ACANTHITE(Compounds.Ag2S),
		ANATASE(Compounds.TiO2),
		ARSENOPYRITE(Compounds.FeAsS),
		AZURITE(Compounds.Cu3CO32OH2),
		BARITE(Compounds.BaSO4, new Color(250, 250, 170), new Substitution(Sr, 0.05)),
		BASTNASITE(Compounds.CeCO3F, new Substitution(La, 0.2), new Substitution(Y, 0.1), new Substitution(Sm, 0.01)),
		BERYL(Compounds.Be3Al2SiO36),
		BISMUTHINITE(Compounds.Bi2S3),
		BORAX(Compounds.Na2B4O7),
		CALCITE(Compounds.CaCO3),
		CARNALLITE(Compounds.KCl.mix(Compounds.MgCl2_6H2O, 1.0), new Color(255, 255, 204)),
		CARNOTITE(Compounds.K2U2V2O12_3H2O),
		CASSITERITE(Compounds.SnO2, new Color(30, 20, 0)),
		CELESTINE(Compounds.SrSO4, new Color(200, 200, 250), new Substitution(Ba, 0.2)),
		CHALCOCITE(Compounds.Cu2S),
		CHALCOPYRITE(Compounds.CuFeS2, new Substitution(Ag, 0.01), new Substitution(Se, 0.01), new Substitution(Te, 0.001)),
		CHROMITE(Compounds.FeCr2O4),
		CINNABAR(Compounds.HgS),
		COBALTITE(Compounds.CoAsS),
		COLUMBITE(Compounds.FeNb2O6, new Substitution(Ta, 0.2)),
		CUPRITE(Compounds.Cu2O, Color.red),
		EPSOMITE(Compounds.MgSO4_7H2O),
		FLUORITE(Compounds.CaF2, new Color(155, 0, 165)),
		GALENA(Compounds.PbS, new Substitution(Bi, 0.1), new Substitution(Ag, 0.05)),
		GIBBSITE(Compounds.AlOH3, new Color(155, 75, 35), new Substitution(Ga, 0.001)),
		GOETHITE(Compounds.alpha_FeOH3, new Substitution(Ni, 0.05), new Substitution(Co, 0.01)),
		GREENOCKITE(Compounds.CdS),
		GYPSUM(Compounds.CaSO4_2H2O),
		HALITE(Compounds.NaCl, new Color(255, 205, 205)),
		LAUTARITE(Compounds.CaI2O6, new Color(255, 255, 170)),
		LEPIDOCROCITE(Compounds.gamma_FeOH3),
		HEMATITE(Compounds.Fe2O3),
		ILMENITE(Compounds.FeTiO3),
		LEPIDOLITE(Compounds.Li3KSi4O10OH2, new Substitution(Rb, 0.01), new Substitution(Cs, 0.005)),
		MAGNETITE(Compounds.Fe3O4),
		MAGNESITE(Compounds.MgCO3),
		MALACHITE(Compounds.Cu2CO3OH2),
		MICROLITE(Compounds.NaCaTa2O6OH, new Substitution(Nb, 0.2), 
				  new Substitution(Ce, 0.03), new Substitution(La, 0.015), 
		          new Substitution(Nd, 0.01), new Substitution(Y, 0.01), 
		          new Substitution(Th, 0.01), new Substitution(U, 0.005)),
		MOLYBDENITE(Compounds.MoS2, new Substitution(Re, 0.01)),
		MONAZITE(Compounds.CePO4, new Substitution(La, 0.5), new Substitution(Nd, 0.3),
				 new Substitution(Pr, 0.15), new Substitution(Th, 0.5), new Substitution(Sm, 0.05)),
		NEPOUITE(Compounds.Ni3Si2O5OH4),
		NITER(Compounds.KNO3),
		NITRATINE(Compounds.NaNO3, new Color(222, 184, 135)),
		ORPIMENT(Compounds.As2S3),
		PENTLANDITE(Compounds.Ni9S8, new Substitution(Fe, 1.0), new Substitution(Co, 0.01)),
		POLLUCITE(Compounds.Cs2Al2Si4O12, new Substitution(Rb, 0.05)),
		PYRITE(Compounds.FeS2, new Substitution(Ni, 0.1), new Substitution(Co, 0.05)),
		// FIXME: how to indicate that Nb is being substituted?
		PYROCHLORE(Compounds.NaCaNb2O6OH, new Substitution(Ta, 0.1), 
				   new Substitution(Ce, 0.03), new Substitution(La, 0.015), 
		           new Substitution(Nd, 0.01), new Substitution(Y, 0.01), 
		           new Substitution(Th, 0.01), new Substitution(U, 0.005)),
		PYROLUSITE(Compounds.MnO2),
		QUARTZ(Compounds.SiO2),
		REALGAR(Compounds.AsS),
		RUTILE(Compounds.TiO2, new Color(130, 60, 5)),
		SCHEELITE(Compounds.CaWO4, new Color(240, 200, 150)),
		SPHALERITE(Compounds.ZnS, new Color(30, 75, 50), 
				   new Substitution(Cd, 0.01), new Substitution(Ga, 0.005), 
				   new Substitution(Ge, 0.002), new Substitution(In, 0.002)),
		SPODUMENE(Compounds.LiAlSiO32),
		STIBNITE(Compounds.Sb2S3),
		SYLVITE(Compounds.KCl, new Color(255, 230, 205)),
		TANTALITE(Compounds.FeTa2O6, new Substitution(Nb, 0.1)),
		TENNANTITE(Compounds.Cu12As4S13),
		TETRAHEDRITE(Compounds.Cu12Sb4S13),
		TITANO_MAGNETITE(Compounds.Fe3O4.mix(Compounds.TiO2, 0.3).mix(Compounds.V2O5, 0.1)),
		URANINITE(Compounds.UO2),
		VANADINITE(Compounds.Pb5V3O12Cl),
		WOLFRAMITE(Compounds.FeWO4),
		ZIRCON(Compounds.ZrSiO4, new Color(130, 60, 5)),
		
		GOLD(Au),
		ELECTRUM(Alloys.ELECTRUM),
		PLATINUM(Pt.mix(Pd, 1.0).mix(Ru, 0.25).mix(Rh, 0.2)
				.mix(Ir, 0.01).mix(Alloys.IRIDOSMINE, 0.01)),
		SULFUR(S)
		;

		private OreMineral delegate;
		
		private Ores(OreMineral delegate) {
			this.delegate = delegate;
		}
		
		private Ores(Mixture mixture) {
			this(mixture, null);
		}
		
		private Ores(Mixture mixture, Color color) {
			this(new SimpleOreMineral(mixture, color));
		}
		
		private Ores(Chemical material, Substitution... substitution) {
			this(material, null, substitution);
		}
		
		private Ores(Chemical material, Color color, Substitution... substitution) {
			this(new SimpleOreMineral(material, color, substitution));
		}
		
		public String getOreDictKey() {
			return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
		}

		@Override
		public List<MixtureComponent> getComponents() {
			return delegate.getComponents();
		}

		@Override
		public ConditionProperties getProperties(Condition condition) {
			return delegate.getProperties(condition);
		}

		@Override
		public Chemical getConcentrate() {
			return delegate.getConcentrate();
		}

		@Override
		public Ore mix(IndustrialMaterial material, double weight) {
			return new SimpleOre(this).mix(material, weight);
		}
	}
	
	public static class Substitution extends MixtureComponent {
		public Substitution(Element substitute, double rate) {
			super(substitute, rate);
		}
		
		@Override
		public Element getMaterial() {
			return (Element)this.material;
		}
	}
}
