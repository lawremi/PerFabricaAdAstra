package org.pfaa.chemica.model;

public class IndustrialMaterialUtils {

	public static Condition getCanonicalCondition(IndustrialMaterial material, State state) {
		Integer temp = getCanonicalTemperature(material, state);
		if (temp == null) {
			return null;
		}
		return new Condition(temp, Constants.STANDARD_PRESSURE);
	}

	private static Integer getCanonicalTemperature(IndustrialMaterial material, State state) {
		switch(state) {
		case SOLID:
			if (material instanceof Fusible) {
				Fusion fusion = ((Fusible)material).getFusion();
				if (fusion != null) {
					return Math.min(fusion.getTemperature() - 1, Constants.STANDARD_TEMPERATURE);
				}
			}
			if (material instanceof Vaporizable) {
				Vaporization vaporization = ((Vaporizable)material).getVaporization();
				if (vaporization != null) {
					return Math.min(vaporization.getTemperature() - 1, Constants.STANDARD_TEMPERATURE);
				}
			}
			break;
		case LIQUID:
			if (material instanceof Fusible) {
				Fusion fusion = ((Fusible)material).getFusion();
				int lowerBound = Constants.STANDARD_TEMPERATURE;
				if (material instanceof Vaporizable) {
					Vaporization vaporization = ((Vaporizable) material).getVaporization();
					if (vaporization != null) {
						lowerBound = Math.min(lowerBound, vaporization.getTemperature() - 1);
					}
				}
				if (fusion != null) {
					return Math.max(fusion.getTemperature(), lowerBound);
				} else {
					return null;
				}
			}
			break;
		case GAS:
			if (material instanceof Vaporizable) {
				Vaporization vaporization = ((Vaporizable)material).getVaporization();
				if (vaporization != null) {
					return Math.max(vaporization.getTemperature(), Constants.STANDARD_TEMPERATURE);
				} else {
					return null;
				}
			}
			break;
		default:
		}
		return Constants.STANDARD_TEMPERATURE;
	}

}
