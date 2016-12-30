package org.pfaa.chemica.model;

import java.util.ArrayList;
import java.util.List;

public class Thermo {

	private List<Segment> shomates = new ArrayList<Segment>();
	private List<Double> temperatureBreaks = new ArrayList<Double>();
	
	private Thermo(Thermo thermo) {
		this.shomates.addAll(thermo.shomates);
		this.temperatureBreaks.addAll(thermo.temperatureBreaks);
	}
	
	private Thermo(Segment shomate) {
		this.shomates.add(shomate);
	}
	
	public Thermo() { /* uncharacterized */
		this(Double.NaN);
	}
	/* 
	 * this constructor assumes that S is independent of temperature, and we use it only to 
	 * compute the enthalpy of the transition to this phase. 
	 * 
	 * */
	public Thermo(double S /* J/mol */) { 
		this(Double.NaN, S);
	}
	public Thermo(double H, double S) {
		this(H, S, Double.NaN);
	}
	public Thermo(double H /* kJ/mol */, double S, double Cp /* J/mol */) { /* constant heat capacity */
		this(new Segment(H, S, Cp));
	}
	public Thermo(double H, double S, double a, double b) { /* linear heat capacity */
		this(new Segment(H, S, a, b));
	}
	public Thermo(double H, double S, double a, double b, double c, double e) { /* quadratic heat capacity */
		this(new Segment(H, S, a, b, c, e));
	}
	public Thermo(double a, double b, double c, double d, double e, double f, double g) { /* quadratic heat capacity */
		this(new Segment(a, b, c, d, e, f, g));
	}
	
	private Segment findSegment(double t) {
		int si = this.temperatureBreaks.size() - 1;
		while(si >= 0 && this.temperatureBreaks.get(si) > t)
			si--;
		return shomates.get(si + 1);
	}
	
	public double getHeatCapacity(double t) {
		return findSegment(t).getHeatCapacity(t);
	}

	public double integrateHeatCapacity(double t) {
		return getEnthalpy(t) - getStandardEnthalpy();
	}
	
	public double getStandardEnthalpy() {
		return getHeatCapacity(Constants.STANDARD_TEMPERATURE);
	}
	
	public double getStandardEntropy() {
		return getEntropy(Constants.STANDARD_TEMPERATURE);
	}
	
	public double getEnthalpy(double t) {
		return findSegment(t).getEnthalpy(t);
	}
	
	public double getEntropy(double t) {
		return findSegment(t).getEntropy(t);
	}
	
	public ConditionThermo at(Condition condition) {
		return this.at(condition, null);
	}
	
	public ConditionThermo at(Condition condition, Thermo adjacent) {
		double enthalpy = getEnthalpy(condition.temperature);
		if (Double.isNaN(enthalpy) && adjacent != null) {
			enthalpy = estimateEnthalpyFromAdjacent(condition, adjacent);
		}
		return new ConditionThermo(
				getHeatCapacity(condition.temperature),
				enthalpy,
				getEntropy(condition.temperature));
	}
	
	private double estimateEnthalpyFromAdjacent(Condition condition, Thermo adjacent) {
		/* Assume we are at equilibrium with the adjacent state */
		ConditionThermo condAdjacent = adjacent.at(condition);
		double transEnthalpy = (this.getEntropy(condition.temperature) - condAdjacent.entropy) * condition.temperature;
		return condAdjacent.enthalpy + transEnthalpy;
	}

	public boolean knowsHeatCapacity() {
		return this.shomates.size() > 0 && !Double.isNaN(this.shomates.get(0).a);
	}

	public Thermo addSegment(double t0, Segment segment) {
		Thermo clone = new Thermo(this);
		clone.shomates.add(segment);
		clone.temperatureBreaks.add(t0);
		return clone;
	}
	
	public Thermo addSegment(double t0, double Cp) {
		return addSegment(t0, Cp, 0);
	}
	
	public Thermo addSegment(double t0, double a, double b) {
		return addSegment(t0, a, b, 0);
	}
	
	public Thermo addSegment(double t0, double a, double b, double transition) {
		return addSegment(t0, a, b, 0, 0, transition);
	}
	
	public Thermo addSegment(double t0, double a, double b, double c, double e, double transition) {
		Segment segment = new Segment(a, b, c, 0, e, 0, 0);
		segment.offsetEnthalpy(this.getEnthalpy(t0) - segment.getEnthalpy(t0) + transition);
		segment.offsetEntropy(this.getEntropy(t0) - segment.getEntropy(t0));
		this.addSegment(t0, segment);
		return this;
	}
	
	public Thermo addSegment(double t0, double a, double b, double c, double d, double e, double f, double g) {
		return addSegment(t0, new Segment(a, b, c, d, e, f, g));
	}
	
	private static class Segment {
		private double a, b, c, d, e, f, g;
		
		public Segment(double a, double b, double c, double d, double e, double f, double g) { // full form
			this.a = a;
			this.b = b;
			this.c = c;
			this.d = d;
			this.e = e;
			this.f = f;
			this.g = g;
		}
		
		/* Simplified forms for when there are no published Shomate parameters */
		
		/* H and S are standard enthalpy and entropy, respectively, at 298 K */
		
		public Segment(double H, double S, double a) { // constant
			this(H, S, a, 0);
		}
		public Segment(double H, double S, double a, double b) { // linear
			this(H, S, a, b, 0, 0);
		}
		public Segment(double H, double S, double a, double b, double c, double e) { // quadratic
			this.a = a;
			this.b = b;
			this.c = c;
			this.d = 0;
			this.e = e;
			this.f = H - this.getEnthalpy(Constants.STANDARD_TEMPERATURE);
			this.g = S - this.getEntropy(Constants.STANDARD_TEMPERATURE);
		}
		
		public /* J/(mol*K) */ double getHeatCapacity(double t) {
			t = t / 1000;
			return a + b * t + c * Math.pow(t, 2) + d * Math.pow(t, 3) + e / Math.pow(t, 2);
		}
		
		public /* kJ/mol */ double getEnthalpy(double t) {
			if (Double.isNaN(a) && t == Constants.STANDARD_TEMPERATURE) {
				return f;
			}
			t = t / 1000;
			return a * t + b * Math.pow(t, 2) / 2 + c * Math.pow(t, 3) / 3 + d * Math.pow(t, 4) / 4 - e / t + f;
		}
		
		public /* J/mol */ double getEntropy(double t) {
			if (Double.isNaN(a)) {
				return g;
			}
			t = t / 1000;
			return a * Math.log(t) + b * t + c * Math.pow(t, 2) / 2 + d * Math.pow(t, 3) / 3 - e / (2*Math.pow(t, 2)) + g;
		}
		
		public Segment offsetEntropy(double offset) {
			this.f += offset;
			return this;
		}

		public Segment offsetEnthalpy(double offset) {
			this.g += offset;
			return this;
		}
	}
}
