package org.pfaa.chemica.processing;

import java.util.List;

import org.pfaa.chemica.model.Condition;

public interface Conversion {
	List<MaterialSpec<?>> getInputs();
	List<MaterialSpec<?>> getOutputs();
	
	double getEnergy();
	Condition getCondition();
}
