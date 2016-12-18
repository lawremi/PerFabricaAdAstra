package org.pfaa.chemica.processing;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;

public class Separation extends MassTransfer<SeparationType> {
	
	private MaterialStoich<Mixture> input;
	private MaterialStoich<?> agent;
	private MaterialStoich<Mixture> separated, residuum;
	
	protected Separation(SeparationType type, MaterialStoich<Mixture> input) {
		super(type);
		this.input = input;
	}
	
	public MaterialStoich<Mixture> getInput() {
		return this.input;
	}

	public MaterialStoich<?> getAgent() {
		return this.agent;
	}
	
	public MaterialStoich<Mixture> getResiduum() {
		return this.residuum;
	}
	public MaterialStoich<Mixture> getSeparated() {
		return this.separated;
	}
	
	public Separation with(MaterialStoich<?> agent) {
		this.agent = agent;
		return this;
	}

	public Separation with(IndustrialMaterial agent) {
		return this.with(MaterialStoich.of(agent));
	}

	public Separation extracts(IndustrialMaterial... outputs) {
		Mixture separated = this.input.material().extract(outputs);
		MaterialStoich<?> agent = this.getAgent();
		if (agent != null) {
			separated = separated.mix(agent.material(), 1.0);
		}
		this.separated = this.getType().getSeparatedState().
				of(separated).
				times(this.input.stoich);
		this.residuum = this.getType().getResidualState().
				of(this.input.material().without(outputs)).
				times(this.input.stoich);
		return this;
	}

	@Override
	public List<MaterialStoich<?>> getInputs() {
		return Collections.singletonList(this.input);
	}

	@Override
	public List<MaterialStoich<?>> getOutputs() {
		return Arrays.asList(this.separated, this.residuum);
	}

	@Override
	public double getEnergy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected Condition deriveCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Separation at(Condition condition) {
		return (Separation)super.at(condition);
	}

	@Override
	public Separation at(int temp) {
		return (Separation)super.at(temp);
	}
}
