package org.pfaa.chemica.processing;

public class MassTransfer extends UnitOperation {

	protected MassTransfer(Type type, MaterialSpec input) {
		super(type, input);
	}

	/* Moving one material with respect to another; either mixing or separation */
	public static interface MassTransferType extends Type { }
}
