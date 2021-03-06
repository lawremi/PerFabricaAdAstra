package org.pfaa.chemica.model;

import static org.pfaa.chemica.model.Ion.Ions.Ag;
import static org.pfaa.chemica.model.Ion.Ions.Al;
import static org.pfaa.chemica.model.Ion.Ions.As;
import static org.pfaa.chemica.model.Ion.Ions.B4O5OH2;
import static org.pfaa.chemica.model.Ion.Ions.Ba;
import static org.pfaa.chemica.model.Ion.Ions.Be;
import static org.pfaa.chemica.model.Ion.Ions.Bi;
import static org.pfaa.chemica.model.Ion.Ions.Br;
import static org.pfaa.chemica.model.Ion.Ions.CO3;
import static org.pfaa.chemica.model.Ion.Ions.Ca;
import static org.pfaa.chemica.model.Ion.Ions.Cd;
import static org.pfaa.chemica.model.Ion.Ions.Ce;
import static org.pfaa.chemica.model.Ion.Ions.Cl;
import static org.pfaa.chemica.model.Ion.Ions.Co;
import static org.pfaa.chemica.model.Ion.Ions.Cr;
import static org.pfaa.chemica.model.Ion.Ions.Cr2O7;
import static org.pfaa.chemica.model.Ion.Ions.CrO4;
import static org.pfaa.chemica.model.Ion.Ions.Cs;
import static org.pfaa.chemica.model.Ion.Ions.Cu;
import static org.pfaa.chemica.model.Ion.Ions.F;
import static org.pfaa.chemica.model.Ion.Ions.Fe_2;
import static org.pfaa.chemica.model.Ion.Ions.Fe_3;
import static org.pfaa.chemica.model.Ion.Ions.H;
import static org.pfaa.chemica.model.Ion.Ions.HCO3;
import static org.pfaa.chemica.model.Ion.Ions.Hg;
import static org.pfaa.chemica.model.Ion.Ions.IO3;
import static org.pfaa.chemica.model.Ion.Ions.K;
import static org.pfaa.chemica.model.Ion.Ions.Li;
import static org.pfaa.chemica.model.Ion.Ions.Mg;
import static org.pfaa.chemica.model.Ion.Ions.Mn;
import static org.pfaa.chemica.model.Ion.Ions.Mo;
import static org.pfaa.chemica.model.Ion.Ions.NH4;
import static org.pfaa.chemica.model.Ion.Ions.NO3;
import static org.pfaa.chemica.model.Ion.Ions.Na;
import static org.pfaa.chemica.model.Ion.Ions.NbO3;
import static org.pfaa.chemica.model.Ion.Ions.Ni;
import static org.pfaa.chemica.model.Ion.Ions.O;
import static org.pfaa.chemica.model.Ion.Ions.OH;
import static org.pfaa.chemica.model.Ion.Ions.PO4;
import static org.pfaa.chemica.model.Ion.Ions.Pb;
import static org.pfaa.chemica.model.Ion.Ions.S;
import static org.pfaa.chemica.model.Ion.Ions.S2;
import static org.pfaa.chemica.model.Ion.Ions.SO4;
import static org.pfaa.chemica.model.Ion.Ions.Sb;
import static org.pfaa.chemica.model.Ion.Ions.Si2O5;
import static org.pfaa.chemica.model.Ion.Ions.SiO3;
import static org.pfaa.chemica.model.Ion.Ions.SiO4;
import static org.pfaa.chemica.model.Ion.Ions.Sn;
import static org.pfaa.chemica.model.Ion.Ions.Sr;
import static org.pfaa.chemica.model.Ion.Ions.Ta2O6;
import static org.pfaa.chemica.model.Ion.Ions.Ti;
import static org.pfaa.chemica.model.Ion.Ions.U;
import static org.pfaa.chemica.model.Ion.Ions.V;
import static org.pfaa.chemica.model.Ion.Ions.VO4;
import static org.pfaa.chemica.model.Ion.Ions.WO4;
import static org.pfaa.chemica.model.Ion.Ions.Zn;
import static org.pfaa.chemica.model.Ion.Ions.Zr;

import java.awt.Color;

import org.pfaa.chemica.model.ChemicalStateProperties.Gas;
import org.pfaa.chemica.model.ChemicalStateProperties.Gas.Sutherland;
import org.pfaa.chemica.model.ChemicalStateProperties.Liquid;
import org.pfaa.chemica.model.ChemicalStateProperties.Liquid.Yaws;
import org.pfaa.chemica.model.ChemicalStateProperties.Solid;
import org.pfaa.chemica.model.Hazard.SpecialCode;
import org.pfaa.chemica.model.Ion.Ions;

/* A compound is a type of chemical resulting from the combination of different 
 * atoms according to a fixed stoichiometry.
 * Purists would claim that a compound must consist of multiple elements, but
 * we broaden that definition to include polyatomic molecules like O2 and Cl2.
 */
