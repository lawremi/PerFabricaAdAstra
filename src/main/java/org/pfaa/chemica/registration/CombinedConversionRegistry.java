package org.pfaa.chemica.registration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.pfaa.chemica.model.Reaction;
import org.pfaa.chemica.processing.Alloying;
import org.pfaa.chemica.processing.Combination;
import org.pfaa.chemica.processing.Communition;
import org.pfaa.chemica.processing.Compaction;
import org.pfaa.chemica.processing.EnthalpyChange;
import org.pfaa.chemica.processing.Separation;
import org.pfaa.chemica.processing.Smelting;
import org.pfaa.chemica.processing.Stacking;

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
		registries.values().forEach((registry) -> registry.register(agglomeration));
	}

	@Override
	public void register(Separation separation) {
		registries.values().forEach((registry) -> registry.register(separation));
	}

	@Override
	public void register(Alloying alloying) {
		registries.values().forEach((registry) -> registry.register(alloying));
	}
	
	public void register(Smelting smelting) {
		registries.values().forEach((registry) -> registry.register(smelting));
	}
	
	@Override
	public void register(Combination combination) {
		registries.values().forEach((registry) -> registry.register(combination));
	}

	@Override
	public void register(Communition sizing) {
		registries.values().forEach((registry) -> registry.register(sizing));		
	}

	@Override
	public void register(Compaction sizing) {
		registries.values().forEach((registry) -> registry.register(sizing));		
	}

	@Override
	public void register(EnthalpyChange enthalpyChange) {
		registries.values().forEach((registry) -> registry.register(enthalpyChange));
	}

	@Override
	public void register(Reaction reaction) {
		registries.values().forEach((registry) -> registry.register(reaction));		
	}
}
