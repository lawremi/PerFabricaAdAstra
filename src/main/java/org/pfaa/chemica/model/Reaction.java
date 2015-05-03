package org.pfaa.chemica.model;


public class Reaction {
	private Equation equation;

	public Reaction(Equation equation) {
		this.equation = equation;
	}
	
	public double getEnthalpy() {
		return 0; // TODO
	}
	
	public double getEntropy() {
		return 0; // TODO
	}
	
	public Equation getEquation() {
		return equation;
	}
}
