package org.pfaa.chemica.model;

import static org.pfaa.chemica.model.Element.Elements.Ag;
import static org.pfaa.chemica.model.Element.Elements.Al;
import static org.pfaa.chemica.model.Element.Elements.As;
import static org.pfaa.chemica.model.Element.Elements.B;
import static org.pfaa.chemica.model.Element.Elements.Ba;
import static org.pfaa.chemica.model.Element.Elements.Be;
import static org.pfaa.chemica.model.Element.Elements.Bi;
import static org.pfaa.chemica.model.Element.Elements.Br;
import static org.pfaa.chemica.model.Element.Elements.C;
import static org.pfaa.chemica.model.Element.Elements.Ca;
import static org.pfaa.chemica.model.Element.Elements.Cd;
import static org.pfaa.chemica.model.Element.Elements.Ce;
import static org.pfaa.chemica.model.Element.Elements.Cl;
import static org.pfaa.chemica.model.Element.Elements.Co;
import static org.pfaa.chemica.model.Element.Elements.Cr;
import static org.pfaa.chemica.model.Element.Elements.Cs;
import static org.pfaa.chemica.model.Element.Elements.Cu;
import static org.pfaa.chemica.model.Element.Elements.F;
import static org.pfaa.chemica.model.Element.Elements.Fe;
import static org.pfaa.chemica.model.Element.Elements.H;
import static org.pfaa.chemica.model.Element.Elements.Hg;
import static org.pfaa.chemica.model.Element.Elements.I;
import static org.pfaa.chemica.model.Element.Elements.K;
import static org.pfaa.chemica.model.Element.Elements.Li;
import static org.pfaa.chemica.model.Element.Elements.Mg;
import static org.pfaa.chemica.model.Element.Elements.Mn;
import static org.pfaa.chemica.model.Element.Elements.Mo;
import static org.pfaa.chemica.model.Element.Elements.N;
import static org.pfaa.chemica.model.Element.Elements.Na;
import static org.pfaa.chemica.model.Element.Elements.Nb;
import static org.pfaa.chemica.model.Element.Elements.Ni;
import static org.pfaa.chemica.model.Element.Elements.O;
import static org.pfaa.chemica.model.Element.Elements.P;
import static org.pfaa.chemica.model.Element.Elements.Pb;
import static org.pfaa.chemica.model.Element.Elements.S;
import static org.pfaa.chemica.model.Element.Elements.Sb;
import static org.pfaa.chemica.model.Element.Elements.Si;
import static org.pfaa.chemica.model.Element.Elements.Sn;
import static org.pfaa.chemica.model.Element.Elements.Sr;
import static org.pfaa.chemica.model.Element.Elements.Ta;
import static org.pfaa.chemica.model.Element.Elements.Ti;
import static org.pfaa.chemica.model.Element.Elements.U;
import static org.pfaa.chemica.model.Element.Elements.V;
import static org.pfaa.chemica.model.Element.Elements.W;
import static org.pfaa.chemica.model.Element.Elements.Zn;
import static org.pfaa.chemica.model.Element.Elements.Zr;

import java.awt.Color;

import org.pfaa.chemica.model.ChemicalStateProperties.Gas;
import org.pfaa.chemica.model.ChemicalStateProperties.Gas.Sutherland;
import org.pfaa.chemica.model.ChemicalStateProperties.Liquid;
import org.pfaa.chemica.model.ChemicalStateProperties.Liquid.Yaws;
import org.pfaa.chemica.model.ChemicalStateProperties.Solid;
import org.pfaa.chemica.model.Formula.Part;
import org.pfaa.chemica.model.Hazard.SpecialCode;

