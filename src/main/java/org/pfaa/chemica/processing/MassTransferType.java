package org.pfaa.chemica.processing;

import org.pfaa.chemica.model.IndustrialMaterial;

/* Moving one material with respect to another; either mixing or separation */
public interface MassTransferType<M extends IndustrialMaterial, C extends MassTransfer<?>> extends UnitOperationType<M,C> { }
