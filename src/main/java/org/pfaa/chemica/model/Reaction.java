package org.pfaa.chemica.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reaction {
	private Equation equation;
	private double activationEnergy = 50000; // dependency of rate on temperature
	
	public Reaction(Equation equation) {
		this.equation = equation;
	}
	
	public double getEnthalpy() {
		return 0; // TODO
	}
	
	public double getEntropy() {
		return 0; // TODO
	}
}
