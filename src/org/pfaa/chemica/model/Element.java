package org.pfaa.chemica.model;

import java.awt.Color;

import org.pfaa.chemica.model.ChemicalPhaseProperties.Gas;
import org.pfaa.chemica.model.ChemicalPhaseProperties.Liquid;
import org.pfaa.chemica.model.ChemicalPhaseProperties.Solid;
import org.pfaa.chemica.model.Formula.PartFactory;
import org.pfaa.chemica.model.Thermo.Shomate;
import org.pfaa.chemica.model.Vaporization.AntoineCoefficients;

public interface Element extends Chemical, PartFactory {
	public double getAtomicWeight();
	
	public static enum Elements implements Element {
		/*
			H                                                  He 
			Li Be                               B  C  N  O  F  Ne 
			Na Mg                               Al Si P  S  Cl Ar 
			K  Ca Sc Ti V  Cr Mn Fe Co Ni Cu Zn Ga Ge As Se Br Kr
			R  Sr Y  Zr Nb Mo Tc Ru Rh Pd Ag Cd In Sn Sb Te I  Xe
			Cs Ba Lu Hf Ta W  Re Os Ir Pt Au Hg Tl Pb Bi Po At Rn
		*/
		
		/*
		 * http://chemistrytable.webs.com/enthalpyentropyandgibbs.htm
		 * http://www.periodictable.com/Properties/A/NFPALabel.html
		 */
		
		H("hydrogen", 1.00, +1), /* See Molecules.H2 for properties */
		C("carbon", 12.0, +4, 
		  new Solid(Color.black, 2.27, 
				    new Thermo(0, 6.0, 9.25).addSegment(300, 10.7), 
				    new Hazard(0, 1, 0)), 
		  null,
		  null, 
		  new Vaporization(3915),
		  new Gas(new Thermo(717, 158, new Shomate(21.2, -0.812, 0.449, -0.0433, -0.0131, 710, 184)))),
		Ca("calcium", 40.1, +2, 
		   new Solid(new Color(228, 237, 237), 1.55, 
				     new Thermo(0, 41.6, new Shomate(19.8, 10.1, 14.5, -5.53, 0.178, -5.86, 62.9)), 
				     new Hazard(3, 1, 2)), 
		   new Fusion(1115), 
		   new Liquid(new Color(228, 237, 237), 1.38, new Thermo(7.79, 45.5, 35.0)), 
		   new Vaporization(new AntoineCoefficients(2.78, 3121, -595)),
		   new Gas(new Thermo(177, 154, new Shomate(122, -75, 19.2, -1.40, -64.5, 42.2, 217)))),
		Ti("titanium", 47.9, +4,
		   new Solid(new Color(230, 230, 230), 4.51, 
				     new Thermo(0, 30.7, new Shomate(23.1, 5.54, -2.06, 1.61, -0.0561, -0.433, 64.1)), 
				     new Hazard(1, 1, 2)), 
	       new Fusion(1941),
		   new Liquid(Color.yellow, 4.11, new Thermo(13.7, 39.2, -22.1)),
		   new Vaporization(3560),
		   new Gas(new Thermo(473, 180, new Shomate(9.27, 6.09, 0.577, -0.110, 6.50, 483, 204)))),
		Fe("iron", 55.8, +3, 
		   new Solid(Color.gray, 7.87, 
				     new Thermo(0, 27.8, new Shomate(24.0, 8.37, 0.000277, -8.60E-5, -5.00E-6, 0.268, 62.1)), 
				     new Hazard(1, 1, 0)), 
		   new Fusion(1811), 
		   new Liquid(Color.yellow, 6.98, new Thermo(12, 35, 46.0)), 
		   new Vaporization(3134),
		   new Gas(new Thermo(415, 180, new Shomate(11.3, 6.99, -1.11, 0.122, 5.69, 424, 206)))),
		Co("cobalt", 58.9, +2,
		   new Solid(Color.gray, 8.90, 
				     new Thermo(0, 30.1, new Shomate(11.0, 54.4, -55.5, 25.8, 0.165, -4.70, 30.3))
		             .addSegment(700, new Shomate(-205, 516, -422, 130, 18.0, 94.6, -273))
		             .addSegment(1394, new Shomate(-12418, 15326, -7087, 1167, 3320, 10139, -10473))), 
		   new Fusion(1768),
		   new Liquid(Color.yellow, 7.75, 
				      new Thermo(18.0, 41.0, new Shomate(45.6, -3.81, 1.03, -0.0967, -3.33, -8.14, 78.0))), 
		   new Vaporization(3200),
		   new Gas(new Thermo(427, 180, new Shomate(40.7, -8.46, 1.54, -0.0652, -11.1, 397, 213)))),
		Ni("nickel", 58.7, +2,
		   new Solid(new Color(160, 160, 140), 8.91, 
				     new Thermo(0, 30.1, new Shomate(13.7, 82.5, -175, 162, -0.0924, -6.83, 27.7))
		             .addSegment(600, new Shomate(1248, -1258, 0, 0, -165, -789, 1213))
		             .addSegment(700, new Shomate(16.5, 18.7, -6.64, 1.72, 1.87, -0.468, 51.7)), 
				     new Hazard(2, 4, 1)), 
		   new Fusion(1728),
		   new Liquid(Color.yellow, 7.81, new Thermo(17.5, 41.5, 38.9)), 
		   new Vaporization(3186),
		   new Gas(new Thermo(430, 182, new Shomate(27.1, -2.59, 0.295, 0.0152, 0.0418, 421, 214)))),
		O("oxygen", 16.0, -2) /* See Molecules.O2 for properties */
		;
		
