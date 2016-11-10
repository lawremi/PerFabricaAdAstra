package org.pfaa.chemica.integration;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Constants;
import org.pfaa.chemica.model.Strength;
import org.pfaa.chemica.processing.TemperatureLevel;

public class RecipeCostUtils {

	public static int grindingEnergyForStrength(Strength strength) {
		switch(strength) {
		case WEAK:
			return 2400;
		case MEDIUM:
			return 4000;
		case STRONG:
			return 5600;
		case VERY_STRONG:
			return 7200;
		default:
			throw new IllegalArgumentException("unhandled strength: " + strength);
		}
	}

	public static int blastTicksForTemperatureLevel(TemperatureLevel temp) {
		switch(temp) {
		case LOW:
			return 40;
		case MEDIUM:
			return 80;
		case HIGH:
			return 120;
		case VERY_HIGH:
			return 160;
		default:
			throw new IllegalArgumentException("unhandled temperature level: " + temp);
		}
	}
	
	public static int arcTicksForTemperature(int temp) {
		return temp / 16;
	}
	
	public static int rfFromTemperatureLevel(TemperatureLevel temp) {
		return temp.getReferenceTemperature();
	}

	public static int rfFromTemperature(int temp) {
		return (int)Math.pow(Math.abs(temp - Constants.STANDARD_TEMPERATURE) / 10, 1.5);
	}

	public static int rfFromPressure(double pressure) {
		return (int)Math.pow(Math.abs(pressure - Constants.STANDARD_PRESSURE) / 100, 1.5);
	}
	
	public static int rfFromCondition(Condition condition) {
		return rfFromTemperature(condition.temperature) + rfFromPressure(condition.pressure);
	}
}
