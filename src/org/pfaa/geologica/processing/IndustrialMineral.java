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
		CHRYSOTILE(new Color(145, 165, 145), 2.53),
		FELDSPAR(new Color(240, 210, 225), 2.56),
		GARNET(new Color(227, 154, 38), 4.0),
		KYANITE(Color.BLUE, 3.60),
		MICA(new Color(180, 180, 190), 2.82),
		QUARTZ(Color.WHITE, 2.65),
		OLIVINE(new Color(155, 230, 40), 3.30),
		WOLLASTONITE(Color.WHITE, 2.90)	
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
