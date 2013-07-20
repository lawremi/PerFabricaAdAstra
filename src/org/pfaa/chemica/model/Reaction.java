package org.pfaa.chemica.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reaction {
	private List<Chemical> reactants = new ArrayList<Chemical>();
	private List<Chemical> products = new ArrayList<Chemical>();
	private double temperature; // equilibrium temperature
	private double enthalpy; // heat released/absorbed
	private double activationEnergy = 50000; // dependency of rate on temperature
	private Chemical catalyst;
	
	public Reaction(Chemical... substances) {
		List<Chemical> side = reactants;
		for (Chemical substance : substances) {
			if (substance == null) {
				side = products;
			} else {
				side.add(substance);
			}
		}
	}
	public Reaction(double temperature, Chemical... substances) {
		this(substances);
		this.temperature = temperature;
	}
}
