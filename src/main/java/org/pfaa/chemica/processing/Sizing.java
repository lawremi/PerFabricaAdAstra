package org.pfaa.chemica.processing;

public interface Sizing extends UnitOperation {
	enum Direction {
		INCREASE,
		DECREASE
	}
	
	Direction getDirection();
}
