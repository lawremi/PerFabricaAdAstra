package org.pfaa.chemica.model;

import java.awt.Color;

import org.pfaa.chemica.model.ChemicalPhaseProperties.Gas;
import org.pfaa.chemica.model.ChemicalPhaseProperties.Liquid;
import org.pfaa.chemica.model.ChemicalPhaseProperties.Solid;
import org.pfaa.chemica.model.Formula.PartFactory;
import org.pfaa.chemica.model.Hazard.SpecialCode;
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
			
			Ce La Pr Nd ...
			.. Th ...
		*/
		
		/*
		 * http://chemistrytable.webs.com/enthalpyentropyandgibbs.htm
		 * http://www.periodictable.com/Properties/A/NFPALabel.html
		 */
		
		H("hydrogen", 1.00, +1), /* See Molecules.H2 for properties */
		B("boron", 10.8, +3,
				   new Solid(new Color(82, 53, 7), 2.37, 
						     new Thermo(10.2, 29.2, -18.0, 4.21, -0.551, -6.04, 7.09)
				             .addSegment(1800, 25.1, 1.98, 0.338, -0.0400, -2.64, -14.4, 25.6),
						     new Hazard(3, 3, 2, SpecialCode.WATER_REACTIVE)),
				   new Fusion(2349),
				   new Liquid(2.08,
						      new Thermo(48.9, 26.5, 31.8)),
				   new Vaporization(4200),
				   new Gas(new Thermo(20.7, 0.226, -0.112, 0.0169, 0.00871, 554, 178))),
		C("carbon", 12.0, +4, 
		  new Solid(Color.black, 2.27, 
				    new Thermo(0, 6.0, 9.25).addSegment(300, 10.7), 
				    new Hazard(0, 1, 0)), 
		  null,
		  null, 
		  new Vaporization(3915),
		  new Gas(new Thermo(21.2, -0.812, 0.449, -0.0433, -0.0131, 710, 184))),
		Na("sodium", 23.0, +1,
		   new Solid(new Color(212, 216, 220), 0.968, 
				     new Thermo(72.6, -9.49, -731, 1415, -1.26, -21.8, 155),
				     new Hazard(3, 3, 2, SpecialCode.WATER_REACTIVE)),
		   new Fusion(371),
		   new Liquid(0.927,
				      new Thermo(40.3, -28.2, 20.7, -3.64, -0.0799, -8.78, 114)),
		   new Vaporization(2.46, 1874, -416),
		   new Gas(new Thermo(20.8, 0.277, -0.392, 0.120, -0.00888, 101, 179))),
		Mg("magnesium", 24.3, +2,
		   new Solid(new Color(157, 157, 157), 1.74, 
				     new Thermo(26.5, -1.53, 8.06, 0.572, -0.174, -8.50, 63.9),
				     new Hazard(0, 1, 1)),
		   new Fusion(923),
		   new Liquid(1.58,
				      new Thermo(4.79, 34.5, 34.3)),
		   new Vaporization(1363),
		   new Gas(new Thermo(20.8, 0.0356, -0.0319, 0.00911, 0.000461, 141)
		           .addSegment(2200, 47.6, -15.4, 2.88, -0.121, -27.0, 97.4, 177))),
		Al("aluminum", 31.0, +5,
		   new Solid(new Color(177, 177, 177), 2.70,
			    	 new Thermo(28.1, -5.41, 8.56, 3.43, -0.277, -9.15, 61.9),
			 	     new Hazard(0, 1, 1)),
		   new Fusion(933),
		   new Liquid(2.38, 
                      new Thermo(10.6, 39.6, 31.8)),
		   new Vaporization(5.74, 13204, -24.3),
		   new Gas(new Thermo(20.4, 0.661, -0.314, 0.0451, 0.0782, 324, 189))),
		Si("silicon", 28.1, +4, 
		   new Solid(new Color(206, 227, 231), 2.33,
				     new Thermo(22.8,  3.90, -0.0829, 0.0421, -0.354, -8.16, 43.3)),
		   new Fusion(1687),
		   new Liquid(2.57, new Thermo(48.5, 44.5, 27.2))),
		P("phosphorus", 31.0, +5,
		  new Solid(Color.white, 1.82,
				    new Thermo(16.5, 43.3, -58.7, 25.6, -0.0867, -6.66, 50.0),
				    new Hazard(4, 4, 2)),
		  new Fusion(317.2),
		  new Liquid(1.74, 
				     new Thermo(26.3, 1.04, -6.12, 1.09, 3.00, -7.23, 74.9)),
		  new Vaporization(553),
		  new Gas(new Thermo(20.4, 1.05, -1.10, 0.378, 0.011, 310, 188)
		          .addSegment(2200, -2.11, 9.31, -0.558, -0.020, 29.3, 354, 190))
		  ),
		S("sulfur", 32.1, +4, 
		  new Solid(new Color(230, 230, 25), 2,
				    new Thermo(21.2, 3.87, 22.3, -10.3, -0.0123, -7.09, 55.5),
				    new Hazard()),
		  new Fusion(388),
		  new Liquid(1.82, 
				     new Thermo(4541, 26066, -55521, 42012, 54.6, 788, -10826)
				  	 .addSegment(432, -37.9, 133, -95.3, 24.0, 7.65, 29.8, -13.2)),
		  new Vaporization(718),
		  new Gas(new Thermo(27.5, -13.3, 10.1, -2.66, -0.00558, 269, 204)
		  		  .addSegment(1400, 16.6, 2.40, -0.256, 0.00582, 3.56, 278, 195))
		),
		Cl("chlorine", 34.5, -1), /* See Compounds.Cl2 for properties */
		K("potassium", 39.1, +1, 
		   new Solid(new Color(219, 219, 219), 0.862, 
				     new Thermo(-63.5, -3226, 14644, -16229, 16.3, 120, 534), 
				     new Hazard(3, 3, 2, SpecialCode.WATER_REACTIVE)), 
		   new Fusion(337), 
		   new Liquid(0.828, new Thermo(40.3, -30.5, 26.5, -5.73, -0.0635, -8.81, 128)), 
		   new Vaporization(new AntoineCoefficients(4.46, 4692, 24.2)),
		   new Gas(new Thermo(20.7, 0.392, -0.417, 0.146, 0.00376, 82.8, 185)
		           .addSegment(1800, 58.7, -27.4, 6.73, -0.421, -25.9, 32.4, 198))),
		Ca("calcium", 40.1, +2, 
		   new Solid(new Color(228, 237, 237), 1.55, 
				     new Thermo(19.8, 10.1, 14.5, -5.53, 0.178, -5.86, 62.9), 
				     new Hazard(3, 1, 2, SpecialCode.WATER_REACTIVE)), 
		   new Fusion(1115), 
		   new Liquid(1.38, new Thermo(7.79, 45.5, 35.0)), 
		   new Vaporization(new AntoineCoefficients(2.78, 3121, -595)),
		   new Gas(new Thermo(122, -75, 19.2, -1.40, -64.5, 42.2, 217))),
		Ti("titanium", 47.9, +4,
		   new Solid(new Color(230, 230, 230), 4.51, 
				     new Thermo(23.1, 5.54, -2.06, 1.61, -0.0561, -0.433, 64.1), 
				     new Hazard(1, 1, 2)), 
	       new Fusion(1941),
		   new Liquid(4.11, new Thermo(13.7, 39.2, -22.1)),
		   new Vaporization(3560),
		   new Gas(new Thermo(9.27, 6.09, 0.577, -0.110, 6.50, 483, 204))),
		Fe("iron", 55.8, +3, 
		   new Solid(Color.gray, 7.87, 
				     new Thermo(24.0, 8.37, 0.000277, -8.60E-5, -5.00E-6, 0.268, 62.1), 
				     new Hazard(1, 1, 0)), 
		   new Fusion(1811), 
		   new Liquid(6.98, new Thermo(12, 35, 46.0)), 
		   new Vaporization(3134),
		   new Gas(new Thermo(11.3, 6.99, -1.11, 0.122, 5.69, 424, 206))),
		Co("cobalt", 58.9, +2,
		   new Solid(Color.gray, 8.90, 
				     new Thermo(11.0, 54.4, -55.5, 25.8, 0.165, -4.70, 30.3)
		             .addSegment(700, -205, 516, -422, 130, 18.0, 94.6, -273)
		             .addSegment(1394, -12418, 15326, -7087, 1167, 3320, 10139, -10473)), 
		   new Fusion(1768),
		   new Liquid(7.75, 
				      new Thermo(45.6, -3.81, 1.03, -0.0967, -3.33, -8.14, 78.0)), 
		   new Vaporization(3200),
		   new Gas(new Thermo(40.7, -8.46, 1.54, -0.0652, -11.1, 397, 213))),
		Ni("nickel", 58.7, +2,
		   new Solid(new Color(160, 160, 140), 8.91, 
				     new Thermo(13.7, 82.5, -175, 162, -0.0924, -6.83, 27.7)
		             .addSegment(600, 1248, -1258, 0, 0, -165, -789, 1213)
		             .addSegment(700, 16.5, 18.7, -6.64, 1.72, 1.87, -0.468, 51.7), 
				     new Hazard(2, 4, 1)), 
		   new Fusion(1728),
		   new Liquid(7.81, new Thermo(17.5, 41.5, 38.9)), 
		   new Vaporization(3186),
		   new Gas(new Thermo(27.1, -2.59, 0.295, 0.0152, 0.0418, 421, 214))),
		Cu("copper", 63.5, +2,
		   new Solid(new Color(208, 147, 29), 8.96, 
				     new Thermo(17.7, 28.1, -31.3, 14.0, 0.0686, -6.06, 47.9), 
		             new Hazard(1, 1, 0)), 
		   new Fusion(1358),
		   new Liquid(8.02, new Thermo(17.5, 41.5, 32.8)), 
		   new Vaporization(2835),
		   new Gas(new Thermo(-80.5, 49.4, -7.58, 0.0405, 133, 520, 194))),
		Zn("zinc", 63.5, +2,
		   new Solid(new Color(230, 230, 230), 7.14, 
				     new Thermo(25.6, -4.41, 20.4, -7.40, -0.0458, -7.56, 72.9), 
				     new Hazard(2, 0, 0)), 
		   new Fusion(673),
		   new Liquid(6.57, new Thermo(6.52, 50.8, 31.4)), 
		   new Vaporization(1180),
		   new Gas(new Thermo(18.2, 2.31, -0.737, 0.0800, 1.07, 127, 185))),
		Ga("gallium", 69.7, +3,
		   new Solid(new Color(212, 216, 220), 5.91, 
				     new Thermo(102, -348, 603, -361, -1.49, -24.7, 236), 
		             new Hazard(1, 0, 0)), 
		   new Fusion(303),
		   new Liquid(6.10, new Thermo(24.6, 2.70, -1.27, 0.197, 0.286, -0.909, 89.9)), 
		   new Vaporization(2673),
		   new Gas(new Thermo(20.3, 0.570, -0.210, 0.0258, 3.05, 273, 201))),
		As("arsenic", 74.9, +3,
				   new Solid(new Color(112, 112, 112), 5.73, 
						     new Thermo(0, 35.1, 21.6, 9.79), 
				             new Hazard(3, 2, 0)), 
				   null, null, 
				   new Vaporization(887),
				   new Gas(new Thermo(74.3))),
		Cr("chromium", 52.0, +3, 
		   new Solid(new Color(212, 216, 220), 7.19,
				     new Thermo(7.49, 71.5, -91.7, 46.0, 0.138, -4.23, 15.8)
		             .addSegment(600, 18.5, 5.48, 7.90, -1.15, 1.27, -2.68, 48.1),
		             new Hazard(2, 1, 1)),
		   new Fusion(2180),
		   new Liquid(6.3, new Thermo(21.6, 36.2, 39.3)),
		   new Vaporization(2944),
		   new Gas(new Thermo(13.7, -3.42, 0.394, -16.7, 383, 183, 397))),
		Mn("manganese", 54.9, +4,
		   new Solid(new Color(212, 216, 220), 7.21,
				     new Thermo(27.2, 5.24, 7.78, -2.12, -0.282, -9.37, 61.5)
		   			 .addSegment(980, 52.3, -28.7, 21.5, -4.98, -2.43, -21.2, 90.7)
		   			 .addSegment(1361, 19.1, 31.4, -15.0, 3.21, 1.87, -2.76, 48.8)
		   			 .addSegment(1412, -534, 679, -296, 46.4, 161, 469, -394)),
		   new Fusion(1519),
		   new Liquid(5.95, new Thermo(16.3, 43.5, 46.0)),
		   new Vaporization(2334),
		   new Gas(new Thermo(188, -97.8, 20.2, -1.27, -177, 1.55, 220))),		
		O("oxygen", 16.0, -2), /* See Compounds.O2 for properties */
		F("fluorine", 19.0, -1), /* See Compounds.F2 for properties, solid density 1.7 */
		Zr("zirconium", 91.2, +4, 
		   new Solid(new Color(212, 216, 220), 6.52,
				     new Thermo(29, -12.6, 20.7, -5.91, -0.157, -8.79, 76.0),
				     new Hazard(1, 1, 0)),
		   new Fusion(2128),
		   new Liquid(5.8, new Thermo(17.4, 47.6, 41.8)),
		   new Vaporization(4650),
		   new Gas(new Thermo(39.5, -6.52, 2.26, -0.194, -12.5, 578, 212))),
		 Mo("molybdenum", 96, +4, 
			new Solid(new Color(105, 105, 105), 10.3,
					  new Thermo(24.7, 3.96, -1.27, 1.15, -0.17, -8.11, 56.4)
			          .addSegment(1900, 1231, -963, 284, -28, -712, -1486, 574),
					  new Hazard(1, 3, 0)),
			new Fusion(2896),
			new Liquid(9.33, new Thermo(41.6, 43.1, 37.7)),
			new Vaporization(4912),
			new Gas(new Thermo(67.9, -40.5, 11.7, -0.819, -22.1, 601, 232))),
		Ag("silver", 108, +1, 
		   new Solid(new Color(213, 213, 213), 10.5,
				     new Thermo(0, 42.6, 23.4, 6.28),
				     new Hazard(1, 1, 0)),
		   new Fusion(1235),
		   new Liquid(9.32, new Thermo(24.7, 52.3, 339, -45)),
		   new Vaporization(1.95, 2505, -1195),
		   new Gas(new Thermo(285, 173, Double.NaN))),
		Cd("cadmium", 112, +2, 
		   new Solid(new Color(190, 190, 210), 8.65,
				     new Thermo(0, 51.8, 22.8, 10.3),
				     new Hazard(2, 2, 0)),
		   new Fusion(594),
		   new Liquid(8.00, new Thermo(Double.NaN, 62.2, 29.7)),
		   new Vaporization(1040),
		   new Gas(new Thermo(112, 168, 20.8))),
		Sn("tin", 119, +4, 
		   new Solid(new Color(212, 216, 220), 7.28,
				     new Thermo(0, 51.2, 23.1, 19.6),
				     new Hazard(1, 3, 3)),
		   new Fusion(505),
		   new Liquid(6.97, new Thermo(65.1)), // FIXME: need liquid heat capacity
		   new Vaporization(6.60, 16867, 15.5),
		   new Gas(new Thermo(168))),
		Sb("antimony", 122, +3, 
		   new Solid(new Color(212, 216, 220), 6.70,
				     new Thermo(0, 45.7, 23.1, 7.45), // NOTE: heating is extremely dangerous
				     new Hazard(4, 4, 2)),
		   new Fusion(904),
		   new Liquid(6.53, new Thermo(67.6)),
		   new Vaporization(2.26, 4475, -152),
		   new Gas(new Thermo(168))),
		Ba("barium", 137, +2, 
		   new Solid(new Color(70, 70, 70), 3.51,
				     new Thermo(83.8, -406, 915, -520, -14.6, 248)
		             .addSegment(583, 76.7, -188, 296, -114, -4.34, -25.6, 189)
		             .addSegment(768, 26.2, 28.6, -23.7, 6.95, 1.04, -5.80, 90),
				     new Hazard(2, 1, 2)),
		   new Fusion(1000),
		   new Liquid(3.34, new Thermo(55.0, -18.7, 2.76, 1.28, 3.02, -8.42, 135)),
		   new Vaporization(4.08, 7599, -45.7),
		   new Gas(new Thermo(-623, 430, -97.0, 7.47, 488, 1077, 19.0)
		           .addSegment(4000, 770, -284, 41.4, -2.13, -1693, -1666, -26.3))),
	    Re("rhenium", 186, +7, 
		   new Solid(new Color(212, 216, 220), 21.0,
				     new Thermo(0, 36.9, 26.4, 2.22)),
		   new Fusion(3459),
		   new Liquid(18.9, new Thermo(54.4))),
		Hg("mercury", 201, +2, 
		   new Solid(new Color(155, 155, 155), 14.25,
				     new Thermo(-2.18, 66.1, 21.5, 29.2)),
		   new Fusion(234),
		   new Liquid(13.5, 
				      new Thermo(0, 75.9, 28),
				      new Hazard(3, 0, 0)),
		   new Vaporization(4.86, 3007, -10.0),
		   new Gas(new Thermo(20.7, 0.179, -0.0801, 0.0105, 0.00701, 55.2, 200))),
		Pb("lead", 207, +2, 
		   new Solid(new Color(65, 65, 65), 11.3,
				     new Thermo(25.0, 5.44, 4.06, -1.24, -0.0107, -7.77, 93.2),
				     new Hazard(2, 0, 0)),
		   new Fusion(601),
		   new Liquid(10.7, new Thermo(38.0, -14.6, 7.26, -1.03, -0.331, -7.94, 119)),
		   new Vaporization(2022),
		   new Gas(new Thermo(-85.7, 69.3, -13.3, 0.840, 63.1, 333, 170))),
		Bi("bismuth", 209, +3, 
		   new Solid(new Color(213, 213, 213), 9.78,
				     new Thermo(0, 56.7, 21.1, 14.7)),
		   new Fusion(545),
		   new Liquid(10.1, new Thermo(0 /* dummy */, 77.4, 31.8)),
		   new Vaporization(1837),
		   new Gas(new Thermo(175))),
		Ce("cerium", 140, +3,
		   new Solid(new Color(212, 216, 220), 6.77, 
				     new Thermo(0, 72.0, 24.6, 0.005),
				     new Hazard(2, 3, 2)),
		   new Fusion(1068),
		   new Liquid(6.55, new Thermo(77.1)),
		   new Vaporization(3716),
		   new Gas(new Thermo(184))),
		La("lanthanum", 139, +3,
		   new Solid(new Color(212, 216, 220), 6.16, 
				     new Thermo(0, 56.9, 24.7, 4),
				     new Hazard(2, 3, 2)),
		   new Fusion(1193),
		   new Liquid(5.94, new Thermo(62.1)),
		   new Vaporization(3737),
		   new Gas(new Thermo(169))),
		Nd("neodynium", 144, +3,
		   new Solid(new Color(212, 216, 220), 7.01, 
				     new Thermo(0, 71.6, 26.2, 4),
				     new Hazard(2, 3, 2)),
		   new Fusion(1297),
		   new Liquid(6.89, new Thermo(77.1)),
		   new Vaporization(3347),
		   new Gas(new Thermo(163))),
		Pr("praseodynium", 141, +3,
		   new Solid(new Color(212, 216, 220), 6.77, 
				     new Thermo(0, 73.2, 26.0, 4),
				     new Hazard(2, 3, 2)),
		   new Fusion(1208),
		   new Liquid(6.50, new Thermo(78.9)),
		   new Vaporization(3403),
		   new Gas(new Thermo(176))),
		Th("thorium", 232, +4,
		   new Solid(new Color(64, 69, 70), 11.7,
				     new Thermo(0, 51.8, 24.3, 10.2)),
		   new Fusion(2115),
		   new Liquid(Double.NaN, new Thermo(58.3)),
		   new Vaporization(5061),
		   new Gas(new Thermo(160)))
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
