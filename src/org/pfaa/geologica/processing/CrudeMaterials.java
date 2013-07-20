package org.pfaa.geologica.processing;

import java.awt.Color;

import org.pfaa.chemica.model.Hazard;
import org.pfaa.chemica.model.Phase;

public enum CrudeMaterials implements CrudeMaterial {
	CHRYSOTILE(new Color(145, 165, 145), 2.53),
	FELDSPAR(new Color(240, 210, 225), 2.56),
	MICA(new Color(180, 180, 190), 2.82),
	QUARTZ(Color.WHITE, 2.65), 
	OLIVINE(new Color(155, 230, 40), 3.30), 
	WOLLASTONITE(Color.WHITE, 2.90)
	
	;

	private String oreDictKey;
	private Color color;
	private Phase phase;
	private double density;
	private Hazard hazard;
	
	private CrudeMaterials(String oreDictKey, Color color, double density, Phase phase, Hazard hazard) {
		this.oreDictKey = oreDictKey;
		this.color = color;
		this.density = density;
		this.phase = phase;
		this.hazard = hazard;
	}
	
	private CrudeMaterials(Color color, double density) {
		this(null, color, density, Phase.SOLID, new Hazard());
	}
	
	@Override
	public String getOreDictKey() {
		return oreDictKey;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public Phase getPhase() {
		return phase;
	}

	@Override
	public double getDensity() {
		return density;
	}

	@Override
	public Hazard getHazard() {
		return hazard;
	}

}
