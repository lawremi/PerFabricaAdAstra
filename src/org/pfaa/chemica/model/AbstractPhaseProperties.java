package org.pfaa.chemica.model;

import java.awt.Color;

public abstract class AbstractPhaseProperties implements ChemicalPhaseProperties {
	private Color color;
	private double density;
	private double enthalpy;
	private double entropy;
	private Hazard hazard;
	
	public AbstractPhaseProperties(Color color, double density, double enthalpy, double entropy, Hazard hazard) {
		this.color = color;
		this.density = density;
		this.enthalpy = enthalpy;
		this.entropy = entropy;
		this.hazard = hazard;
	}
	
	/* (non-Javadoc)
	 * @see org.pfaa.chemica.PhaseProperties#getColor()
	 */
	@Override
	public Color getColor() {
		return color;
	}

	/* (non-Javadoc)
	 * @see org.pfaa.chemica.PhaseProperties#getDensity()
	 */
	@Override
	public double getDensity() {
		return density;
	}

	/* (non-Javadoc)
	 * @see org.pfaa.chemica.PhaseProperties#getEnthalpy()
	 */
	@Override
	public double getEnthalpy() {
		return enthalpy;
	}

	/* (non-Javadoc)
	 * @see org.pfaa.chemica.PhaseProperties#getEntropy()
	 */
	@Override
	public double getEntropy() {
		return entropy;
	}

	/* (non-Javadoc)
	 * @see org.pfaa.chemica.PhaseProperties#getHazard()
	 */
	@Override
	public Hazard getHazard() {
		return hazard;
	}

}