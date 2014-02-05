package org.pfaa.chemica.model;

import java.util.ArrayList;
import java.util.List;

public class Thermo {

	private List<Shomate> shomates = new ArrayList();
	private List<Double> temperatureBreaks;
	private double H, S;
	
	private Thermo(Thermo thermo) {
		this.H = thermo.H;
		this.S = thermo.S;
		this.shomates.addAll(thermo.shomates);
		this.temperatureBreaks.addAll(thermo.temperatureBreaks);
	}
	
	public Thermo(double H, double S, Shomate shomate) {
		this.H = H;
		this.S = S;
		this.shomates.add(shomate);
	}
	
	public Thermo(double H, double S, double Cp) { /* constant heat capacity */
		this(H, S, new Shomate(H, S, Cp));
	}

	public Thermo(double H, double S, double a, double b) { /* linear heat capacity */
		this(H, S, new Shomate(H, S, a, b));
	}
	
	private Shomate findSegment(double t) {
		int si = this.temperatureBreaks.size() - 1;
		while(si >= 0 && this.temperatureBreaks.get(si) < t)
			si--;
		return shomates.get(si + 1);
	}
	
	public double getHeatCapacity(double t) {
		return findSegment(t).getHeatCapacity(t);
	}

	public double integrateHeatCapacity(double t) {
		return getEnthalpy(t) - this.H;
	}
	
	public double getStandardEnthalpy() {
		return this.H;
	}
	
	public double getStandardEntropy() {
		return this.S;
	}
	
	public double getEnthalpy(double t) {
		return findSegment(t).getEnthalpy(t);
	}
	
	public double getEntropy(double t) {
		return findSegment(t).getEntropy(t);
	}
	
	public Thermo addSegment(double t0, Shomate segment) {
		Thermo clone = new Thermo(this);
		clone.shomates.add(segment);
		clone.temperatureBreaks.add(t0);
		return clone;
	}
	
	public Thermo addSegment(double t0, double Cp) {
		return addSegment(t0, new Shomate(this.H, this.S, Cp));
	}
	
	public Thermo addSegment(double t0, double a, double b) {
		return addSegment(t0, new Shomate(this.H, this.S, a, b));
	}
	
	public static class Shomate {
		public final double a, b, c, d, e, f, g;
	
		public Shomate(double H, double S, double a) { /* constant heat capacity */
			this(H, S, a, 0);
		}
		
		public Shomate(double H, double S, double a, double b) { /* simple linear relationship */
			this(a, b, 0, 0, 0, estimateF(H, a, b), estimateG(S, a, b));
		}
		
		public Shomate(double a, double b, double c, double d, double e, double f, double g) {
			this.a = a;
			this.b = b;
			this.c = c;
			this.d = d;
			this.e = e;
			this.f = f;
			this.g = g;
		}
		
		private static double estimateF(double H, double a, double b) {
			double t = Constants.STANDARD_TEMPERATURE / 1000;
			return H - a * t - b * Math.pow(t, 2) / 2;
		}
		
		private static double estimateG(double S, double a, double b) {
			double t = Constants.STANDARD_TEMPERATURE / 1000;
			return S - a * Math.log(t) - b * t;
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
