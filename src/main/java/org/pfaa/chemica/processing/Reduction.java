package org.pfaa.chemica.processing;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.pfaa.chemica.model.Mixture;

public class Reduction extends Communition implements Sizing {

	private Mixture input;
	
	private Reduction(Mixture input) {
		super(input);
		this.input = input;
	}
	
	@Override
	public Direction getDirection() {
		return Direction.DECREASE;
	}

	@Override
	public Form getOutputForm(Form inputForm) {
		return inputForm.reduce();
	}

	@Override
	public List<MaterialStoich<?>> getInputs() {
		return Collections.singletonList(MaterialStoich.of(this.input));
	}

	@Override
	public List<MaterialStoich<?>> getOutputs() {
		return this.input.getComponents().stream().map(MaterialStoich::of).collect(Collectors.toList());
	}

	public static Reduction of(Mixture mixture) {
		return new Reduction(mixture);
	}

}
