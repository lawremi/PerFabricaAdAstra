package org.pfaa.chemica.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reaction {
	private Equation equation;
	private double temperature; // equilibrium temperature
	private double enthalpy; // heat released/absorbed
	private double activationEnergy = 50000; // dependency of rate on temperature
	
	public Reaction(Equation equation) {
		this.equation = equation;
	}
	public Reaction(Equation equation, double temperature) {
		this(equation);
		this.temperature = temperature;
	}
}
