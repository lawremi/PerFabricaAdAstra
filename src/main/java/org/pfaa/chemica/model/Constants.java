package org.pfaa.chemica.model;

public interface Constants {
	/* Technically Standard Ambient Temperature and Pressure (SATP) */
	public final static int STANDARD_TEMPERATURE = 298; // K
	public final static double STANDARD_PRESSURE = 101; // kPa
	
	/* Besides being a round number, 10% means that a solution is 9 parts solvent (water)
	 * and 1 part solute, so one dust to 9*144 mB, or 1 tiny dust to 144mB, 
	 * which is nicely rounded.
	 */
	public final static float STANDARD_SOLUTE_WEIGHT = 0.1F;
	
	public final static double R = 0.008314; /* (m^3*kPa) / (K*mol) */
	public final static double AIR_DENSITY = 0.001225;
	
	public final static double FLESH_IGNITION_TEMPERATURE = 1000;
}
