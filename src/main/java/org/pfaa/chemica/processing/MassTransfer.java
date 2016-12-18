package org.pfaa.chemica.processing;

public abstract class MassTransfer<T extends MassTransferType<?,?>> extends UnitOperation<T> {

	protected MassTransfer(T type) {
		super(type);
	}
}