/* A compound is a type of chemical resulting from the combination of different 
 * atoms according to a fixed stoichiometry.
 * Purists would claim that a compound must consist of multiple elements, but
 * we broaden that definition to include polyatomic molecules like O2 and Cl2.
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
					   new Hazard(0, 0, 0), new Yaws(-10.2, 1.79E3, 1.77E-2, 1.26E-5)), 
			new Vaporization(4.65, 1435, -64.8), 
			new Gas(new Thermo(30.1, 6.83, 6.79, -2.53, 0.0821, -251, 223)
			        .addSegment(1700, 42.0, 8.62, -1.50, 0.0981, -11.2, -272, 220), 
					new Hazard(0, 0, 0), new Sutherland(13, 373, 650))),
		CO(new Formula(C, O), null, 
		   new Solid(0.929,
			    	 new Thermo(112),
			    	 new Hazard(0, 4, 0)), 
		   new Fusion(68.1),
		   new Liquid(0.789,
	    			  new Thermo(-110, 124, 46.4, 190),
					  new Hazard(0, 4, 0), new Yaws(-1.12, 57.9, -4.92E-3, 8.22E-6)),
		   new Vaporization(-269, 292, 268),
		   new Gas(new Thermo(25.6, 6.10, 4.05, -2.67, 0.131, -118, 227)
				   .addSegment(1300, 35.2, 1.3, -0.206, 0.0136, -3.28, -128, 232), 
				   new Hazard(2, 4, 0), new Sutherland(17.2, 288, 118))),
		CO2(new Formula(C, O._(2)), null, 
			new Solid(1.56, 
					  new Thermo(-427, 51.1, 0, 300), 
					  new Hazard(3, 0, 0)), 
			null, /* Does exist, but at high pressure, which we are not modeling */
			null,
			new Vaporization(6.81, 1301, -3.49), 
			new Gas(new Thermo(25.0, 55.2, -33.7, 7.95, -0.137, -404, 228)
					.addSegment(1200, 58.2, 2.72, -0.492, 0.0388, -6.45, -426, 264), 
					new Hazard(2, 0, 0), new Sutherland(14.8, 293, 240))),
		Ag2S(new Formula(Ag._(2), S), null, 
			 new Solid(Color.black, 7.23, new Thermo(-32.6, 144, 55.4, 77.9).addSegment(450, 91.2, 0, 5.86)),
			 new Fusion(1098),
			 new Liquid(new Thermo(157))),
		AlOH3(new Formula(Al, new Part(O, H)._(3)), null, 
			  new Solid(2.42, new Thermo(-1277, 71, 25.7, 155, -98.6, 0.690))),
		As2S3(new Formula(As._(2), S._(3)), null, 
		      new Solid(new Color(245, 210, 15), 3.43, new Thermo(-169, 164, 116)),
			  new Fusion(583),
			  new Liquid(new Thermo(213)),
			  new Vaporization(980)),
	    AsS(new Formula(As, S), null, 
	        new Solid(Color.red, 3.56, new Thermo()),
	        new Fusion(633),
	        new Liquid(new Thermo()),
	        new Vaporization(838)),
		BaSO4(new Formula(Ba, S, O._(4)), null, 
			  new Solid(4.5, new Thermo(-1465, 132, 141, 0.595, 0, 3.49), new Hazard(0, 0, 0)),
			  new Fusion(1618), 
			  new Liquid(new Thermo(157))),
	    Be3Al2SiO36(new Formula(Be._(3), Al._(2), new Part(Si, O._(3))._(6)), "beryl", 
				    new Solid(new Color(190, 255, 200), 2.7, new Thermo(-9000, 347, 593, 132, 17.1, -20.5)),
				    new Fusion(1700),
				    new Liquid(new Thermo(495))), // just made up the liquid entropy 
		Bi2S3(new Formula(Bi._(2), S._(3)), null, 
			  new Solid(new Color(160, 106, 0), 6.78, new Thermo(-143, 200, 155, -43.2, 36.2, 2.08), new Hazard(0, 0, 0)),
			  new Fusion(1123), 
			  new Liquid(new Thermo(274))),
		Br2(new Formula(Br._(2)), "bromine",
			new Solid(new Color(112, 112, 112), 4.05, 
					  new Thermo(56.4)), 
				   new Fusion(265), 
				   new Liquid(Color.red, 3.10,
						      new Thermo(0, 152, 76), 
						      new Hazard(3, 0, 0, SpecialCode.OXIDIZER),
						      new Yaws(-1.4, 389, 4.16E-4, -5.21E-7)), 
				   new Vaporization(332),
				   new Gas(Color.red, 
						   new Thermo(21.3, -1.78, 1.59, 0.290, -0.0117, 106, 201)
						   .addSegment(900, 20.9, 2.04, -0.619, -0.0507, -0.956, 104, 198),
						   new Hazard(3, 0, 0, SpecialCode.OXIDIZER),
						   null)),
		CaCl2(new Formula(Ca, Cl._(2)), null,
			  new Solid(2.15, new Thermo(87.3, -35.1, 44.1, -9.85, -0.674, -822, 215)),
			  new Fusion(1045),
			  new Liquid(2.09, new Thermo(103, 0, 0, 0, 0, -815, 226)),
			  new Vaporization(2208),
			  new Gas(new Thermo(62.2, 0.156, -0.0422, 0.00360, -0.260, -491, 364))),
		CaCO3(new Formula(Ca, new Part(C, O._(3))), null, 
			  new Solid(2.7, 
					    new Thermo(-1207, 93, 82.3, 49.8, 0, 1.29))),
		CaF2(new Formula(Ca, F._(2)), null, 
			 new Solid(3.18, 
			           new Thermo(89.9, -46.1, 65.3, -17.9, -1.14, -1255, 182)
			           .addSegment(1424, 108, 10.4, 0.0230, -0.00286, -0.0280, -1288, 148),
			           new Hazard(0, 0, 0)),
			 new Fusion(1691),
			 new Liquid(3.58,
					    new Thermo(-1186, 92.6, 99.9)),
			 new Vaporization(2806),
			 new Gas(new Thermo(57.4, 0.718, -0.194, 0.0166, -0.571, -804, 340))),
		CaI2O6(new Formula(Ca, new Part(I, O._(3))._(2)), null,
			   new Solid(4.51, new Thermo(230)),
			   new Fusion(813),
			   new Liquid(new Thermo()),
			   new Vaporization(1200),
			   null),
	    CaSO4_2H2O(new Formula(Ca, new Part(S, O._(4))).hydrate(2), null, 
			       new Solid(2.32, 
				 	         new Thermo(-1433, 107, 186))),
		CaWO4(new Formula(Ca, new Part(W, O._(4))), null, 
			  new Solid(4.7, 
					    new Thermo(-1645, 126, 133, 21.8, 0, 2.29)),
			  new Fusion(1893)),
		Cs2Al2Si4O12(new Formula(Cs._(2), Al._(2), new Part(Si, O._(3))._(4)).hydrate(2), null,
				     new Solid(Color.white, 2.9, new Thermo())),
	    CdS(new Formula(Cd, S), null, 
	    	new Solid(new Color(244, 208, 10), 4.83, 
	    	   	      new Thermo(-162, 65, 54.0, 3.77),
	    	 	      new Hazard(3, 0, 0)),
			null, null,
			new Vaporization(1250)),
		CeCO3F(new Formula(Ce, new Part(C, O._(3), F)), null, 
			   new Solid(new Color(177, 81, 39), 4.95, new Thermo())),
		CePO4(new Formula(Ce, new Part(P, O._(4))), null,
			   new Solid(new Color(184, 83, 43), 5.22, new Thermo())),
		CoAsS(new Formula(Co, As, S), null, 
			  new Solid(new Color(255, 80, 80), 6.33, new Thermo())),	   
	    CoO(new Formula(Co, O), null, 
			new Solid(Color.black, 6.44, 
					  new Thermo(43.7, 22.4, -16.9, 6.56, 0.532, -250, 103)
			          .addSegment(1600, 36.0, 14.1, 1.16, -0.116, 5.16, -238, 106),
					  new Hazard(2, 0, 0)),
					  new Fusion(2103)),
		Cu2CO3OH2(new Formula(Cu._(2), new Part(C, O._(3)), new Part(O, H)._(2)), null,
				  new Solid(Color.green, 3.8, new Thermo())),
		CuFeS2(new Formula(Cu, Fe, S._(2)), null, 
			   new Solid(new Color(195, 163, 104), 4.2, new Thermo(-194, 125, 100)), 
			   new Fusion(1223),
			   new Liquid(new Thermo(133))),
	    Cu2O(new Formula(Cu._(2), O), null,
	    	 new Solid(new Color(135, 0, 20), 6.1, new Thermo(-169, 93.1, 59.6, 27.4, -5.11, 0.325)), 
	         new Fusion(1505),
	         new Liquid(new Thermo(136))),
		Fe2O3(new Formula(Fe._(2), O._(3)), null, 
			  new Solid(new Color(200, 42, 42), 5.2, 
					    new Thermo(93.4, 108, 50.9, 25.6, -1.61, -863, 161)
			            .addSegment(950, 151, 0, 0, 0, 0, -876, 253)
			            .addSegment(1050, 111, 32, -9.19, 0.902, 5.43, -843, 228))),
		Fe3O4(new Formula(Fe._(3), O._(4)), null, 
			  new Solid(Color.BLACK, 5.17, 
					    new Thermo(104, 179, 10.6, 1.13, -0.994, -1163, 212)
			  			.addSegment(900, 201, 1.59, -6.66, 9.45, 3.19, -1174, 388)), 
			  new Fusion(1870),
			  new Liquid(new Thermo(164))),
		gamma_FeOH3(new Formula(Fe, new Part(O, H)._(3)), null, 
			  new Solid(new Color(120, 80, 40), 4.25, 
					    new Thermo(65.1, 182, -101, 19, -0.825, -862, 128))),
	    alpha_FeOH3(new Formula(Fe, new Part(O, H)._(3)), null, 
	    		    new Solid(new Color(230, 140, 0), 4.25, 
	    		    		  new Thermo(65.1, 182, -101, 19, -0.825, -862, 128))),
		FeCr2O4(new Formula(Fe, Cr._(2), O._(4)), null, 
				new Solid(new Color(25, 10, 10), 4.7, new Thermo(-1438, 152, 160, 31.8, -6.33, 3.06)),
				new Fusion(2500)),
		FeNb2O6(new Formula(Fe, Nb._(2), O._(6)), null, 
				new Solid(new Color(40, 20, 20), 6.0, new Thermo())),
		FeS2(new Formula(Fe, S._(2)), "iron", 
			 new Solid(new Color(204, 154, 0), 4.9, 
		   		       new Thermo(82.3, -23.7, 35.1, -9.60, -1.40, -196, 151))),
		FeTa2O6(new Formula(Fe, Ta._(2), O._(6)), null, 
				new Solid(new Color(40, 20, 20), 7.94, new Thermo())),
		FeTiO3(new Formula(Fe, Ti, O._(3)), null, 
			   new Solid(new Color(25, 10, 10), 4.6, 
					     new Thermo(-1234, 106, 110, 38.8, -10.3, 1.91),
					     new Hazard(1, 0, 0)), 
			   new Fusion(1638),
			   new Liquid(new Thermo(161))),
		FeV2O4(new Formula(Fe, V._(2), O._(4)), null, new Solid()),
		FeWO4(new Formula(Fe, new Part(W, O._(4))), null,
			  new Solid(Color.black, 6.64, 
					    new Thermo(-1155, 132, 114))),
		H2(new Formula(H._(2)), "hydrogen",
 		   new Solid(0.088,
 				     new Thermo(2.38),
 		    		 new Hazard(0, 4, 0)),
 		   new Fusion(14.0),
 		   new Liquid(0.071,
 					  new Thermo(-7.62, 16.0, 1.76, 0.847),
 					  new Hazard(0, 4, 0), new Yaws(-7.02, 40.8, 0.237, -4.08E-3)),
 		   new Vaporization(3.54, 99.4, 7.73),
 		   new Gas(new Thermo(17.6, 44.9, 92.7, -405, 0.000618, -7.26, 137)
 				   .addSegment(298, 33.1, -11.4, 11.4, -2.77, -0.159, -9.98, 173)
 			       .addSegment(1000, 18.6, 12.3, -2.86, 0.268, 1.98, -1.15, 156)
 			       .addSegment(2500, 43.4, -4.29, 1.27, -0.0969, -20.5, -38.5, 162), 
 				   new Hazard(0, 4, 0), new Sutherland(8.76, 294, 72))),
		H2S(new Formula(H._(2), S), null,
			null,
			null,
			new Liquid(0.949, new Thermo(122)),
			new Vaporization(4.53, 959, -0.539),
			new Gas(new Thermo(26.9, 18.7, 3.43, -3.38, 0.136, -28.9, 233).
					addSegment(1400, 51.2, 4.15, -0.644, 0.0416, -10.5, -55.9, 244), 
					new Hazard(4, 4, 0), new Sutherland(11.3, 273, 313))),
		HgS(new Formula(Hg, S), null, 
			new Solid(new Color(139, 0, 0), 8.1, 
					  new Thermo(-58, 78, 43.9, 15.3), 
					  new Hazard(2, 1, 0))),
		// NOTE: Iodine has a high solid vapor pressure, so it evaporates as heated, but it *does* melt
		I2(new Formula(I._(2)), "iodine",
		   new Solid(new Color(70, 65, 70), 4.93, 
				   	 new Thermo(-196, 919, -1079, 534, 5.16, 43.3, -322),
				   	 new Hazard(3, 0, 0)), 
		   new Fusion(389), 
		   new Liquid(new Color(70, 65, 90), 3.98,
				      new Thermo(80.7, 0, 0, 0, 0, -10.5, 248), 
					  new Hazard(3, 0, 0),
					  new Yaws(-0.905, 519, -1.98E-4, 4.68E-8)), 
		   new Vaporization(819),
		   new Gas(new Color(155, 100, 145), 
				   new Thermo(37.8, 0.225, -0.913, 1.03, -0.0838, 50.9, 306)
				   .addSegment(2000, 76.7, -4.05, -1.85, 0.219, -82.4, -53.9, 281),
				   new Hazard(3, 0, 0),
				   new Sutherland(2.20, 477, 590))),
		KCl(new Formula(K, Cl), null, 
			new Solid(1.98, 
					  new Thermo(35.4, 70.0, -91.4, 52.5, 0.153, -449, 109)
			          .addSegment(900, -717, 1248, -709, 141, 104, 1.76, -738)),
			new Fusion(1094),
			new Liquid(1.52, new Thermo(-422, 86.7, 73.6)),
			new Vaporization(4.78, 7441, -123),
			new Gas(new Thermo(37.4, 0.792, -0.00970, 0.000827, -0.996, -226, 283.5))),
		KNO3(new Formula(K, new Part(N, O._(3))), "saltpeter",
			 new Solid(2.11, 
					   new Thermo(-495, 133, 43.3, 226, -161, 0)),
			 new Fusion(607),
			 new Liquid(1.86, new Thermo(-451, 149, 142)),
			 new Vaporization(673),
			 null),
		K2U2V2O12(new Formula(K._(2), new Part(U, O._(2))._(2), new Part(V, O._(4))._(2)).hydrate(3), null,
				  new Solid(new Color(250, 250, 35), 4.70, new Thermo())),
		LiAlSiO32(new Formula(Li, Al, new Part(Si, O._(3))._(2)), null,
				  new Solid(new Color(190, 170, 170), 3.1, new Thermo())),
		LiCl(new Formula(Li, Cl), null, 
			 new Solid(2.07, 
					   new Thermo(43.7, 20.4, 0.31, -0.09, -0.16, -423, 105)),
			 new Fusion(880),
			 new Liquid(1.50, new Thermo(63., -9.05, -0.316, 0.0796, 0.0136, -417, 158)),
			 new Vaporization(1655),
			 new Gas(new Thermo(37.3, 0.668, -0.0222, 0.00171, -0.496, -208, 255))),
		Li3KSi4O10OH2(new Formula(Li._(3), K, new Part(Si._(4), O._(10)), new Part(O, H)._(2)), null,
				      new Solid(new Color(240, 50, 140), 2.8, new Thermo())),
	    MgCl2_6H2O(new Formula(Mg, Cl._(2)).hydrate(6), null,
	    		   new Solid(1.57, new Thermo(78.3, 2.44, 6.86, -1.73, -0.730, -668, 179)),
	    		   new Fusion(987),
	    		   new Liquid(2.11, new Thermo(-602, 130, 92.0)),
	    		   new Vaporization(1685),
	    		   new Gas(new Thermo(61.7, 0.573, -0.155, 0.0132, -0.431, -412, 349))),
		MgCO3(new Formula(Mg, new Part(C, O._(3))), null,
			  new Solid(2.96, new Thermo(44.9, 150, -74.2, 12.0, -0.629, -1133, 75.2))),
	    MgSO4_7H2O(new Formula(Mg, new Part(S, O._(4))).hydrate(7), "epsomSalt",
	     	       new Solid(1.68, new Thermo(-3389, 372, 203, 669, 0, -4.47)),
	    	       new Fusion(423)),
	    MnO2(new Formula(Mn, O._(2)), null,
			 new Solid(Color.black, 5.03, new Thermo(-520, 53, 70.2, 8.86, 0.732, 1.67))),
		MoS2(new Formula(Mo, S._(2)), null, 
			 new Solid(new Color(26, 26, 26), 5.06, 
					   new Thermo(71.7, 7.52, -0.0449, 0.00887, -0.921, -301, 142), 
					   new Hazard(1, 1, 0))),
		N2(new Formula(N._(2)), "nitrogen", 
		   new Solid(1.03, 
				     new Thermo(84.7), 
		    		 new Hazard(0, 0, 0)), 
		   new Fusion(63.2), 
		   new Liquid(0.808, 
					  new Thermo(-0.966, 79.3, 28), 
					  new Hazard(0, 0, 0), new Yaws(-15.6, 4.65E2, 1.63E-1, -6.34E-4)), 
		   new Vaporization(3.74, 265, -6.79),
		   new Gas(new Thermo(29.0, 1.85, -9.65, 16.6, 0.000117, -8.67, 226)
			       .addSegment(500, 19.5, 19.9, -8.60, 1.37, 0.528, -4.94, 212)
			       .addSegment(2000, 35.5, 1.13, -0.196, 0.0147, -4.55, -19.0, 225), 
				   new Hazard(0, 0, 0), new Sutherland(17.8, 301, 111))),
		Na2B4O7(new Formula(Na._(2), new Part(B._(4), O._(5), new Part(O, H)._(4))).hydrate(8), "borax",
				new Solid(Color.WHITE, 1.7, new Thermo(-3276, 189, 614)),
				new Fusion(1016),
				new Liquid(new Thermo(265))),
		NaCl(new Formula(Na, Cl), "sodium",
		     new Solid(2.17, new Thermo(50.7, 6.67, -2.52, 10.2, -0.201, -427, 130)),
		     new Fusion(1074),
		     new Liquid(Double.NaN, new Thermo(-42.4, 114, -43.6, 5.90, 39.1, -306, 91.1)),
		     new Vaporization(5.07, 8388, -82.6),
		     new Gas(new Thermo(37.3, 0.792, -0.0270, 0.00231, -0.157, -193, 274))),
		//NaCl("salt", new Fusion(1074, 30), new Solid(Color.WHITE, 2.2, -411, 72)),
		NiO(new Formula(Ni, O), "nickel", 
			new Solid(Color.GREEN, 2.7, new Thermo(-240, 38, 47.3, 9.00), 
				      new Hazard(2, 0, 0)), 
		    new Fusion(2228)),
		NiOH2(new Formula(Ni, new Part(O, H)._(2)), "nickel", 
			  new Solid(Color.GREEN, 4.10, new Thermo(-538, 79, 81), 
					    new Hazard(2, 0, 0))),
	    Ni9S8(new Formula(Ni._(9), S._(8)), "nickel",
			  new Solid(new Color(165, 150, 5), 4.8, new Thermo(-847, 475, 443))),
		Ni3Si2O5OH4(new Formula(Ni._(3), new Part(Si._(2), O._(5)), new Part(O, H)._(4)), "nickel",
				    new Solid(new Color(100, 150, 100), 3.2, new Thermo())),
	    O2(new Formula(O._(2)), "oxygen",
			null,
			null,
			new Liquid(1.14, new Thermo(129)),
			new Vaporization(3.95, 340,	-4.14),
			new Gas(new Thermo(31.3, -20.2, 57.9, -36.5, -0.00737, -8.90, 247).
					addSegment(700, 30.03, 8.77, -3.99, 0.788, -0.742, -11.3, 236).
					addSegment(2000, 20.9, 10.7, -2.02, 0.146, 9.25, 5.34, 238), 
					new Hazard(3, 0, 0, SpecialCode.OXIDIZER), new Sutherland(20.1, 292, 127))),
		PbS(new Formula(Pb, S), null, 
			new Solid(new Color(26, 26, 26), 7.6, 
					  new Thermo(47.4, 7.55, 2.01, -0.700, -0.0318, -113, 146), 
					  new Hazard(2, 0, 0)),
			new Fusion(1391),
			new Liquid(new Thermo(-98.3, 101, 66.9)),
			new Vaporization(1554),
			new Gas(new Thermo(77.9, -29.8, 7.55, -0.510, -25.1, 68.6, 311))),
		Pb5V3O12Cl(new Formula(Pb._(5), new Part(V, O._(4))._(3), Cl), null,
				   new Solid(new Color(210, 0, 0), 6.90, new Thermo())),
		Sb2S3(new Formula(Sb._(2), S._(3)), null, 
			  new Solid(new Color(100, 100, 100), 4.64, 
					    new Thermo(-175, 182, 101, 55.2), 
					    new Hazard(2, 1, 0)),
			  new Fusion(823),
			  new Liquid(new Thermo(231))),
		//SiO2("quartz", new Fusion(1900, 14), new Solid(Color.WHITE, 2.6, -911, 42)),
		SnO2(new Formula(Sn, O._(2)), null,
			 new Solid(Color.WHITE, 7.0, new Thermo(-581, 52, 60), new Hazard(2, 0, 0)),
			 new Fusion(1903),
			 new Liquid()),
	    SrSO4(new Formula(Sr, new Part(S, O._(4))), null,
	    	  new Solid(Color.WHITE, 3.96, new Thermo(-1453, 117, 91.7, 55.4), new Hazard(1, 0, 0)),
	    	  new Fusion(1879),
	    	  new Liquid(new Thermo(136))),
		TiO2(new Formula(Ti, O._(2)), null, 
		     new Solid(4.23, 
		    		   new Thermo(67, 18.7, -11.6, 2.45, -1.49, -965, 117), 
		    		   new Hazard(1, 0, 0)), 
		     new Fusion(2116),
		     new Liquid(new Thermo(78.0)),
		     new Vaporization(3245),
		     new Gas(new Thermo(285))),
		UO2(new Formula(U, O._(2)), null, 
			new Solid(Color.black, 11.0, 
					  new Thermo(70, 40, -37.5, 12.7, -1.45, -1111, 144), 
					  new Hazard(1, 0, 0)), 
		    new Fusion(3140),
		    new Liquid(new Thermo(100))),
		ZnS(new Formula(Zn, S), null, 
			new Solid(4.09, 
					  new Thermo(-206, 201, 53.6, 3.97, 0, 0.814), 
					  new Hazard(1, 0, 0))),
		ZrSiO4(new Formula(Zr, Si, O._(4)), null,
			   new Solid(new Color(210, 210, 210), 4.56,
					     new Thermo(85.2, 137, -94.6, 21.7, -1.74, -2061, 140)
			                        .addSegment(1700, 151, 0, 0, 0, 0, -2082, 238),
			             new Hazard(1, 0, 1))),
		METHANE(new Formula(C, H._(4)).setSmiles("c"), null, 
				new Solid(0.494, new Thermo(104)),
				new Fusion(90),
				new Liquid(new Thermo(-74, 116, 52.9)),
				new Vaporization(3.99, 443, -0.49),
				new Gas(new Thermo(-0.703, 108, -42.5, 5.86, 0.679, -76.8, 159)
						.addSegment(1300, 85.8, 11.2, -2.11, 0.138, -26.4, -154, 224),
						new Hazard(1, 4, 0), new Sutherland(11.6, 311, 165))
				),
		ETHANE(new Formula(C._(2), H._(6)).setSmiles("cc"), null, 
			   null,
			   new Fusion(90.4),
			   new Liquid(new Thermo(127)),
			   new Vaporization(4.51, 791, -6.42),
			   new Gas(new Thermo(6.16, 173, -0.683, 9.07, 0.126, -93.1, 186),
					   new Hazard(1, 4, 0), new Sutherland(9.1, 298, 272))
				),
		PROPANE(new Formula(C._(3), H._(8)).setSmiles("ccc"), null, 
				new Solid(new Thermo(130)),
				new Fusion(85.5),
				new Liquid(new Thermo(-119, 171, 98.4)),
				new Vaporization(4.54, 1149, 24.9),
				new Gas(new Thermo(-105, 270, 12.8, 231, -0.689, -0.00266),
						new Hazard(1, 4, 0), new Sutherland(8.0, 289, 338))
				),
		N_BUTANE(new Formula(C._(4), H._(10)).setSmiles("cccc"), "butane", 
			  	 null,
			  	 new Fusion(135),
			  	 new Liquid(new Thermo(-148, 230, 132)),
			  	 new Vaporization(4.36, 1176, -2.07),
			  	 new Gas(new Thermo(-126, 310, 21.9, 294, -0.829, -0.00695),
			  			 new Hazard(1, 4, 0), new Sutherland(8.4, 289, 400))
				),
		ISO_BUTANE(new Formula(C._(4), H._(10)).setSmiles("cc(c)c"), "butane", 
				   null,
				   new Fusion(113),
				   new Liquid(new Thermo(201)),
				   new Vaporization(4.33, 1132,	0.918),
				   new Gas(new Thermo(-126, 310, 13.3, 316, -0.991, -0.0157),
						   new Hazard(1, 4, 0), new Sutherland(7.5, 296, 385))
					)
		;
		
		private Chemical delegate;
		
		private Compounds(Formula formula, String oreDictKey, Solid solid, Fusion fusion, 
				Liquid liquid,	Vaporization vaporization, Gas gas) 
		{
			this.delegate = new SimpleChemical(formula, oreDictKey, solid, fusion, liquid, vaporization, gas);
			CompoundDictionary.register(formula, this);
		}
		
		private Compounds(Formula formula, String oreDictKey, Solid solid, Fusion fusion, 
				Liquid liquid,	Vaporization vaporization) 
		{
			this(formula, oreDictKey, solid, fusion, liquid, vaporization, new Gas());
		}
		
		private Compounds(Formula formula, String oreDictKey, Solid solid, Fusion fusion, 
				Liquid liquid) 
		{
			this(formula, oreDictKey, solid, fusion, liquid, null);
		}
		
		private Compounds(Formula formula, String oreDictKey, Solid solid, Fusion fusion) {
			this(formula, oreDictKey, solid, fusion, new Liquid());
		}
		
		private Compounds(Formula formula, String oreDictKey, Solid solid) {
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
		public ChemicalConditionProperties getProperties(Condition condition) {
			return delegate.getProperties(condition);
		}

		@Override
		public Formula getFormula() {
			return delegate.getFormula();
		}

		@Override
		public Mixture mix(IndustrialMaterial material, double weight) {
			return delegate.mix(material, weight);
		}
	}
}
