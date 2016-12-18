package org.pfaa.chemica.processing;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.IndustrialMaterial;

import com.google.common.collect.Lists;

public class Combination extends MassTransfer<CombinationType> {
	private List<MaterialStoich<?>> inputs = Lists.newArrayList();
	private MaterialStoich<?> output;
	
	protected Combination(CombinationType type, MaterialStoich<?>... materialStoichs) {
		super(type);
		this.with(materialStoichs);
	}
	
	public Combination with(MaterialStoich<?>... inputs) {
		this.inputs.addAll(Arrays.asList(inputs));
		return this;
	}

	public Combination with(IndustrialMaterial... inputs) {
		this.inputs.addAll(MaterialStoich.of(inputs));
		return this;
	}

	public Combination yields(MaterialStoich<?> output) {
		this.output = output;
		return this;
	}

	public Combination yields(IndustrialMaterial output) {
		return this.yields(MaterialStoich.of(output));
	}

	@Override
	public List<MaterialStoich<?>> getInputs() {
		return Collections.unmodifiableList(this.inputs);
	}

	@Override
	public List<MaterialStoich<?>> getOutputs() {
		return Collections.singletonList(this.output);
	}

	@Override
	public double getEnergy() {
		return 0;
	}

	@Override
	public Condition getCondition() {
		return Condition.STP;
	}
}
