package org.pfaa.chemica.model;

public interface Transition {
	default int getTemperature() {
		return this.getCondition().temperature;
	}
	
	default Condition getCondition() {
		return new Condition(this.getTemperature());
	}
}
