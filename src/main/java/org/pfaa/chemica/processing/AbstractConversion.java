package org.pfaa.chemica.processing;

import org.pfaa.chemica.model.Condition;

public abstract class AbstractConversion<T extends ConversionType<?,?>> implements Conversion<T> {

	private T type;
	private Condition condition;
	
	public AbstractConversion(T type) {
		this.type = type;
	}

	@Override
	public T getType() {
		return this.type;
	}

	@Override
	public Conversion<T> at(Condition condition) {
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
		return null;
	}
}
