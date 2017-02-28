package org.pfaa.chemica.processing;

import org.pfaa.chemica.model.Condition;

public abstract class ConditionedConversion implements Conversion {

	private Condition condition;
	
	public Conversion at(int temp) {
		return at(new Condition(temp));
	}

	public Conversion at(Condition condition) {
		this.condition = condition;
		return this;
	}

	@Override
	public Condition getCondition() {
		if (this.condition == null) {
			this.condition = this.deriveCondition();
		}
		return this.condition;
	}

	protected Condition deriveCondition() {
		return Condition.STP;
	}
}
