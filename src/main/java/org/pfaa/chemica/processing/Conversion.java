package org.pfaa.chemica.processing;

import java.util.List;

import org.pfaa.chemica.model.Condition;

public interface Conversion {
	Type getType(); 
	
	List<MaterialStoich<?>> getInputs();
	List<MaterialStoich<?>> getOutputs();
	
	default double getEnergy() {
		return 0;
	}
	default Condition getCondition() {
		return Condition.STP;
	}
		
	public interface Type {
		
	}
}
