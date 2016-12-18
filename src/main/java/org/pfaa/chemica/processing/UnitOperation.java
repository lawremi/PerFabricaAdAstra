package org.pfaa.chemica.processing;

public abstract class UnitOperation<T extends UnitOperationType<?,?>> extends AbstractConversion<T> {
	
	protected UnitOperation(T type) {
		super(type);
	}
	
}
