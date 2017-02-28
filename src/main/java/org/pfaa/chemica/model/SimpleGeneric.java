package org.pfaa.chemica.model;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class SimpleGeneric implements Generic {

	private String name;
	private ImmutableList<IndustrialMaterial> specifics;

	public SimpleGeneric(String name, IndustrialMaterial... materials) {
		this(name, Arrays.asList(materials));
	}
	
	public SimpleGeneric(String name, List<IndustrialMaterial> materials) {
		this.name = name;
		this.specifics = ImmutableList.copyOf(materials);
	}
	
	@Override
	public ConditionProperties getProperties(Condition condition) {
		return this.specifics.get(0).getProperties(condition);
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public ImmutableList<IndustrialMaterial> getSpecifics() {
		return this.specifics;
	}

}
