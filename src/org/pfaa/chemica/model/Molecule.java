package org.pfaa.chemica.model;

import static org.pfaa.chemica.model.Element.Elements.C;
import static org.pfaa.chemica.model.Element.Elements.Ca;
import static org.pfaa.chemica.model.Element.Elements.Co;
import static org.pfaa.chemica.model.Element.Elements.Fe;
import static org.pfaa.chemica.model.Element.Elements.H;
import static org.pfaa.chemica.model.Element.Elements.Ni;
import static org.pfaa.chemica.model.Element.Elements.O;
import static org.pfaa.chemica.model.Element.Elements.Ti;

import java.awt.Color;

import org.pfaa.chemica.model.ChemicalPhaseProperties.Gas;
import org.pfaa.chemica.model.ChemicalPhaseProperties.Liquid;
import org.pfaa.chemica.model.ChemicalPhaseProperties.Solid;
import org.pfaa.chemica.model.Formula.Part;
import org.pfaa.chemica.model.Thermo.Shomate;
import org.pfaa.chemica.model.Vaporization.AntoineCoefficients;

public interface Molecule extends Chemical {
	public static enum Molecules implements Molecule {
		H2O(new Formula(H, O._(2)), "water", 
		    new Solid(0.917, 
		    		  new Thermo(-291, 41, 146, 7.25), 
		    		  new Hazard(0, 0, 0)), 
		    new Fusion(273), 
			new Liquid(1.00, 
					   new Thermo(-286, 70.0, new Shomate(-204, 1523, -3196, 2474, 3.86, -257, -489)), 
					   new Hazard(0, 0, 0)), 
			new Vaporization(4.65, 1435,	-64.8), 
			new Gas(new Thermo(-242, 189, new Shomate(30.1, 6.83, 6.79, -2.53, 0.0821, -251, 223))
			        .addSegment(1700, new Shomate(42.0, 8.62, -1.50, 0.0981, -11.2, -272, 220)), 
					new Hazard(0, 0, 0))),
		CO2(new Formula(C, O._(2)), null, 
			new Solid(1.56, 
					  new Thermo(-427, 51.1, 0), 
					  new Hazard(3, 0, 0)), 
			null, 
			null,
			new Vaporization(new AntoineCoefficients(6.81, 1301, -3.49)), 
			new Gas(new Thermo(-393, 214, new Shomate(25.0, 55.2, -33.7, 7.95, -0.137, -404, 228))
					.addSegment(1200, new Shomate(58.2, 2.72, -0.492, 0.0388, -6.45, -426, 264)), 
					new Hazard(2, 0, 0))),	
		//AlOH3(new Formula(Al), "bauxite", new Solid(Color.WHITE, 2.42, -1277, 71)),
		//BaSO4("barium", new Fusion(1345, 40.6), new Solid(Color.WHITE, 4.5, -1465, 132, new Hazard(0, 0, 0))),
		CaCO3(new Formula(Ca, new Part(C, O._(3))), "calcite", 
			  new Solid(Color.WHITE, 2.7, 
					    new Thermo(-1207, 93, new Shomate(82.3, 0.0498, -12.9E5)))),
		//CaWO4("tungstate", new Solid(Color.ORANGE, 6.1, -1645, 126)),
		//CeCO3F("bastnasite", new Solid(new Color(177, 81, 39), 4.4, )),
		CoO(new Formula(Co, O), "cobalt", new Solid(Color.black, 6.44, -237, 52.9, new Hazard(2, 0, 0)), new Fusion(2206)),
		//CuFeS2("copper", new Fusion(1223, Double.NaN), new Solid(new Color(195, 163, 104), 4.2, -194, 125)),
		Fe2O3(new Formula(Fe._(2), O._(3)), "iron", new Solid(new Color(200, 42, 42), 5.2, -826, 90)),
		Fe3O4(new Formula(Fe._(3), O._(4)), "iron", new Solid(Color.BLACK, 5.17, -1118, 145), new Fusion(1870, 138)),
		FeOOH(new Formula(Fe, O, new Part(O, H)), "iron", new Solid(new Color(229, 60, 0), 4.25, -559, 60.4)),
		//FeCr2O4("chromium", new Solid(new Color(25, 10, 10), 4.7, -1438, 152)),
		FeTiO3(new Formula(Fe, Ti, O._(3)), "titanium", 
			   new Solid(new Color(25, 10, 10), 4.6, -1234, 106, new Hazard(1, 0, 0)), 
			   new Fusion(1638, 90.8)),
		//FeWO4("tungstate", new Solid(new Color(15, 10, 10), 7.5, -1154, 132)),
		//NiS("nickel", new Fusion(1070, 30.1), new Solid(Color.BLACK, 5.8, -82, 53)),
		//HgS("cinnabar", new Solid(new Color(139, 0, 0), 8.1, -58, 78, new Hazard(2, 1, 0))),
		//MgCO3("magnesite", new Solid(Color.WHITE, 3.0, -1113, 66)),
		//Mg2SiO4("olivine", new Fusion(2170, 71), new Solid(new Color(154, 205, 50), 3.2, -2175, 95)),
		//MnO2("manganese", new Solid(new Color(15, 10, 10), 5.0, -520, 53, new Hazard(1, 1, 2, SpecialCode.OXIDIZER))),
		//MoS2("molybdenum", new Solid(new Color(26, 26, 26), 5.1, -234, 62, new Hazard(1, 1, 0))),
		//Na2B4O7("borax", new Fusion(1016, 81), new Solid(Color.WHITE, 1.7, -3276, 189)),
		//NaCl("salt", new Fusion(1074, 30), new Solid(Color.WHITE, 2.2, -411, 72)),
		NiO(new Formula(Ni, O), "nickel", new Solid(Color.GREEN, 2.7, -240, 38, new Hazard(2, 0, 0)), new Fusion(2228)),
		//PbS("lead", new Fusion(1391, 17), new Solid(new Color(26, 26, 26), 7.6, -100, 91, new Hazard(2, 0, 0))),
		//Sb2S3("antimony", new Fusion(823, 40), new Solid(Color.GRAY, 4.64, -140, 182, new Hazard(2, 1, 0))),
		//SiO2("quartz", new Fusion(1900, 14), new Solid(Color.WHITE, 2.6, -911, 42)),
		//SnO2("tin", new Fusion(1903, Double.NaN), new Solid(Color.WHITE, 7.0, -581, 52, new Hazard(2, 0, 0))),
		TiO2(new Formula(Ti, O._(2)), "titanium", 
		     new Solid(4.23, -945, 50, new Hazard(1, 0, 0)), new Fusion(2116, 58),
		     new Vaporization(3245, 673))
		//ZnS("zinc", new Solid(Color.WHITE, 4.1, -206, 58))
		;
		
