package org.pfaa.chemica.model;

import java.awt.Color;

import org.pfaa.chemica.model.ChemicalStateProperties.Aqueous;
import org.pfaa.chemica.model.Formula.PartFactory;

public interface Ion extends PartFactory, Chemical {
	
	public enum Ions implements Ion {
		/* cations */
		Ag(Element.Ag, +1, new Thermo(106, 72.7)),
		Al(Element.Al, +3, new Thermo(-531, -322)),
		As(Element.As, +3, null),
		Ba(Element.Ba, +2, new Thermo(-538, 9.6)),
		Bi(Element.Bi, +3, null),
		Be(Element.Be, +2, new Thermo(-383, -130)),
		Br(Element.Br, -1, new Thermo(-122, 82.4)),
		Ca(Element.Ca, +2, new Thermo(-543, -53.1)),
		Cd(Element.Cd, +2, new Thermo(-75.9, -73.2)),
		Ce(Element.Ce, +3, new Thermo(-696, -205)),
		Co(Element.Co, +2, new Color(255, 170, 180), new Thermo(-58.2, -113)),
		Cs(Element.Cs, +1, new Thermo(-258, 133)),
		Cu(Element.Cu, +2, Color.blue, new Thermo(64.8, -99.6)),
		Fe_2(Element.Fe, +2, new Color(185, 255, 175), new Thermo(-89.1, -138)),
		Fe_3(Element.Fe, +3, new Color(255, 190, 225), new Thermo(-48.5, -316)),
		H(Element.H, +1, new Thermo(0, 0)),
		Hg(Element.Hg, +2, new Thermo(171, -32.2)),
		K(Element.K, +1, new Thermo(-252, 103)),
		Li(Element.Li, +1, new Thermo(-279, 13.4)),
		Mg(Element.Mg, +2, new Thermo(-467, -138)),
		Mn(Element.Mn, +2, new Color(255, 170, 180), new Thermo(-221, -73.6)),
		Mo(Element.Mo, +4, null),
		Na(Element.Na, +1, new Thermo(-240, 59)),
		Ni(Element.Ni, +2, new Color(185, 255, 175), new Thermo(-54, -129)),
		Pb(Element.Pb, +2, new Thermo(-1.7, 10.5)),
		Sb(Element.Sb, +3, null),
		Sn(Element.Sn, +2, new Thermo(-8.8, -17)),
		Sr(Element.Sr, +2, new Thermo(-545, -32.6)),
		Ti(Element.Ti, +4, null),
		U(Element.U, +4, new Thermo(-591, -410)),
		UO2(new Formula(Element.U, Element.O._(2)), +2, null),
		Zn(Element.Zn, +2, new Thermo(-154, -112)),
		Zr(Element.Zr, +4, null),
		
		/* anions */
		AsS(new Formula(Element.As, Element.S), -3, null),
		CO3(new Formula(Element.C, Element.O._(3)), -2, new Thermo(-677, -56.9)),
		Cl(Element.Cl, -1, new Thermo(-167, 56.5)),
		CrO4(new Formula(Element.Cr, Element.O._(4)), -2, Color.yellow, null),
		F(Element.F, -1, new Thermo(-256, 145.5)),
		IO3(new Formula(Element.I, Element.O._(3)), -1, new Thermo(-221, 118)),
		NO2(new Formula(Element.N, Element.O._(2)), -1, new Thermo(-100, Double.NaN)),
		NO3(new Formula(Element.N, Element.O._(3)), -2, new Thermo(-207, 146)),
		NbO3(new Formula(Element.Nb._(2), Element.O._(6)), -1, null),
		O(Element.O, -2, null),
		OH(new Formula(Element.O, Element.H), -1, new Thermo(-230, -10.8)),
		PO4(new Formula(Element.P, Element.O._(4)), -3, new Thermo(-1278, -222)),
		S(Element.S, -2, new Thermo(33.1, -14.7)),
		S2(Element.S, -2, null),
		SiO3(new Formula(Element.Si, Element.O._(3)), -2, null),
		SiO4(new Formula(Element.Si, Element.O._(4)), -4, null),
		Si2O5(new Formula(Element.Si._(2), Element.O._(5)), -2, null),
		SO4(new Formula(Element.S, Element.O._(4)), -2, new Thermo(-909, 20.1)),
		Ta2O6(new Formula(Element.Ta._(2), Element.O._(6)), -2, null),
		VO4(new Formula(Element.V, Element.O._(4)), -3, null),
		V2O5(new Formula(Element.V._(2), Element.O._(5)), -2, null),
		WO4(new Formula(Element.W, Element.O._(4)), -2, null),
		B4O5OH2(new Formula(Element.B._(4), Element.O._(5), OH._(2)), -2, null);

		private Ion delegate;
		
		private Ions(Element element, int charge, Color color, Thermo aqueousThermo) {
			this(new Formula(element), charge, color, aqueousThermo);
		}
		
		private Ions(Element element, int charge, Thermo aqueousThermo) {
			this(new Formula(element), charge, null, aqueousThermo);
		}
		
		private Ions(Formula formula, int charge, Thermo aqueousThermo) {
			this(formula, charge, null, aqueousThermo);
		}
		
		private Ions(Formula formula, int charge, Color color, Thermo aqueousThermo) {
			this.delegate = new SimpleIon(formula.ionize(charge), new Aqueous(color, aqueousThermo));
		}
		
		@Override
		public Formula getFormula() {
			return this.delegate.getFormula();
		}

		public Formula.Part _(int quantity) {
			return this.delegate._(quantity);
		}

		@Override
		public Formula.Part getPart() {
			return this.delegate.getPart();
		}

		@Override
		public ChemicalConditionProperties getProperties(Condition condition) {
			return this.delegate.getProperties(condition);
		}

		@Override
		public ChemicalConditionProperties getProperties(Condition condition, State state) {
			return this.delegate.getProperties(condition, state);
		}

		@Override
		public String getOreDictKey() {
			return this.delegate.getOreDictKey();
		}

		@Override
		public Mixture mix(IndustrialMaterial material, double weight) {
			return this.delegate.mix(material, weight);
		}

		@Override
		public Vaporization getVaporization() {
			return this.delegate.getVaporization();
		}

		@Override
		public Fusion getFusion() {
			return this.delegate.getFusion();
		}

		@Override
		public Reaction getDissolution() {
			return this.delegate.getDissolution();
		}
	}
}
