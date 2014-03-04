package org.pfaa.chemica.model;

import java.util.ArrayList;
import java.util.List;

public class Thermo {

	private List<Segment> shomates = new ArrayList();
	private List<Double> temperatureBreaks;
	
	private Thermo(Thermo thermo) {
		this.shomates.addAll(thermo.shomates);
		this.temperatureBreaks.addAll(thermo.temperatureBreaks);
	}
	
	private Thermo(Segment shomate) {
		this.shomates.add(shomate);
	}
	
	public Thermo(double S) { /* supports only transitions to this phase, but no heating or reactions */
		this(Double.NaN, S, Double.NaN);
	}
	public Thermo(double H, double S, double Cp) { /* constant heat capacity */
		this(H, S, Cp, 0);
	}
	public Thermo(double H, double S, double a, double b) { /* linear heat capacity */
		this(H, S, a, b, 0, 0);
	}
	public Thermo(double H, double S, double a, double b, double c, double e) { /* quadratic heat capacity */
		this(new Segment(H, S, a, b, c, e));
	}
	public Thermo(double a, double b, double c, double d, double e, double f, double g) { /* quadratic heat capacity */
		this(new Segment(a, b, c, d, e, f, g));
	}
	
	private Segment findSegment(double t) {
		int si = this.temperatureBreaks.size() - 1;
		while(si >= 0 && this.temperatureBreaks.get(si) < t)
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
		return addSegment(t0, new Segment(this.getStandardEnthalpy(), this.getStandardEntropy(), a, b));
	}
	
	public Thermo addSegment(double t0, double a, double b, double c, double d, double e, double f, double g) {
		return addSegment(t0, new Segment(a, b, c, d, e, f, g));
	}
	
	private static class Segment {
		public final double a, b, c, d, e, f, g;
		
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
		
		public double getHeatCapacity(double t) {
			t = t / 1000;
			return a + b * t + c * Math.pow(t, 2) + d * Math.pow(t, 3) + e / Math.pow(t, 2);
		}
		
		public double getEnthalpy(double t) {
			t = t / 1000;
			return a * t + b * Math.pow(t, 2) / 2 + c * Math.pow(t, 3) / 3 + d * Math.pow(t, 4) / 4 - e / t + f;
		}
		
		public double getEntropy(double t) {
			t = t / 1000;
			return a * Math.log(t) + b * t + c * Math.pow(t, 2) / 2 + d * Math.pow(t, 3) / 3 - e / (2*Math.pow(t, 2)) + g;
		}
	}
}