		private Chemical delegate;
		
		private Molecules(Formula formula, String oreDictKey, ChemicalPhaseProperties solid, Fusion fusion, 
				ChemicalPhaseProperties liquid,	Vaporization vaporization, ChemicalPhaseProperties gas) 
		{
			this.delegate = new SimpleChemical(formula, oreDictKey, solid, fusion, liquid, vaporization, gas);
		}
		
		private Molecules(Formula formula, String oreDictKey, ChemicalPhaseProperties solid, Fusion fusion, 
				ChemicalPhaseProperties liquid,	Vaporization vaporization) 
		{
			this(formula, oreDictKey, solid, fusion, liquid, vaporization, new Gas());
		}
		
		private Molecules(Formula formula, String oreDictKey, ChemicalPhaseProperties solid, Fusion fusion, 
				Vaporization vaporization) 
		{
			this(formula, oreDictKey, solid, fusion, new Liquid(), vaporization, new Gas());
		}
		
		private Molecules(Formula formula, String oreDictKey, ChemicalPhaseProperties solid, Fusion fusion, 
				ChemicalPhaseProperties liquid) 
		{
			this(formula, oreDictKey, solid, fusion, liquid, null);
		}
		
		private Molecules(Formula formula, String oreDictKey, ChemicalPhaseProperties solid, Fusion fusion) {
			this(formula, oreDictKey, solid, fusion, new Liquid());
		}
		
		private Molecules(Formula formula, String oreDictKey, ChemicalPhaseProperties solid) {
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
