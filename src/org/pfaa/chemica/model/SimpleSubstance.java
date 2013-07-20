package org.pfaa.chemica.model;

import java.awt.Color;

public class SimpleSubstance implements Chemical {

	private String name;
	private String oreDictKey;
	private Fusion fusion;
	private Vaporization vaporization;
	
	private AbstractPhaseProperties solid;
	private AbstractPhaseProperties liquid;
	private AbstractPhaseProperties gas;
	
	private AbstractPhaseProperties currentPhase;
	
	public SimpleSubstance(String name, String oreDictKey, Fusion fusion, Vaporization vaporization, AbstractPhaseProperties... phases) {
		this.name = name;
		this.oreDictKey = oreDictKey == null ? name : oreDictKey;;
		this.fusion = fusion;
		this.vaporization = vaporization;
		
		if (phases.length > 0) {
			this.currentPhase = phases[0];
		}
		for (AbstractPhaseProperties phase : phases) {
			if (phase instanceof Solid)
				this.solid = phase;
			else if (phase instanceof Liquid)
				this.liquid = phase;
			else if (phase instanceof Gas)
				this.gas = phase;
		}
	}
	
	@Override
	public String getOreDictKey() {
		return oreDictKey;
	}

	@Override
	public Fusion getFusion() {
		return fusion;
	}

	@Override
	public Vaporization getVaporization() {
		return vaporization;
	}

	private Chemical cloneForPhase(AbstractPhaseProperties phase) {
		return new SimpleSubstance(name, oreDictKey, fusion, vaporization, phase, solid, liquid, gas);
	}
	
	@Override
	public Chemical solid() {
		return cloneForPhase(solid);
	}

	@Override
	public Chemical liquid() {
		return cloneForPhase(liquid);
	}

	@Override
	public Chemical gas() {
		return cloneForPhase(gas);
	}

	@Override
	public Color getColor() {
		return currentPhase.getColor();
	}

	@Override
	public double getDensity() {
		return currentPhase.getDensity();
	}

	@Override
	public double getEnthalpy() {
		return currentPhase.getEnthalpy();
	}

	@Override
	public double getEntropy() {
		return currentPhase.getEntropy();
	}

	@Override
	public Hazard getHazard() {
		return currentPhase.getHazard();
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public Phase getPhase() {
		return currentPhase.getPhase();
	}

}
