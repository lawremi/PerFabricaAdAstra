package org.pfaa.chemica.processing;

import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.Condition;

public class EnthalpyChange extends UnitOperation<EnthalpyChangeType> {

	private MaterialStoich<?> input;
	private MaterialStoich<?> output;
	
	public EnthalpyChange(EnthalpyChangeType type, MaterialStoich<?> input) {
		super(type);
	}

	public EnthalpyChange yields(MaterialStoich<?> output) {
		this.output = output;
		return this;
	}
	
	public MaterialStoich<?> getInput() {
		return this.input;
	}

	public MaterialStoich<?> getOutput() {
		return this.output;
	}

	@Override
	public List<MaterialStoich<?>> getInputs() {
		return Collections.singletonList(this.getInput());
	}

	@Override
	public List<MaterialStoich<?>> getOutputs() {
		return Collections.singletonList(this.getOutput());
	}

	@Override
	public double getEnergy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Condition getCondition() {
		// TODO Auto-generated method stub
		return null;
	}
}
