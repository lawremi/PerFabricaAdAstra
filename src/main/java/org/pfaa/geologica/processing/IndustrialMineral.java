package org.pfaa.geologica.processing;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.Hazard;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.State;

public interface IndustrialMineral extends Mineral {
	public enum IndustrialMinerals implements IndustrialMineral {
		ALUNITE(new Color(225, 180, 65), 2.7),
		APATITE(new Color(160, 190, 160), 3.1),
		AZURITE(new Color(20, 20, 200), 3.8),
		SODIUM_MONTMORILLONITE(new Color(245, 215, 210), 0.593),
		CHRYSOTILE(new Color(110, 140, 110), 2.53),
		DIAMOND(new Color(215, 240, 255), 3.51),
		DIATOMITE(new Color(225, 225, 225), 2),
		DOLOMITE(new Color(225, 205, 205), 2.85),
		FELDSPAR(new Color(240, 210, 225), 2.56),
		CALCIUM_MONTMORILLONITE(new Color(160, 160, 120), 0.65),
		GARNET(new Color(227, 154, 38), 4.0),
		GLAUCONITE(new Color(130, 180, 60), 2.6),
		GRAPHITE(new Color(45, 45, 45), 2.15),
		GYPSUM(new Color(230, 230, 250), 2.3),
		KAOLINITE(new Color(245, 235, 235), 2.63),
		KYANITE(new Color(110, 110, 250), 3.60),
		LAZURITE(new Color(0, 85, 175), 2.4),
		MICA(new Color(195, 195, 205), 2.82),
		MIRABILITE(new Color(240, 250, 210), 1.49),
		QUARTZ(Color.WHITE, 2.65),
		OLIVINE(new Color(155, 230, 40), 3.30),
		PERLITE(new Color(255, 255, 245), 1.1),
		PUMICE(new Color(230, 185, 185), 0.64),
		SEPIOLITE(new Color(230, 220, 220), 2),
		SODALITE(new Color(0, 35, 100), 2.3),
		TALC(new Color(220, 245, 225), 2.7),
		TRONA(new Color(135, 135, 95), 2.14),
		VERMICULITE(new Color(210, 200, 170), 2.5),
		WOLLASTONITE(Color.WHITE, 2.90),
		VOLCANIC_ASH(Color.gray, 3),
		ZEOLITE(new Color(240, 230, 230), 2.3)
		;

		private String oreDictKey;
		private ConditionProperties properties;
		
		private IndustrialMinerals(String oreDictKey, Color color, double density, Hazard hazard) {
			this.oreDictKey = oreDictKey;
			this.properties = new ConditionProperties(State.SOLID, color, density, hazard);
		}
		
		private IndustrialMinerals(Color color, double density) {
			this(null, color, density, new Hazard());
		}
		
		@Override
		public String getOreDictKey() {
			return oreDictKey;
		}

		@Override
		public ConditionProperties getProperties(Condition condition) {
			return properties;
		}

		@Override
		public List<MixtureComponent> getComponents() {
			return Collections.emptyList();
		}

		@Override
		public Ore mix(IndustrialMaterial material, double weight) {
			return new SimpleOre(this).mix(material, weight);
		}
	}

}