public interface Compound extends Chemical {
	public static enum Compounds implements Compound {
		H2O(new Formula(Element.H, Element.O.__(2)), "water", 
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
		CO(new Formula(Element.C, Element.O), null, 
		   new Solid(0.929,
			    	 new Thermo(112),
			    	 new Hazard(0, 4, 0)), 
		   new Fusion(68),
		   new Liquid(0.789,
	    			  new Thermo(-110, 124, 46.4, 190),
					  new Hazard(0, 4, 0), new Yaws(-1.12, 57.9, -4.92E-3, 8.22E-6)),
		   new Vaporization(-269, 292, 268),
		   new Gas(new Thermo(25.6, 6.10, 4.05, -2.67, 0.131, -118, 227)
				   .addSegment(1300, 35.2, 1.3, -0.206, 0.0136, -3.28, -128, 232), 
				   new Hazard(2, 4, 0), new Sutherland(17.2, 288, 118))),
		CO2(new Formula(Element.C, Element.O.__(2)), null, 
			new Solid(1.56, 
					  new Thermo(-427, 51.1, 0, 300), 
					  new Hazard(3, 0, 0)), 
			null, /* Does exist, but at high pressure, which we are not modeling */
			null,
			new Vaporization(6.81, 1301, -3.49), 
			new Gas(new Thermo(25.0, 55.2, -33.7, 7.95, -0.137, -404, 228)
					.addSegment(1200, 58.2, 2.72, -0.492, 0.0388, -6.45, -426, 264), 
					new Hazard(2, 0, 0), new Sutherland(14.8, 293, 240))),
		Ag2S(new Formula(Ag.__(2), S), null, 
			 new Solid(new Color(75, 75, 75), 7.23, 
					   new Thermo(-32.6, 144, 55.4, 77.9).addSegment(450, 91.2, 0, 5.86)),
			 new Fusion(1098),
			 new Liquid(new Thermo(157))),
		Al2O3(new Formula(Al.__(2), O.__(3)), "alumina", // assumes alpha phase (corundum)
			  new Solid(4.0, 
					    new Thermo(102, 38.7, -15.9, 2.63, -3.01, -1718, 147)),
			  new Fusion(2345),
			  new Liquid(new Thermo(192, 0, 0, 0, 0, -1758, 177)),
			  new Vaporization(3250)),
		AlOH3(new Formula(Al, OH.__(3)), null, 
			  new Solid(2.42, new Thermo(-1277, 71, 25.7, 155, -98.6, 0.690))),
		As2S3(new Formula(As.__(2), S.__(3)), null, 
		      new Solid(new Color(245, 210, 15), 3.43, new Thermo(-169, 164, 116)),
			  new Fusion(583),
			  new Liquid(new Thermo(213)),
			  new Vaporization(980)),
	    AsS(new Formula(As, S), null, 
	        new Solid(Color.red, 3.56, new Thermo()),
	        new Fusion(633),
	        new Liquid(new Thermo()),
	        new Vaporization(838)),
		BaSO4(new Formula(Ba, SO4), null, 
			  new Solid(4.5, new Thermo(-1465, 132, 141, 0.595, 0, 3.49), new Hazard(0, 0, 0)),
			  new Fusion(1618), 
			  new Liquid(new Thermo(157))),
	    Be3Al2SiO36(new Formula(Be.__(3), Al.__(2), SiO3.__(6)), null, 
				    new Solid(new Color(190, 255, 200), 2.7, new Thermo(-9000, 347, 593, 132, 17.1, -20.5)),
				    new Fusion(1700),
				    new Liquid(new Thermo(495))), // just made up the liquid entropy 
		Bi2S3(new Formula(Bi.__(2), S.__(3)), null, 
			  new Solid(new Color(160, 106, 0), 6.78, new Thermo(-143, 200, 155, -43.2, 36.2, 2.08), new Hazard(0, 0, 0)),
			  new Fusion(1123), 
			  new Liquid(new Thermo(274))),
		Br2(new Formula(Element.Br.__(2)), "bromine",
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
		CaCl2(new Formula(Ca, Cl.__(2)), null,
			  new Solid(2.15, new Thermo(87.3, -35.1, 44.1, -9.85, -0.674, -822, 215)),
			  new Fusion(1045),
			  new Liquid(2.09, new Thermo(103, 0, 0, 0, 0, -815, 226)),
			  new Vaporization(2208),
			  new Gas(new Thermo(62.2, 0.156, -0.0422, 0.00360, -0.260, -491, 364))),
		CaCO3(new Formula(Ca, CO3), null, 
			  new Solid(2.7, 
					    new Thermo(-1207, 93, 82.3, 49.8, 0, 1.29))),
		CaF2(new Formula(Ca, F.__(2)), null, 
			 new Solid(3.18, 
			           new Thermo(89.9, -46.1, 65.3, -17.9, -1.14, -1255, 182)
			           .addSegment(1424, 108, 10.4, 0.0230, -0.00286, -0.0280, -1288, 148),
			           new Hazard(0, 0, 0)),
			 new Fusion(1691),
			 new Liquid(3.58,
					    new Thermo(-1186, 92.6, 99.9)),
			 new Vaporization(2806),
			 new Gas(new Thermo(57.4, 0.718, -0.194, 0.0166, -0.571, -804, 340))),
		CaI2O6(new Formula(Ca, IO3.__(2)), null,
			   new Solid(4.51, new Thermo(230)),
			   new Fusion(813),
			   new Liquid(new Thermo()),
			   new Vaporization(1200),
			   null),
		CaOH2(new Formula(Ca, OH.__(2)), "slakedLime",
			  new Solid(2.21, 
					    new Thermo(131, -82.7, 123, -50.4, -2.51, -1031, 247),
					    new Hazard(3, 0, 0))),
		CaO(new Formula(Ca, O), "quicklime",
			new Solid(new Color(250, 240, 175), 3.34, 
					  new Thermo(50, 4.89, -0.352, 0.0462, -0.825, -653, 92.6),
					  new Hazard(3, 0, 2)),
			new Fusion(2886),
			new Liquid(new Thermo(62.8, 0, 0, 0, 0, -592, 117)),
			new Vaporization(4120),
			new Gas(new Thermo(358, -118, 16.7, -0.832, -615, -714, 172))),
	    CaSO4_2H2O(new Formula(Ca, SO4).hydrate(2), null, 
			       new Solid(2.32, 
				 	         new Thermo(-1433, 107, 186))),
		CaWO4(new Formula(Ca, WO4), null, 
			  new Solid(4.7, 
					    new Thermo(-1645, 126, 133, 21.8, 0, 2.29)),
			  new Fusion(1893)),
		Cs2Al2Si4O12(new Formula(Cs.__(2), Al.__(2), SiO3.__(4)).hydrate(2), null,
				     new Solid(Color.white, 2.9, new Thermo())),
	    CdS(new Formula(Cd, S), null, 
	    	new Solid(new Color(244, 208, 10), 4.83, 
	    	   	      new Thermo(-162, 65, 54.0, 3.77),
	    	 	      new Hazard(3, 0, 0)),
			null, null,
			new Vaporization(1250)),
		CeCO3F(new Formula(Ce, CO3, F), null, 
			   new Solid(new Color(177, 81, 39), 4.95, new Thermo())),
		CePO4(new Formula(Ce, PO4), null,
			   new Solid(new Color(184, 83, 43), 5.22, new Thermo())),
		Cl2(new Formula(Element.Cl.__(2)), "chlorine",
		    new Solid(new Color(255, 220, 120), 1.72, // density guessed from F2
		    		  new Thermo(101)),
		    new Fusion(172),
		    new Liquid(new Color(255, 220, 120), 1.56,
		    		   new Thermo(138),
		    		   new Hazard(4, 0, 0, SpecialCode.OXIDIZER),
		    		   new Yaws(-0.768, 151, -8.07E-4, 4.08E-7)),
		    new Vaporization(3.02, 531, -64.6),
		    new Gas(new Color(230, 255, 150), 
				    new Thermo(33.1, 12.2, -12.1, 4.39, -0.159, -10.8, 259)
				    .addSegment(1000, 42.7, -5.01, 1.90, -0.166, -2.10, -17.3, 270)
				    .addSegment(3000, -42.6, 41.7, -7.13, 0.388, 101, 133, 265),
				    new Hazard(4, 0, 0, SpecialCode.OXIDIZER),
				    null)),
		CoAsS(new Formula(Co, As, S), null, 
			  new Solid(new Color(255, 80, 80), 6.33, new Thermo())),	   
	    CoO(new Formula(Co, O), null, 
			new Solid(Color.black, 6.44, 
					  new Thermo(43.7, 22.4, -16.9, 6.56, 0.532, -250, 103)
			          .addSegment(1600, 36.0, 14.1, 1.16, -0.116, 5.16, -238, 106),
					  new Hazard(2, 0, 0)),
			new Fusion(2103)),
	    Cr2O3(new Formula(Cr.__(2), O.__(3)), null, 
			  new Solid(Color.green, 5.22,
				    	new Thermo(-147396, 711495, -1.11E6, 407561, 2077, 27167, -332949)
				    	.addSegment(305, 125, -0.337, 5.71, -1.05, -2.03, -1178, 221),
				    	new Hazard(0, 0, 0)),
			  new Fusion(2708),
			  new Liquid(new Thermo(157, 0, 0, 0, 0, -1112, 253)),
			  new Vaporization(4270)),
		Cu2CO3OH2(new Formula(Cu.__(2), CO3, OH.__(2)), null,
				  new Solid(Color.green, 3.8, new Thermo(-1067, 166, 154))),
		Cu3CO32OH2(new Formula(Cu.__(3), CO3.__(2), OH.__(2)), null,
				   new Solid(new Color(20, 20, 200), 3.8, new Thermo(-1675, 254, 229))),
		Cu12As4S13(new Formula(Cu.__(12), As.__(4), S.__(13)), null,
				  new Solid(new Color(130, 136, 132), 4.65, new Thermo())),
		CuFeS2(new Formula(Cu, Fe_2, S.__(2)), null, 
			   new Solid(new Color(195, 163, 104), 4.2, new Thermo(-194, 125, 100)), 
			   new Fusion(1223),
			   new Liquid(new Thermo(133))),
		Cu2S(new Formula(Cu.__(2), S), null,
		     new Solid(new Color(80, 80, 80), 5.6, 
		    		   new Thermo(-79.5, 120, 76.3)
		               .addSegment(376, 97.3, 0, 3.85)
		               .addSegment(623, 85, 0, 0.84)), 
		     new Fusion(1400),
		     new Liquid(new Thermo(127))),
		Cu12Sb4S13(new Formula(Cu.__(12), Sb.__(4), S.__(13)), null,
				  new Solid(new Color(123, 144, 149), 4.97, new Thermo())),
		CuO(new Formula(Cu, O), null,
			new Solid(new Color(50, 35, 5), 6.32, new Thermo(48.6, 7.50, -0.0560, 0.0139, -0.760, -173, 94.9)), 
		    new Fusion(1599),
		    new Liquid(new Thermo(-73.5, 137, 235)),
		    new Vaporization(2270),
		    new Gas(new Thermo(-6.23, 1.67, -0.0923, -4.40, 284, 283, 306))),
	    Cu2O(new Formula(Cu.__(2), O), null,
	    	 new Solid(new Color(135, 0, 20), 6.1, new Thermo(-169, 93.1, 59.6, 27.4, -5.11, 0.325)), 
	         new Fusion(1505),
	         new Liquid(new Thermo(136))),
	    F2(new Formula(Element.F.__(2)), "fluorine",
		   new Solid(Color.yellow, 1.70, 
				     new Thermo(116),
				     new Hazard(4, 0, 4, SpecialCode.OXIDIZER)), 
		   new Fusion(54), 
		   new Liquid(Color.yellow, 1.51,
				      new Thermo(125), 
				      new Hazard(4, 0, 4, SpecialCode.OXIDIZER),
				      new Yaws(-1.58, 85.6, -4.07E-4, -2.73E-6)),
		   new Vaporization(4.02, 322, -4.75),
		   new Gas(new Color(255, 255, 200), 
				   new Thermo(31.4, 8.41, -2.78, 0.22, -0.21, -10.4, 237),
				   new Hazard(4, 0, 4, SpecialCode.OXIDIZER),
				   null)),
		Fe2O3(new Formula(Fe_3.__(2), O.__(3)), null, 
			  new Solid(new Color(200, 42, 42), 5.2, 
					    new Thermo(93.4, 108, 50.9, 25.6, -1.61, -863, 161)
			            .addSegment(950, 151, 0, 0, 0, 0, -876, 253)
			            .addSegment(1050, 111, 32, -9.19, 0.902, 5.43, -843, 228))),
		Fe3O4(new Formula(Fe_2, Fe_3.__(2), O.__(4)), null, 
			  new Solid(Color.BLACK, 5.17, 
					    new Thermo(104, 179, 10.6, 1.13, -0.994, -1163, 212)
			  			.addSegment(900, 201, 1.59, -6.66, 9.45, 3.19, -1174, 388)), 
			  new Fusion(1870),
			  new Liquid(new Thermo(164))),
		gamma_FeOH3(new Formula(Fe_3, OH.__(3)), null, 
			  new Solid(new Color(120, 80, 40), 4.25, 
					    new Thermo(65.1, 182, -101, 19, -0.825, -862, 128))),
	    alpha_FeOH3(new Formula(Fe_3, OH.__(3)), null, 
	    		    new Solid(new Color(230, 140, 0), 4.25, 
	    		    		  new Thermo(65.1, 182, -101, 19, -0.825, -862, 128))),
	    FeAsS(new Formula(Fe_3, Ions.AsS), null, 
			  new Solid(new Color(123, 144, 149), 6.0,
					    new Thermo(-121, 68.5, 75.5, 4.78, 0, -0.754))),
		FeCr2O4(new Formula(Fe_2, CrO4), null, 
				new Solid(new Color(25, 10, 10), 4.7, new Thermo(-1438, 152, 160, 31.8, -6.33, 3.06)),
				new Fusion(2500)),
		FeNb2O6(new Formula(Fe_2, NbO3.__(2)), null, 
				new Solid(new Color(40, 20, 20), 6.0, new Thermo())),
		FeS2(new Formula(Fe_2, S2), null, 
			 new Solid(new Color(204, 154, 0), 4.9, 
		   		       new Thermo(82.3, -23.7, 35.1, -9.60, -1.40, -196, 151))),
		FeTa2O6(new Formula(Fe_2, Ta2O6), null, 
				new Solid(new Color(40, 20, 20), 7.94, new Thermo())),
		FeTiO3(new Formula(Fe_2, Ti, O.__(3)), null, 
			   new Solid(new Color(25, 10, 10), 4.6, 
					     new Thermo(-1234, 106, 110, 38.8, -10.3, 1.91),
					     new Hazard(1, 0, 0)), 
			   new Fusion(1638),
			   new Liquid(new Thermo(161))),
		FeWO4(new Formula(Fe_2, WO4), null,
			  new Solid(Color.black, 6.64, 
					    new Thermo(-1155, 132, 114))),
		H2(new Formula(Element.H.__(2)), "hydrogen",
 		   new Solid(0.088,
 				     new Thermo(2.38),
 		    		 new Hazard(0, 4, 0)),
 		   new Fusion(14),
 		   new Liquid(0.071,
 					  new Thermo(-7.62, 16.0, 1.76, 0.847),
 					  new Hazard(0, 4, 0), new Yaws(-7.02, 40.8, 0.237, -4.08E-3)),
 		   new Vaporization(3.54, 99.4, 7.73),
 		   new Gas(new Thermo(17.6, 44.9, 92.7, -405, 0.000618, -7.26, 137)
 				   .addSegment(298, 33.1, -11.4, 11.4, -2.77, -0.159, -9.98, 173)
 			       .addSegment(1000, 18.6, 12.3, -2.86, 0.268, 1.98, -1.15, 156)
 			       .addSegment(2500, 43.4, -4.29, 1.27, -0.0969, -20.5, -38.5, 162), 
 				   new Hazard(0, 4, 0), new Sutherland(8.76, 294, 72))),
		HCl(new Formula(H, Cl), "hydrochloricAcid",
			new Solid(),
			new Fusion(159),
			new Liquid(new Thermo(101)),
			new Vaporization(4.58, 868, 1.75),
			new Gas(new Thermo(32.1, -13.5, 19.9, -6.85, -0.0497, -102, 229)
					.addSegment(1200, 31.9, 3.20, -0.542, 0.0359, -3.44, -108, 218),
					new Hazard(3, 0, 1), 
					null)),
		HNO3(new Formula(H, NO3), "nitricAcid",
			 new Solid(new Thermo()),
			 new Fusion(231),
			 new Liquid(1.51,
					    new Thermo(-174, 156, 110),
					    new Hazard(3, 0, 0, SpecialCode.OXIDIZER),
					    new Yaws(-3.52, 729, 0.00396, -2.24E-6)),
			 new Vaporization(356),
			 new Gas(new Thermo(19.6, 154, -116, 32.9, -0.249, -147, 248).
					 addSegment(1200, 97.5, 5.43, -1.03, 0.0680, -12.3, -192, 344))),
		H3PO4(new Formula(H.__(3), PO4), "phosphoricAcid",
			  new Solid(2.03, new Thermo(15.5, 304, -0.968, 0.387, 0.00765, -1302, 38.8)),
			  new Fusion(315),
			  new Liquid(1.89,
					     new Thermo(55.2, 301, -0.0952, 0.0423, 0.000512, -1302, 128),
					     new Hazard(3, 0, 0),
					     null)),
		H2S(new Formula(H.__(2), S), null,
			new Solid(1.12, new Thermo()),
			new Fusion(191),
			new Liquid(0.949, new Thermo(122)),
			new Vaporization(4.53, 959, -0.539),
			new Gas(new Thermo(26.9, 18.7, 3.43, -3.38, 0.136, -28.9, 233).
					addSegment(1400, 51.2, 4.15, -0.644, 0.0416, -10.5, -55.9, 244), 
					new Hazard(4, 4, 0), new Sutherland(11.3, 273, 313))),
		H2SO4(new Formula(H.__(2), SO4), "sulfuricAcid",
		 	  new Solid(new Thermo(119)),
		 	  new Fusion(283),
		 	  new Liquid(1.84,
		 			     new Thermo(139, 157, 81.5, 192),
		 			     new Hazard(3, 0, 2, SpecialCode.WATER_REACTIVE), 
		 			     new Yaws(-18.7, 3496, 0.0331, 8.04E-5))),
		H2S2O7(new Formula(H.__(2), S.__(2), O.__(7)), "oleum",
			   new Solid(1.94, new Thermo(), new Hazard(3, 0, 2, SpecialCode.WATER_REACTIVE)),
			   new Fusion(309)),
		HgS(new Formula(Hg, S), null, 
			new Solid(new Color(139, 0, 0), 8.1, 
					  new Thermo(-58, 78, 43.9, 15.3), 
					  new Hazard(2, 1, 0))),
		// NOTE: Iodine has a high solid vapor pressure, so it evaporates as heated, but it *does* melt
		I2(new Formula(Element.I.__(2)), "iodine",
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
		K2O(new Formula(K.__(2), O), null,
			new Solid(new Color(255, 255, 100), 2.32, 
					  new Thermo(-363, 94, 83.6),
					  new Hazard(3, 0, 2, SpecialCode.WATER_REACTIVE)),
			new Fusion(1010)),
		KNO2(new Formula(K, Ions.NO2), null,
			 new Solid(new Color(255, 255, 140), 1.91, 
					   new Thermo(-371, 152, 107),
					   new Hazard(2, 0, 2, SpecialCode.OXIDIZER))),
		KNO3(new Formula(K, NO3), "saltpeter",
			 new Solid(2.11, 
					   new Thermo(-495, 133, 43.3, 226, -161, 0),
					   new Hazard(1, 0, 0, SpecialCode.OXIDIZER)),
			 new Fusion(607),
			 new Liquid(1.86, new Thermo(-451, 149, 142))),
		K2U2V2O12_3H2O(new Formula(K.__(2), Ions.UO2.__(2), VO4.__(2)).hydrate(3), null,
				  new Solid(new Color(250, 250, 35), 4.70, new Thermo())),
		LiAlSiO32(new Formula(Li, Al, SiO3.__(2)), null,
				  new Solid(new Color(190, 170, 170), 3.1, new Thermo())),
		LiCl(new Formula(Li, Cl), null, 
			 new Solid(2.07, 
					   new Thermo(43.7, 20.4, 0.31, -0.09, -0.16, -423, 105)),
			 new Fusion(880),
			 new Liquid(1.50, new Thermo(63., -9.05, -0.316, 0.0796, 0.0136, -417, 158)),
			 new Vaporization(1655),
			 new Gas(new Thermo(37.3, 0.668, -0.0222, 0.00171, -0.496, -208, 255))),
		Li3KSi4O10OH2(new Formula(Li.__(3), K, Si2O5.__(2), OH.__(2)), null,
				      new Solid(new Color(240, 50, 140), 2.8, new Thermo())),
	    MgCl2_6H2O(new Formula(Mg, Cl.__(2)).hydrate(6), null,
	    		   new Solid(1.57, new Thermo(78.3, 2.44, 6.86, -1.73, -0.730, -668, 179)),
	    		   new Fusion(987),
	    		   new Liquid(2.11, new Thermo(-602, 130, 92.0)),
	    		   new Vaporization(1685),
	    		   new Gas(new Thermo(61.7, 0.573, -0.155, 0.0132, -0.431, -412, 349))),
		MgCO3(new Formula(Mg, CO3), null,
			  new Solid(2.96, new Thermo(44.9, 150, -74.2, 12.0, -0.629, -1133, 75.2))),
		MgO(new Formula(Mg, O), null,
			new Solid(3.58, new Thermo(47.3, 5.68, -0.873, 0.104, -1.05, -619, 76.5),
					  new Hazard(1, 0, 0)),
			new Fusion(3125),
			new Liquid(new Thermo(-532, 48.3, 66.9)),
			new Vaporization(3870),
			new Gas(new Thermo(33.2, 1.94, -0.207, 0.0209, 33.9, 86.6, 277))),
	    MgSO4_7H2O(new Formula(Mg, SO4).hydrate(7), "epsomSalt",
	     	       new Solid(1.68, new Thermo(-3389, 372, 203, 669, 0, -4.47)),
	    	       new Fusion(423)),
	    MnO2(new Formula(Mn, O.__(2)), null,
			 new Solid(Color.black, 5.03, new Thermo(-520, 53, 70.2, 8.86, 0.732, 1.67))),
		MoS2(new Formula(Mo, S.__(2)), null, 
			 new Solid(new Color(26, 26, 26), 5.06, 
					   new Thermo(71.7, 7.52, -0.0449, 0.00887, -0.921, -301, 142), 
					   new Hazard(1, 1, 0))),
		N2(new Formula(Element.N.__(2)), "nitrogen", 
		   new Solid(1.03, 
				     new Thermo(84.7), 
		    		 new Hazard(0, 0, 0)), 
		   new Fusion(63), 
		   new Liquid(0.808, 
					  new Thermo(-0.966, 79.3, 28), 
					  new Hazard(0, 0, 0), new Yaws(-15.6, 4.65E2, 1.63E-1, -6.34E-4)), 
		   new Vaporization(3.74, 265, -6.79),
		   new Gas(new Thermo(29.0, 1.85, -9.65, 16.6, 0.000117, -8.67, 226)
			       .addSegment(500, 19.5, 19.9, -8.60, 1.37, 0.528, -4.94, 212)
			       .addSegment(2000, 35.5, 1.13, -0.196, 0.0147, -4.55, -19.0, 225), 
				   new Hazard(0, 0, 0), new Sutherland(17.8, 301, 111))),
		NH3(new Formula(Element.N, Element.H.__(3)), "ammonia", 
			new Solid(0.817, new Thermo(42.2)), 
			new Fusion(195),
			new Liquid(0.684,
					   new Thermo(-71.3, 87.9, 80.8),
					   null, new Yaws(-8.59, 876, 0.268, 3.61E-5)),
			new Vaporization(3.19, 507, -80.8),
			new Gas(new Thermo(20, 49.8, -15.4, 1.92, 0.189, -53.3, 204)
					.addSegment(1400, 52.0, 18.5, -3.77, 0.249, -12.5, -85.5, 224), 
					new Hazard(3, 1, 0), null)),
		NH4Cl(new Formula(NH4, Cl), null,
			  new Solid(1.53,
					    new Thermo(2101, -11194, 23342, -16997, -26.8, -706, 4937)
					    .addSegment(458, -64.4, 327, -149, 24.4, 6.43, -281, -24.4),
					    new Hazard(2, 0, 0))),
		NO(new Formula(Element.N, Element.O), null, 
		   new Solid(new Color(180, 225, 255), Double.NaN, new Thermo(45.9)), 
		   new Fusion(109),
		   new Liquid(new Color(45, 100, 135), 1.27,
				      new Thermo(67),
				      new Hazard(3, 0, 3, SpecialCode.OXIDIZER), new Yaws(-6.88, 325, 0.0501, -1.66E-4)),
		   new Vaporization(121),
		   new Gas(new Thermo(23.8, 12.6, -1.14, -1.50, 0.214, 83.4, 237)
				   .addSegment(1200, 36.0, 0.957, -0.148, 0.00997, -3.00, 73.1, 246), 
				   new Hazard(3, 0, 3, SpecialCode.OXIDIZER), null)),
		NO2(new Formula(Element.N, Element.O.__(2)), null, 
			new Solid(new Thermo(54)), 
			new Fusion(262),
			new Liquid(new Color(150, 20, 20), 1.45,
					   new Thermo(110),
					   new Hazard(3, 0, 0, SpecialCode.OXIDIZER), new Yaws(-8.43, 933, 0.0276, -3.75E-5)),
		    new Vaporization(3.35, 541, -132),
		    new Gas(new Color(245, 110, 20),
		    		new Thermo(16, 76, -54.4, 14.3, 0.239, 26.2, 241)
		    		.addSegment(1200, 56.8, 0.738, -0.145, 0.00978, -5.46, 2.85, 291), 
		    		new Hazard(3, 0, 0, SpecialCode.OXIDIZER), null)),
		Na2B4O7(new Formula(Na.__(2), B4O5OH2).hydrate(8), null,
				new Solid(Color.WHITE, 1.7, new Thermo(-3276, 189, 614)),
				new Fusion(1016),
				new Liquid(new Thermo(265))),
		NaCaNb2O6OH(new Formula(Na, Ca, NbO3.__(2), OH), null, 
				new Solid(new Color(78, 46, 40), 4.75)),
	    NaCaTa2O6OH(new Formula(Na, Ca, Ta2O6, OH), null, 
	    		new Solid(new Color(255, 255, 150), 6.33)),
	    NaBr(new Formula(Na, Br), null,
	    	 new Solid(3.21, new Thermo(50.2, 10.1, 0.963, -0.0284, -0.166, -377, 144)),
	    	 new Fusion(1020),
	    	 new Liquid(2.34, new Thermo(62.3, 0, 0, 0, 0, -358, 180)),
	    	 new Vaporization(1660),
	    	 new Gas(new Thermo(37.4, 0.829, -0.0136, 0.00116, -0.115, -155, 286))),
		NaCl(new Formula(Na, Cl), "salt",
		     new Solid(2.17, new Thermo(50.7, 6.67, -2.52, 10.2, -0.201, -427, 130)),
		     new Fusion(1074),
		     new Liquid(1.56, new Thermo(-42.4, 114, -43.6, 5.90, 39.1, -306, 91.1)),
		     new Vaporization(5.07, 8388, -82.6),
		     new Gas(new Thermo(37.3, 0.792, -0.0270, 0.00231, -0.157, -193, 274))),
		Na2CrO4(new Formula(Na.__(2), CrO4), null,
				new Solid(Color.yellow, 2.7, new Thermo(-1342, 177, 142),
						  new Hazard(3, 0, 0, SpecialCode.OXIDIZER)),
				new Fusion(1065)),
		Na2Cr2O7_2H2O(new Formula(Na.__(2), Cr2O7).hydrate(2), null,
				// FIXME: not clear whether properties are correct for the dihydrate
				 new Solid(Color.red, 2.35, new Thermo(-1979, Double.NaN),
				    	   new Hazard(3, 0, 0, SpecialCode.OXIDIZER))),
		Na2CO3(new Formula(Na.__(2), CO3), "sodaAsh",
			   new Solid(2.54, new Thermo(190, 0, 0, 0, 0, -1183, 343),
					     new Hazard(2, 0, 0))),
		NaHCO3(new Formula(Na, HCO3), "bakingSoda",
			   new Solid(2.20, new Thermo(-951, 102, 87.6),
					     new Hazard(0, 0, 1))),
		NaOH(new Formula(Na, OH), "lye",
			 new Solid(2.13, 
					   new Thermo(419, -1718, 2954, -1597, -6.05, -518, 933).
					   addSegment(572, 86.0, 0, 0, 0, 0, -449, 170),
					   new Hazard(3, 0, 1)),
			 new Fusion(591),
			 new Liquid(Double.NaN,
					    new Thermo(88.3, -2.50, -30.01, 0.862, 0.0422, -443, 184)),
			 new Vaporization(1661),
			 new Gas(new Thermo(49.5, 7.00, -1.39, 0.0952, -0.257, -214, 285))),
		NaNO2(new Formula(Na, Ions.NO2), null,
			  new Solid(new Color(255, 255, 150), 2.17,
					    new Thermo(-359, 106, 237, -1128, 2100, 0).
					               addSegment(463, 1469, -5750, 6170, 0, 8.71E-3))),
		NaNO3(new Formula(Na, NO3), "saltpeter",
			  new Solid(2.26, 
					    new Thermo(-467, 116, 93.1)),
			  new Fusion(581),
			  new Liquid(1.9, new Thermo())),
		Na2O(new Formula(Na.__(2), O), null,
			 new Solid(2.27, 
					   new Thermo(-416, 73, 73),
					   new Hazard(2, 0, 1, SpecialCode.WATER_REACTIVE)),
			 new Fusion(1405)),
		Na2SO4(new Formula(Na.__(2), SO4), null,
			   new Solid(2.66, new Thermo(154, 12.2, 49.4, -15.4, -0.190, -1428, 340)),
			   new Fusion(1157),
			   new Liquid(2.07, new Thermo(197, 0, 0, 0, 0, -1427, 394)),
			   new Vaporization(1.93, 7518, -375),
			   new Gas(new Thermo(157, 0.562, -0.114, 0.00787, -8.27, -1103, 501))),
		Na2SO4_10H2O(new Formula(Na.__(2), SO4).hydrate(10), null,
				   	 new Solid(1.46, new Thermo(-4327, 592)),
				   	 new Fusion(305)),
		NiO(new Formula(Ni, O), null, 
			new Solid(Color.GREEN, 2.7, new Thermo(-240, 38, 47.3, 9.00), 
				      new Hazard(2, 0, 0)), 
		    new Fusion(2228)),
		NiOH2(new Formula(Ni, OH.__(2)), null, 
			  new Solid(Color.GREEN, 4.10, new Thermo(-538, 79, 81), 
					    new Hazard(2, 0, 0))),
	    Ni9S8(new Formula(Ni.__(9), S.__(8)), null,
			  new Solid(new Color(165, 150, 5), 4.8, new Thermo(-847, 475, 443))),
		Ni3Si2O5OH4(new Formula(Ni.__(3), Si2O5, OH.__(4)), null,
				    new Solid(new Color(100, 150, 100), 3.2, new Thermo())),
	    O2(new Formula(Element.O.__(2)), "oxygen",
			new Solid(),
			new Fusion(54),
			new Liquid(1.14, new Thermo(129)),
			new Vaporization(3.95, 340,	-4.14),
			new Gas(new Thermo(31.3, -20.2, 57.9, -36.5, -0.00737, -8.90, 247).
					addSegment(700, 30.03, 8.77, -3.99, 0.788, -0.742, -11.3, 236).
					addSegment(2000, 20.9, 10.7, -2.02, 0.146, 9.25, 5.34, 238), 
					new Hazard(3, 0, 0, SpecialCode.OXIDIZER), new Sutherland(20.1, 292, 127))),
	    PbO(new Formula(Pb, O), null, 
			new Solid(new Color(250, 200, 110), 9.53, 
					  new Thermo(51.7, -2.69, 1.76, -0.788, -238, 121), 
					  new Hazard(3, 0, 0)),
			new Fusion(1161),
			new Liquid(new Thermo(65, 0, 0, 0, 0, -222, 152)),
			new Vaporization(1750),
			new Gas(new Thermo(36.1, -0.879, 0.163, -0.377, 58.2, 281, 70.3))),
		PbS(new Formula(Pb, S), null, 
			new Solid(new Color(26, 26, 26), 7.6, 
					  new Thermo(47.4, 7.55, 2.01, -0.700, -0.0318, -113, 146), 
					  new Hazard(2, 0, 0)),
			new Fusion(1391),
			new Liquid(new Thermo(-98.3, 101, 66.9)),
			new Vaporization(1554),
			new Gas(new Thermo(77.9, -29.8, 7.55, -0.510, -25.1, 68.6, 311))),
		Pb5V3O12Cl(new Formula(Pb.__(5), VO4.__(3), Cl), null,
				   new Solid(new Color(210, 0, 0), 6.90, new Thermo())),
		Sb2S3(new Formula(Sb.__(2), S.__(3)), null, 
			  new Solid(new Color(100, 100, 100), 4.64, 
					    new Thermo(-175, 182, 101, 55.2), 
					    new Hazard(2, 1, 0)),
			  new Fusion(823),
			  new Liquid(new Thermo(231))),
		SiO2(new Formula(Element.Si, Element.O.__(2)), "silica", 
			 new Solid(Color.WHITE, 2.6, 
					   new Thermo(72.8, 1.29, -0.004, 0.0008, -4.14, -941, 114),
					   new Hazard(0, 0, 0)),
			 new Fusion(1900),
			 new Liquid(new Thermo(45.6))),
		SnO2(new Formula(Sn, O.__(2)), null,
			 new Solid(Color.WHITE, 7.0, new Thermo(-581, 52, 60), new Hazard(2, 0, 0)),
			 new Fusion(1903),
			 new Liquid()),
		SO2(new Formula(Element.S, Element.O.__(2)), null,
			new Solid(1.93, new Thermo(107)),
			new Fusion(201),
			new Liquid(1.46, new Thermo(-323, 150, 32)),
			new Vaporization(4.38, 967, -42.1),
			new Gas(new Thermo(21.4, -57.8, 16.4, 0.0867, -306, 255).
					addSegment(1200, 57.5, 1.01, -0.0763, 0.00517, -4.05, -324, 303),
					new Hazard(3, 0, 0),
					new Sutherland(12.5, 294, 416))),
		SO3(new Formula(Element.S, Element.O.__(3)), null,
			new Solid(new Thermo(107)),
			new Fusion(290),
			new Liquid(1.92, new Thermo(129)),
			new Vaporization(4.21, 892, -104),
			new Gas(new Thermo(24, 119, -94.4, 27, -0.118, -408, 254)
					.addSegment(1200, 82, 0.622, -0.122, 0.00829, -6.70, -438, 331),
					new Hazard(3, 0, 3, SpecialCode.OXIDIZER),
					new Sutherland(2.2, 293, 445))),
	    SrSO4(new Formula(Sr, SO4), null,
	    	  new Solid(Color.WHITE, 3.96, new Thermo(-1453, 117, 91.7, 55.4), new Hazard(1, 0, 0)),
	    	  new Fusion(1879),
	    	  new Liquid(new Thermo(136))),
		TiO2(new Formula(Ti, O.__(2)), null, 
		     new Solid(4.23, 
		    		   new Thermo(67, 18.7, -11.6, 2.45, -1.49, -965, 117), 
		    		   new Hazard(1, 0, 0)), 
		     new Fusion(2116),
		     new Liquid(new Thermo(78.0)),
		     new Vaporization(3245),
		     new Gas(new Thermo(285))),
		UO2(new Formula(U, O.__(2)), null, 
			new Solid(Color.black, 11.0, 
					  new Thermo(70, 40, -37.5, 12.7, -1.45, -1111, 144), 
					  new Hazard(1, 0, 0)), 
		    new Fusion(3140),
		    new Liquid(new Thermo(100))),
		V2O5(new Formula(V.__(2), O.__(5)), null, 
				new Solid(Color.yellow, 3.36, 
						  new Thermo(162, 33.3, -12.9, 5.13, -3.58, -1612, 297), 
						  new Hazard(3, 0, 0)), 
			    new Fusion(963),
			    new Liquid(new Thermo(191, -1.69, 8.06, -1.26, -1.74, -1559, 396))),
		ZnCl2(new Formula(Zn, Cl.__(2)), null,
			  new Solid(2.91,
					    new Thermo(-415, 111, 71.3),
					    new Hazard(3, 0, 0)),
			  new Fusion(563),
			  new Liquid(new Thermo(-386, 129, 101)),
			  new Vaporization(1005),
			  new Gas(new Thermo(254))),
		ZnS(new Formula(Zn, S), null, 
			new Solid(4.09, 
					  new Thermo(-206, 201, 53.6, 3.97, 0, 0.814), 
					  new Hazard(1, 0, 0))),
		ZrSiO4(new Formula(Zr, SiO4), null,
			   new Solid(new Color(210, 210, 210), 4.56,
					     new Thermo(85.2, 137, -94.6, 21.7, -1.74, -2061, 140)
			                        .addSegment(1700, 151, 0, 0, 0, 0, -2082, 238),
			             new Hazard(1, 0, 1))),
		METHANE(new Formula(Element.C, Element.H.__(4)).setSmiles("c"), null, 
				new Solid(0.494, new Thermo(104)),
				new Fusion(90),
				new Liquid(new Thermo(-74, 116, 52.9)),
				new Vaporization(3.99, 443, -0.49),
				new Gas(new Thermo(-0.703, 108, -42.5, 5.86, 0.679, -76.8, 159)
						.addSegment(1300, 85.8, 11.2, -2.11, 0.138, -26.4, -154, 224),
						new Hazard(1, 4, 0), new Sutherland(11.6, 311, 165))
				),
		ETHANE(new Formula(Element.C.__(2), Element.H.__(6)).setSmiles("cc"), null, 
			   new Solid(),
			   new Fusion(90),
			   new Liquid(new Thermo(127)),
			   new Vaporization(4.51, 791, -6.42),
			   new Gas(new Thermo(6.16, 173, -0.683, 9.07, 0.126, -93.1, 186),
					   new Hazard(1, 4, 0), new Sutherland(9.1, 298, 272))
				),
		PROPANE(new Formula(Element.C.__(3), Element.H.__(8)).setSmiles("ccc"), null, 
				new Solid(new Thermo(130)),
				new Fusion(86),
				new Liquid(new Thermo(-119, 171, 98.4)),
				new Vaporization(4.54, 1149, 24.9),
				new Gas(new Thermo(-105, 270, 12.8, 231, -0.689, -0.00266),
						new Hazard(1, 4, 0), new Sutherland(8.0, 289, 338))
				),
		N_BUTANE(new Formula(Element.C.__(4), Element.H.__(10)).setSmiles("cccc"), "butane", 
				 new Solid(),
			  	 new Fusion(135),
			  	 new Liquid(new Thermo(-148, 230, 132)),
			  	 new Vaporization(4.36, 1176, -2.07),
			  	 new Gas(new Thermo(-126, 310, 21.9, 294, -0.829, -0.00695),
			  			 new Hazard(1, 4, 0), new Sutherland(8.4, 289, 400))
				),
		ISO_BUTANE(new Formula(Element.C.__(4), Element.H.__(10)).setSmiles("cc(c)c"), "butane", 
				   new Solid(),
				   new Fusion(113),
				   new Liquid(new Thermo(201)),
				   new Vaporization(4.33, 1132,	0.918),
				   new Gas(new Thermo(-126, 310, 13.3, 316, -0.991, -0.0157),
						   new Hazard(1, 4, 0), new Sutherland(7.5, 296, 385))
					),
		ETHENE(new Formula(Element.C.__(2), Element.H.__(4)).setSmiles("c=c"), null, 
			   new Solid(),
			   new Fusion(104),
			   new Liquid(new Thermo(-19, 117, 67.4)),
			   new Vaporization(3.87, 584, -18.3),
			   new Gas(new Thermo(-6.39, 184, -113, 28.5, 0.316, 48.2, 163)
					   .addSegment(1200, 107, 13.7, -2.63, 0.175, -26.1, -35.4, 275),
					   new Hazard(2, 4, 2), new Sutherland(9.85, 273, 250))
			   ),
		PROPENE(new Formula(Element.C.__(3), Element.H.__(6)).setSmiles("cc=c"), null, 
				new Solid(),
				new Fusion(88),
				new Liquid(new Thermo(-3.93, 195.7, 90)),
				new Vaporization(3.97, 796, -24.9),
				new Gas(new Thermo(20, 267, 6.57, 215, -0.784, 0.0325),
						new Hazard(1, 4, 1), new Sutherland(8.35, 273, 331))
			   ),
		N_BUTENE(new Formula(Element.C.__(4), Element.H.__(8)).setSmiles("c/(c=c)\\c"), "butene", /* cis-2-butene */ 
				 new Solid(new Thermo(163)),
			  	 new Fusion(134),
			  	 new Liquid(new Thermo(-29.8, 220, 127)),
			  	 new Vaporization(277),
			  	 new Gas(new Thermo(-7.1, 301, 0.260, 297, -104, -0.0642),
			  			 new Hazard(1, 4, 0), new Sutherland(7.99, 298, 407))
				),
		ISO_BUTENE(new Formula(Element.C.__(4), Element.H.__(8)).setSmiles("cc(=c)c"), "butene",
				   new Solid(new Thermo(149)),
			  	   new Fusion(132),
				   new Liquid(new Thermo(-36.7, 134, 161)),
				   new Vaporization(3.65, 799, -46.6),
				   new Gas(new Thermo(-16.9, 294, 21.1, 268, -92.5, -0.322),
						   new Hazard(1, 4, 0), new Sutherland(7.36, 273, 391))
				),
		BUTADIENE(new Formula(Element.C.__(4), Element.H.__(6)).setSmiles("c=cc=c"), null, /* 1,3-butadiene */ 
				  new Solid(),
				  new Fusion(164),
				  new Liquid(new Thermo(90.5, 199, 124)),
				  new Vaporization(4, 942, -32.8),
				  new Gas(new Thermo(110, 279, 6.97, 147, -41.2, -2.73),
					      new Hazard(3, 4, 2), new Sutherland(250, 273, 395))
			   ),
		BENZENE(new Formula(Element.C.__(6), Element.H.__(6)).setSmiles("c1ccccc1"), null, 
				new Solid(new Thermo(36.3, 45.6, 118)),
				new Fusion(278),
				new Liquid(new Thermo(49, 173, 136)),
				new Vaporization(0.146, 39.2, -261),
				new Gas(new Thermo(82.9, 269, 67.6, 220, -71.8, -4.99),
						new Hazard(2, 3, 0), new Sutherland(601, 300, 519))
			   ),
		TOLUENE(new Formula(Element.C.__(7), Element.H.__(8)).setSmiles("Cc1ccccc1"), null, 
				new Solid(new Thermo(179)),
				new Fusion(178),
				new Liquid(new Thermo(12, 220, 136)),
				new Vaporization(4.14, 1378, -50.5),
				new Gas(new Thermo(50.4, 321, 37.2, 355, -125, -3.12),
						new Hazard(2, 3, 0), new Sutherland(550, 300, 564))
			   ),
		OXIRANE(new Formula(Element.C.__(2), Element.H.__(4), Element.O).setSmiles("c1co1"), null, 
				new Solid(),
				new Fusion(104),
				new Liquid(new Thermo(95.7, 149, 86.9)),
				new Vaporization(5.85, 2023, 62.7),
				new Gas(new Thermo(-23.3, 276, -189, 51, 0.387, -55.1, 143)
						.addSegment(1200, 131, 13.8, -2.65, 0.176, -30.0, -158, 313),
						new Hazard(3, 4, 3), new Sutherland(9.0, 273, 237))
				),
		ETHANOLAMINE(new Formula(Element.C.__(2), Element.H.__(7), Element.N, Element.O).setSmiles("c(co)n"), null, 
				new Solid(),
				new Fusion(283),
				new Liquid(1.01, new Thermo(), new Hazard(3, 2, 0), null),
				new Vaporization(4.29, 1409, -116),
				new Gas(new Thermo())
				)
		;
		
		private Chemical delegate;
		
		private Compounds(Formula formula, String commonName, Solid solid, Fusion fusion, 
				Liquid liquid,	Vaporization vaporization, Gas gas) 
		{
			this.delegate = new SimpleChemical(formula, commonName, solid, fusion, liquid, vaporization, gas);
			CompoundDictionary.register(formula, this);
		}
		
		private Compounds(Formula formula, String commonName, Solid solid, Fusion fusion, 
				Liquid liquid,	Vaporization vaporization) 
		{
			this(formula, commonName, solid, fusion, liquid, vaporization, new Gas());
		}
		
		private Compounds(Formula formula, String commonName, Solid solid, Fusion fusion, 
				Liquid liquid) 
		{
			this(formula, commonName, solid, fusion, liquid, null);
		}
		
		private Compounds(Formula formula, String commonName, Solid solid, Fusion fusion) {
			this(formula, commonName, solid, fusion, new Liquid());
		}
		
		private Compounds(Formula formula, String commonName, Solid solid) {
			this(formula, commonName, solid, null);
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
		public ConditionProperties getProperties(Condition condition) {
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

		@Override
		public ConditionProperties getProperties(Condition condition, State state) {
			return delegate.getProperties(condition, state);
		}

		public static Compounds forFormula(Formula.Part part) {
			return forFormula(new Formula(part));
		}
		public static Compounds forFormula(Formula formula) {
			Compounds compound = valueOf(formula.toString());
			if (compound == null) {
				throw new IllegalArgumentException("Compound not found: " + formula);
			}
			return compound;
		}
	}
}
