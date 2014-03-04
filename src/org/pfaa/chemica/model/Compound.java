package org.pfaa.chemica.model;

import static org.pfaa.chemica.model.Element.Elements.C;
import static org.pfaa.chemica.model.Element.Elements.Ca;
import static org.pfaa.chemica.model.Element.Elements.Ce;
import static org.pfaa.chemica.model.Element.Elements.Co;
import static org.pfaa.chemica.model.Element.Elements.Cr;
import static org.pfaa.chemica.model.Element.Elements.Fe;
import static org.pfaa.chemica.model.Element.Elements.H;
import static org.pfaa.chemica.model.Element.Elements.La;
import static org.pfaa.chemica.model.Element.Elements.Nd;
import static org.pfaa.chemica.model.Element.Elements.O;
import static org.pfaa.chemica.model.Element.Elements.P;
import static org.pfaa.chemica.model.Element.Elements.Pr;
import static org.pfaa.chemica.model.Element.Elements.Si;
import static org.pfaa.chemica.model.Element.Elements.Ti;
import static org.pfaa.chemica.model.Element.Elements.Zr;

import java.awt.Color;

import org.pfaa.chemica.model.ChemicalPhaseProperties.Gas;
import org.pfaa.chemica.model.ChemicalPhaseProperties.Liquid;
import org.pfaa.chemica.model.ChemicalPhaseProperties.Solid;
import org.pfaa.chemica.model.Formula.Part;
import org.pfaa.chemica.model.Vaporization.AntoineCoefficients;

/* Notes:
 * * We are often given dH_fus/vap but not the entropy on the other side of the transition. We can compute the entropy
 *   by e.g. S_l = dH_fus / T_m + S_s.
 */
public interface Compound extends Chemical {
	public static enum Compounds implements Compound {
		H2O(new Formula(H, O._(2)), "water", 
		    new Solid(0.917, 
		    		  new Thermo(-291, 41, 2.79, 128), 
		    		  new Hazard(0, 0, 0)), 
		    new Fusion(273), 
			new Liquid(1.00, 
					   new Thermo(-204, 1523, -3196, 2474, 3.86, -257, -489), 
					   new Hazard(0, 0, 0)), 
			new Vaporization(4.65, 1435, -64.8), 
			new Gas(new Thermo(30.1, 6.83, 6.79, -2.53, 0.0821, -251, 223)
			        .addSegment(1700, 42.0, 8.62, -1.50, 0.0981, -11.2, -272, 220), 
					new Hazard(0, 0, 0))),
		CO2(new Formula(C, O._(2)), null, 
			new Solid(1.56, 
					  new Thermo(-427, 51.1, 0, 300), 
					  new Hazard(3, 0, 0)), 
			null, 
			null,
			new Vaporization(new AntoineCoefficients(6.81, 1301, -3.49)), 
			new Gas(new Thermo(25.0, 55.2, -33.7, 7.95, -0.137, -404, 228)
					.addSegment(1200, 58.2, 2.72, -0.492, 0.0388, -6.45, -426, 264), 
					new Hazard(2, 0, 0))),	
		//AlOH3(new Formula(Al), "bauxite", new Solid(Color.WHITE, 2.42, -1277, 71)),
		//BaSO4("barium", new Fusion(1345, 40.6), new Solid(Color.WHITE, 4.5, -1465, 132, new Hazard(0, 0, 0))),
		CaCO3(new Formula(Ca, new Part(C, O._(3))), "calcite", 
			  new Solid(2.7, 
					    new Thermo(-1207, 93, 82.3, 49.8, 0, 1.29))),
		//CaWO4("tungstate", new Solid(Color.ORANGE, 6.1, -1645, 126)),
		//CeCO3F("bastnasite", new Solid(new Color(177, 81, 39), 4.4, )
		CePO4(new Formula(Ce, new Part(P, O._(4))), "cerium",
			   new Solid(new Color(184, 83, 43), 5.22, null)),
	    CoO(new Formula(Co, O), "cobalt", 
			new Solid(Color.black, 6.44, 
					  new Thermo(43.7, 22.4, -16.9, 6.56, 0.532, -250, 103)
			          .addSegment(1600, 36.0, 14.1, 1.16, -0.116, 5.16, -238, 106),
					  new Hazard(2, 0, 0)),
					  new Fusion(2103)),
		//CuFeS2("copper", new Fusion(1223, Double.NaN), new Solid(new Color(195, 163, 104), 4.2, -194, 125)),
		Fe2O3(new Formula(Fe._(2), O._(3)), "iron", 
			  new Solid(new Color(200, 42, 42), 5.2, 
					    new Thermo(93.4, 108, 50.9, 25.6, -1.61, -863, 161)
			            .addSegment(950, 151, 0, 0, 0, 0, -876, 253)
			            .addSegment(1050, 111, 32, -9.19, 0.902, 5.43, -843, 228))),
		Fe3O4(new Formula(Fe._(3), O._(4)), "iron", 
			  new Solid(Color.BLACK, 5.17, 
					    new Thermo(104, 179, 10.6, 1.13, -0.994, -1163, 212)
			  			.addSegment(900, 201, 1.59, -6.66, 9.45, 3.19, -1174, 388)), 
			  new Fusion(1870),
			  new Liquid(new Thermo(164))),
		FeOH3(new Formula(Fe, new Part(O, H)._(3)), "iron", 
			  new Solid(new Color(229, 60, 0), 4.25, 
					    new Thermo(65.1, 182, -101, 19, -0.825, -862, 128))),
		FeCr2O4(new Formula(Fe, Cr._(2), O._(4)), "chromium", 
				new Solid(new Color(25, 10, 10), 4.7, new Thermo(-1438, 152, 160, 31.8, -6.33, 3.06)),
				new Fusion(2500)),
		FeTiO3(new Formula(Fe, Ti, O._(3)), "titanium", 
			   new Solid(new Color(25, 10, 10), 4.6, 
					     new Thermo(-1234, 106, 110, 38.8, -10.3, 1.91),
					     new Hazard(1, 0, 0)), 
			   new Fusion(1638),
			   new Liquid(new Thermo(161))),
		//FeWO4("tungstate", new Solid(new Color(15, 10, 10), 7.5, -1154, 132)),
		//NiS("nickel", new Fusion(1070, 30.1), new Solid(Color.BLACK, 5.8, -82, 53)),
		//HgS("cinnabar", new Solid(new Color(139, 0, 0), 8.1, -58, 78, new Hazard(2, 1, 0))),
		LaPO4(new Formula(La, new Part(P, O._(4))), "lanthanum",
			  new Solid(new Color(184, 83, 43), 5.22, null)),
		//MgCO3("magnesite", new Solid(Color.WHITE, 3.0, -1113, 66)),
		//Mg2SiO4("olivine", new Fusion(2170, 71), new Solid(new Color(154, 205, 50), 3.2, -2175, 95)),
		//MnO2("manganese", new Solid(new Color(15, 10, 10), 5.0, -520, 53, new Hazard(1, 1, 2, SpecialCode.OXIDIZER))),
		//MoS2("molybdenum", new Solid(new Color(26, 26, 26), 5.1, -234, 62, new Hazard(1, 1, 0))),
		//Na2B4O7("borax", new Fusion(1016, 81), new Solid(Color.WHITE, 1.7, -3276, 189)),
		//NaCl("salt", new Fusion(1074, 30), new Solid(Color.WHITE, 2.2, -411, 72)),
		NdPO4(new Formula(Nd, new Part(P, O._(4))), "neodynium",
			  new Solid(new Color(184, 83, 43), 5.22, null)),	  
		//NiO(new Formula(Ni, O), "nickel", new Solid(Color.GREEN, 2.7, -240, 38, new Hazard(2, 0, 0)), new Fusion(2228)),
		//PbS("lead", new Fusion(1391, 17), new Solid(new Color(26, 26, 26), 7.6, -100, 91, new Hazard(2, 0, 0))),
		PrPO4(new Formula(Pr, new Part(P, O._(4))), "praesodynium",
			  new Solid(new Color(184, 83, 43), 5.22, null)),
		//Sb2S3("antimony", new Fusion(823, 40), new Solid(Color.GRAY, 4.64, -140, 182, new Hazard(2, 1, 0))),
		//SiO2("quartz", new Fusion(1900, 14), new Solid(Color.WHITE, 2.6, -911, 42)),
		//SnO2("tin", new Fusion(1903, Double.NaN), new Solid(Color.WHITE, 7.0, -581, 52, new Hazard(2, 0, 0))),
		TiO2(new Formula(Ti, O._(2)), "titanium", 
		     new Solid(4.23, 
		    		   new Thermo(67, 18.7, -11.6, 2.45, -1.49, -965, 117), 
		    		   new Hazard(1, 0, 0)), 
		     new Fusion(2116),
		     new Liquid(new Thermo(78.0)),
		     new Vaporization(3245),
		     new Gas(new Thermo(285))),
		//ZnS("zinc", new Solid(Color.WHITE, 4.1, -206, 58))
		ZrSiO4(new Formula(Zr, Si, O._(4)), "zirconium",
			   new Solid(new Color(210, 210, 210), 4.56,
					     new Thermo(85.2, 137, -94.6, 21.7, -1.74, -2061, 140)
			                        .addSegment(1700, 151, 0, 0, 0, 0, -2082, 238),
			             new Hazard(1, 0, 1)))
		;
		
