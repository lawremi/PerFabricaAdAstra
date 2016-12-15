package org.pfaa.chemica.processing;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.IndustrialMaterial;

import com.google.common.collect.Lists;

public class UnitOperation implements Conversion {

	private Type type;
	private double energy;
	private Condition condition;
	private List<MaterialSpec<?>> inputs;
	private List<MaterialSpec<?>> outputs;
	
	protected UnitOperation(Type type, MaterialSpec<?> input) {
		this(type, Arrays.asList(input));
	}
	
	protected UnitOperation(Type type, List<MaterialSpec<?>> inputs) {
		this(type, inputs, Collections.emptyList(), Condition.STP, 0);
	}
	
	private UnitOperation(Type type, List<MaterialSpec<?>> inputs, List<MaterialSpec<?>> outputs, 
			Condition condition, double energy) {
		this.type = type;
		this.inputs = Lists.newArrayList(inputs);
		this.outputs = Lists.newArrayList(outputs);
		this.condition = condition;
		this.energy = energy;
	}
	
	@Override
	public double getEnergy() {
		return this.energy;
	}
	
	public UnitOperation given(double energy) {
		this.energy = energy;
		return this;
	}
	
	@Override
	public List<MaterialSpec<?>> getInputs() {
		return Collections.unmodifiableList(this.inputs);
	}
	
	public UnitOperation with(MaterialSpec<?>... inputs) {
		this.inputs.addAll(Arrays.asList(inputs));
		return this;
	}

	public UnitOperation with(IndustrialMaterial... inputs) {
		this.inputs.addAll(Arrays.asList(inputs).stream().map(MaterialSpec::of).collect(Collectors.toList()));
		return this;
	}
	
	@Override
	public List<MaterialSpec<?>> getOutputs() {
		return Collections.unmodifiableList(this.outputs);
	}
	
	public UnitOperation yields(MaterialSpec<?>... outputs) {
		this.outputs.addAll(Arrays.asList(outputs));
		return this;
	}
	
	public UnitOperation yields(IndustrialMaterial... outputs) {
		this.outputs.addAll(Arrays.asList(outputs).stream().map(MaterialSpec::of).collect(Collectors.toList()));
		return this;
	}
	
	@Override
	public Condition getCondition() {
		return this.condition;
	}
	
	public UnitOperation at(int temp) {
		return this.at(new Condition(temp));
	}
	
	public UnitOperation at(Condition condition) {
		this.condition = condition;
		return this;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public static interface Type {
		default UnitOperation of(IndustrialMaterial material) {
			return this.of(MaterialSpec.of(material));
		}
		public UnitOperation of(MaterialSpec<?> stream);
	}	
}
