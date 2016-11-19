package org.pfaa.chemica.model;

import java.util.List;

import org.pfaa.chemica.model.Compound.Compounds;

import com.google.common.base.CaseFormat;

/*
 * Catalog of mixture catalysts. Pure substance catalysts can be used directly.
 * This list declares the mixtures. 
 */

public enum Catalysts implements Mixture {
	HTS(Compounds.Fe2O3.mix(Compounds.Cr2O3, 0.1));
	
	private Mixture delegate;
	
	private Catalysts(Mixture delegate) {
		this.delegate = delegate;	
	}
	
	@Override
	public List<MixtureComponent> getComponents() {
		return delegate.getComponents();
	}

	@Override
	public String getOreDictKey() {
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
	}

	@Override
	public ConditionProperties getProperties(Condition condition) {
		return delegate.getProperties(condition);
	}

	@Override
	public Mixture mix(IndustrialMaterial material, double weight) {
		return delegate.mix(material, weight);
	}
}
