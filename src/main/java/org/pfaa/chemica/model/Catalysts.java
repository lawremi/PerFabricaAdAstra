package org.pfaa.chemica.model;

import java.util.List;

import org.pfaa.chemica.model.Compound.Compounds;

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
	public ConditionProperties getProperties(Condition condition) {
		return delegate.getProperties(condition);
	}

	@Override
	public Mixture mix(IndustrialMaterial material, double weight) {
		return delegate.mix(material, weight);
	}

	@Override
	public Mixture removeAll() {
		return delegate.removeAll();
	}
}
