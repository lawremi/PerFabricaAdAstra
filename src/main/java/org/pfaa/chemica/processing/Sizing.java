package org.pfaa.chemica.processing;

import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.IndustrialMaterial;

import com.google.common.collect.Lists;

public class Sizing extends UnitOperation<SizingType> {

	private MaterialStoich<IndustrialMaterial> input;
	private MaterialStoich<IndustrialMaterial> output;
	private List<MaterialStoich<IndustrialMaterial>> secondaries;
	
	protected Sizing(SizingType type, MaterialStoich<IndustrialMaterial> input) {
		super(type);
		this.input = input;
	}

	public MaterialStoich<?> getInput() {
		return this.input;
	}
	
	public MaterialStoich<?> getOutput() {
		return this.output;
	}

	public Sizing yields(MaterialStoich<IndustrialMaterial> output, List<MaterialStoich<IndustrialMaterial>> secondaries) {
		 this.output = output;
		 this.secondaries = Lists.newArrayList(secondaries);
		 return this;
	}
	
	public List<MaterialStoich<?>> getSecondaries() {
		return Collections.unmodifiableList(this.secondaries);
	}

	@Override
	public List<MaterialStoich<?>> getInputs() {
		return Collections.singletonList(this.input);
	}

	@Override
	public List<MaterialStoich<?>> getOutputs() {
		List<MaterialStoich<?>> outputs = Lists.newArrayList(this.output);
		outputs.addAll(this.secondaries);
		return outputs;
	}

	@Override
	public double getEnergy() {
		// TODO Get from strength of material
		return 0;
	}

	@Override
	public Condition getCondition() {
		return Condition.STP;
	}
}
