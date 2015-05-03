package org.pfaa.chemica.processing;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.IndustrialMaterial;

/* An industrial material in some condition and form */
public interface Stream {
	public IndustrialMaterial getIndustrialMaterial();
	public Condition getCondition();
	public Form getForm();
}
