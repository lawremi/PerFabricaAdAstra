package org.pfaa.chemica.processing;

import java.util.List;

import org.pfaa.chemica.model.Condition;

public interface Conversion<T extends ConversionType<?,?>> {
	T getType(); 
	
	List<MaterialStoich<?>> getInputs();
	List<MaterialStoich<?>> getOutputs();
	
	double getEnergy();
	Condition getCondition();
	
	Conversion<T> at(Condition condition);
	default Conversion<T> at(int temp) {
		return at(new Condition(temp));
	}
}
