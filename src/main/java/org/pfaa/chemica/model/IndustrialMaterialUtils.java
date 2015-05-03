package org.pfaa.chemica.model;

public class IndustrialMaterialUtils {

	public static Condition getCanonicalCondition(IndustrialMaterial material, State state) {
		double temperature = Constants.STANDARD_TEMPERATURE;
		if (material instanceof Chemical) {
			Chemical chemical = (Chemical)material;
			temperature = getTemperatureClosestToStandard(chemical, state);
		}
		return new Condition(temperature, Constants.STANDARD_PRESSURE);
	}

	private static double getTemperatureClosestToStandard(Chemical chemical, State state) {
		switch(state) {
		case SOLID:
			return Math.min(chemical.getFusion().getTemperature(), Constants.STANDARD_TEMPERATURE);
		case LIQUID:
			return Math.max(chemical.getFusion().getTemperature(), Constants.STANDARD_TEMPERATURE);
		case GAS:
			return Math.max(chemical.getVaporization().getTemperature(), Constants.STANDARD_TEMPERATURE);
		default:
			return Constants.STANDARD_TEMPERATURE;
		}
	}

}
