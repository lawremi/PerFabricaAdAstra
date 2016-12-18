package org.pfaa.chemica.processing;

import org.pfaa.chemica.model.IndustrialMaterial;

public interface UnitOperationType<M extends IndustrialMaterial, C extends UnitOperation<?>> extends ConversionType<M,C> {
}