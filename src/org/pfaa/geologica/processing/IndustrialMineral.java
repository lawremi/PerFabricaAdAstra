package org.pfaa.geologica.processing;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.Hazard;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.IndustrialMaterial.Phase;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.PhaseProperties;

public interface IndustrialMineral extends Mineral {
	public PhaseProperties getProperties();
	
	public enum IndustrialMinerals implements IndustrialMineral {
		ALUNITE(new Color(225, 180, 65), 2.7),
		APATITE(new Color(160, 190, 160), 3.1),
		BENTONITE(new Color(245, 215, 210), 0.593),
		CHRYSOTILE(new Color(110, 140, 110), 2.53),
		COAL(Color.black, 0.8),
		DIATOMITE(new Color(225, 225, 225), 2),
		DOLOMITE(new Color(225, 205, 205), 2.85),
		FELDSPAR(new Color(240, 210, 225), 2.56),
		FULLERS_EARTH(new Color(160, 160, 120), 0.65),
		GARNET(new Color(227, 154, 38), 4.0),
		GLAUCONITE(new Color(130, 180, 60), 2.6),
		GRAPHITE(new Color(45, 45, 45), 2.15),
		GYPSUM(new Color(230, 230, 250), 2.3),
		KAOLINITE(new Color(245, 235, 235), 2.63),
		KYANITE(new Color(110, 110, 250), 3.60),
		MICA(new Color(195, 195, 205), 2.82),
		MIRABILITE(new Color(240, 250, 210), 1.49),
		QUARTZ(Color.WHITE, 2.65),
		OBSIDIAN(new Color(55, 40, 60), 2.35),
		OLIVINE(new Color(155, 230, 40), 3.30),
		PERLITE(new Color(30, 20, 30), 1.1),
		PUMICE(new Color(230, 185, 185), 0.64),
		SEPIOLITE(new Color(230, 220, 220), 2),
		TALC(new Color(90, 180, 90), 2.7),
		TRONA(new Color(135, 135, 95), 2.14),
		VERMICULITE(new Color(200, 80, 15), 2.5),
		WOLLASTONITE(Color.WHITE, 2.90),
		VOLCANIC_ASH(Color.gray, 3),
		ZEOLITE(new Color(240, 230, 230), 2.3)
		;

		private String oreDictKey;
		private PhaseProperties properties;
		
		private IndustrialMinerals(String oreDictKey, Color color, double density, Hazard hazard) {
			this.oreDictKey = oreDictKey;
			this.properties = new PhaseProperties(color, density, hazard);
		}
		
		private IndustrialMinerals(Color color, double density) {
			this(null, color, density, new Hazard());
		}
		
		@Override
		public String getOreDictKey() {
			return oreDictKey;
		}

		@Override
		public PhaseProperties getProperties(Phase phase) {
			if (phase == Phase.SOLID) {
				return properties;
			}
			return null;
		}

		@Override
		public PhaseProperties getProperties() {
			return properties;
		}

		@Override
		public List<MixtureComponent> getComponents() {
			return Collections.EMPTY_LIST;
		}

		@Override
		public Ore mix(IndustrialMaterial material, double weight) {
			return new SimpleOre(this).mix(material, weight);
		}
	}

}
