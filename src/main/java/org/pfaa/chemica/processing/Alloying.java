package org.pfaa.chemica.processing;

import java.util.List;

import org.pfaa.chemica.model.Alloy;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.MaterialState;

public class Alloying extends ConditionedConversion {

	private Combination combination;
	
	protected Alloying(Combination combination) {
		this.combination = combination;
	}

	@Override
	public Type getType() {
		return null;
	}

	@Override
	public List<MaterialStoich<?>> getInputs() {
		return this.combination.getInputs();
	}

	@Override
	public List<MaterialStoich<?>> getOutputs() {
		return this.combination.getOutputs();
	}

	@Override
	public Form getOutputForm(Form inputForm) {
		return inputForm.isRegular() ? inputForm : inputForm.compact();
	}
	
	@Override
	protected Condition deriveCondition() {
		return this.combination.getCondition();
	}

	public static Alloying yielding(MaterialState<Alloy> alloy) {
		return new Alloying(Combination.yielding(alloy).at(alloy.material.getFusion().getCondition()));
	}
}
