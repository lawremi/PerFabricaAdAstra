package org.pfaa.chemica.registration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.pfaa.chemica.model.Reaction;
import org.pfaa.chemica.processing.Stacking;
import org.pfaa.chemica.processing.Combination;
import org.pfaa.chemica.processing.EnthalpyChange;
import org.pfaa.chemica.processing.Separation;
import org.pfaa.chemica.processing.Sizing;

public class CombinedConversionRegistry implements ConversionRegistry {

	private Map<String,ConversionRegistry> registries = new HashMap<String,ConversionRegistry>();
	
	public void putRegistry(String key, ConversionRegistry registry) {
		registries.put(key, registry);
	}
	
	public ConversionRegistry getRegistry(String key) {
		return registries.get(key);
	}
	
	public Set<String> getRegistryNames() {
		return Collections.unmodifiableSet(registries.keySet());
	}

	@Override
	public void register(Stacking agglomeration) {
		for (ConversionRegistry registry : registries.values()) {
			registry.register(agglomeration);
		}
	}

	@Override
	public void register(Separation separation) {
		for (ConversionRegistry registry : registries.values()) {
			registry.register(separation);
		}
	}

	@Override
	public void register(Combination combination) {
		for (ConversionRegistry registry : registries.values()) {
			registry.register(combination);
		}
		
	}

	@Override
	public void register(Sizing sizing) {
		for (ConversionRegistry registry : registries.values()) {
			registry.register(sizing);
		}		
	}

	@Override
	public void register(EnthalpyChange enthalpyChange) {
		for (ConversionRegistry registry : registries.values()) {
			registry.register(enthalpyChange);
		}
	}

	@Override
	public void register(Reaction reaction) {
		for (ConversionRegistry registry : registries.values()) {
			registry.register(reaction);
		}		
	}
}