		private Chemical delegate;
		private double atomicWeight;
		private int defaultOxidationState;
	
		private Elements(String oreDictKey, double atomicWeight, int defaultOxidationState, 
				         ChemicalPhaseProperties solid, Fusion fusion, 
						 ChemicalPhaseProperties liquid, Vaporization vaporization, 
						 ChemicalPhaseProperties gas) 
		{
			Formula formula = new Formula(this._(1));
			this.delegate = new SimpleChemical(formula, oreDictKey, solid, fusion, liquid, 
					                           vaporization, gas);
			this.atomicWeight = atomicWeight;
			this.defaultOxidationState = defaultOxidationState;
		}
	
		private Elements(String oreDictKey, double atomicWeight, int defaultOxidationState, 
				         ChemicalPhaseProperties solid, Fusion fusion, 
					     ChemicalPhaseProperties liquid, Vaporization vaporization) 
		{
			this(oreDictKey, atomicWeight, defaultOxidationState, solid, fusion, liquid, 
				 vaporization, null);
		}
	
		private Elements(String oreDictKey, double atomicWeight, int defaultOxidationState,
				         ChemicalPhaseProperties solid, Fusion fusion, 
				         ChemicalPhaseProperties liquid) 
		{
			this(oreDictKey, atomicWeight, defaultOxidationState, solid, fusion, liquid, null);
		}
	
		private Elements(String oreDictKey, double atomicWeight, int defaultOxidationState,
				         ChemicalPhaseProperties solid, Fusion fusion) 
		{
			this(oreDictKey, atomicWeight, defaultOxidationState, solid, fusion, null);
		}
	
		private Elements(String oreDictKey, double atomicWeight, int defaultOxidationState, 
				         ChemicalPhaseProperties solid) {
			this(oreDictKey, atomicWeight, defaultOxidationState, solid, null);
		}
	
		private Elements(String oreDictKey, double atomicWeight, int defaultOxidationState) {
			this(oreDictKey, atomicWeight, defaultOxidationState, null);
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
		
		public Formula.Part _(int quantity) {
			return new Formula.Part(this, quantity);
		}

		@Override
		public Formula.Part getPart() {
			return this._(1);
		}

		@Override
		public double getAtomicWeight() {
			return atomicWeight;
		}
	}
}