		private Chemical delegate;
		
		private Compounds(Formula formula, String oreDictKey, ChemicalPhaseProperties solid, Fusion fusion, 
				ChemicalPhaseProperties liquid,	Vaporization vaporization, ChemicalPhaseProperties gas) 
		{
			this.delegate = new SimpleChemical(formula, oreDictKey, solid, fusion, liquid, vaporization, gas);
		}
		
		private Compounds(Formula formula, String oreDictKey, ChemicalPhaseProperties solid, Fusion fusion, 
				ChemicalPhaseProperties liquid,	Vaporization vaporization) 
		{
			this(formula, oreDictKey, solid, fusion, liquid, vaporization, new Gas());
		}
		
		private Compounds(Formula formula, String oreDictKey, ChemicalPhaseProperties solid, Fusion fusion, 
				ChemicalPhaseProperties liquid) 
		{
			this(formula, oreDictKey, solid, fusion, liquid, null);
		}
		
		private Compounds(Formula formula, String oreDictKey, ChemicalPhaseProperties solid, Fusion fusion) {
			this(formula, oreDictKey, solid, fusion, new Liquid());
		}
		
		private Compounds(Formula formula, String oreDictKey, ChemicalPhaseProperties solid) {
			this(formula, oreDictKey, solid, null);
		}
	
		@Override
		public Fusion getFusion() {
			return delegate.getFusion();
		}
	
		@Override
		public Vaporization getVaporization() {
			return delegate.getVaporization();
		}
	
		@Override
		public String getOreDictKey() {
			return delegate.getOreDictKey();
		}
	
		@Override
		public ChemicalPhaseProperties getProperties(IndustrialMaterial.Phase phase) {
			return delegate.getProperties(phase);
		}

		@Override
		public Formula getFormula() {
			return delegate.getFormula();
		}
	}
}
