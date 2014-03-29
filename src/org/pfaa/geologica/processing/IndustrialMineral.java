package org.pfaa.geologica.processing;

import java.awt.Color;
import java.util.List;

import org.pfaa.chemica.model.Hazard;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.PhaseProperties;
import org.pfaa.chemica.model.IndustrialMaterial.Phase;

public interface IndustrialMineral extends IndustrialMaterial {
	public PhaseProperties getProperties();
	
	public enum IndustrialMinerals implements IndustrialMineral {
		BENTONITE(new Color(245, 215, 210), 0.593),
		CHRYSOTILE(new Color(110, 140, 110), 2.53),
		FELDSPAR(new Color(240, 210, 225), 2.56),
		FULLERS_EARTH(new Color(160, 160, 120), 0.65),
		GARNET(new Color(227, 154, 38), 4.0),
		KAOLINITE(new Color(245, 235, 235), 2.63),
		KYANITE(new Color(110, 110, 250), 3.60),
		MICA(new Color(195, 195, 205), 2.82),
		QUARTZ(Color.WHITE, 2.65),
		OLIVINE(new Color(155, 230, 40), 3.30),
		SEPIOLITE(new Color(230, 220, 220), 2),
		TALC(new Color(90, 180, 90), 2.7),
		VERMICULITE(new Color(200, 80, 15), 2.5),
		WOLLASTONITE(Color.WHITE, 2.90),
		VOLCANIC_ASH(Color.gray, 3),
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
	}

}
