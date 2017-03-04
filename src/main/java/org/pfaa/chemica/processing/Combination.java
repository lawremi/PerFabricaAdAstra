package org.pfaa.chemica.processing;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.MaterialState;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.State;

import com.google.common.collect.Lists;

public class Combination extends ConditionedConversion implements MassTransfer {
	private List<MaterialStoich<?>> inputs = Lists.newArrayList();
	private List<MaterialStoich<?>> outputs = Lists.newArrayList();
	private double energy;
	
	public Combination with(List<MaterialStoich<?>> inputs) {
		this.inputs.addAll(inputs);
		return this;
	}
	
	public Combination with(float stoich, IndustrialMaterial input) {
		return this.with(MaterialStoich.of(stoich, input));
	}
	
	public Combination with(MaterialStoich<?>... inputs) {
		this.with(Arrays.asList(inputs));
		return this;
	}

	public Combination with(IndustrialMaterial... inputs) {
		this.inputs.addAll(MaterialStoich.of(inputs));
		return this;
	}

	public Combination yields(List<MaterialStoich<?>> outputs) {
		this.outputs.addAll(outputs);
		return this;
	}
	
	public Combination yields(MaterialStoich<?> output) {
		this.outputs.add(output);
		return this;
	}

	public Combination yields(IndustrialMaterial output) {
		return this.yields(MaterialStoich.of(output));
	}
	
	public Combination yields(MaterialState<?> output) {
		return this.yields(MaterialStoich.of(output));
	}
	
	@Override
	public List<MaterialStoich<?>> getInputs() {
		return Collections.unmodifiableList(this.inputs);
	}

	@Override
	public List<MaterialStoich<?>> getOutputs() {
		return Collections.unmodifiableList(this.outputs);
	}

	@Override
	public Combination at(Condition condition) {
		this.at(condition);
		return this;
	}
	
	@Override
	public Form getOutputForm(Form inputForm) {
		return inputForm.isGranular() ? inputForm : null;
	}
	
	@Override
	public double getEnergy() {
		return this.energy;
	}
	
	public Combination given(double energy) {
		this.energy = energy;
		return this;
	}

	public static Combination of(List<MaterialStoich<?>> inputs) {
		return new Combination().with(inputs);
	}
	
	public static Combination of(IndustrialMaterial... inputs) {
		return new Combination().with(inputs);
	}
	
	public static Combination of(MaterialStoich<?>... inputs) {
		return new Combination().with(inputs);
	}
	
	public static Combination yielding(Mixture output) {
		return yielding(output.getStandardState().of(output));
	}
	
	public static Combination yielding(MaterialState<? extends Mixture> output) {
		List<MaterialStoich<?>> stoichs = output.material.discretize().getComponents().stream().
				map((comp) -> MaterialStoich.of(comp, output.state)).
				collect(Collectors.toList());
		return of(stoichs).yields(output);
	}
	
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public interface Type extends MassTransfer.Type { 
		public State getContinuousState();
		public State getDispersedState();
		
		// TODO: If continuous phase is water, and material is soluble, yield aqueous state.
		public static enum SimpleTypes implements Type {
			GAS_MIXING(State.GAS, State.GAS),
			LIQUID_MIXING(State.LIQUID, State.LIQUID),
			SOLID_MIXING(State.SOLID, State.SOLID),
			GAS_ABSORPTION(State.LIQUID, State.GAS),
			LIQUID_ABSORPTION(State.SOLID, State.LIQUID),
			GAS_FLUIDIZATION(State.GAS, State.SOLID),
			LIQUID_FLUIDIZATION(State.LIQUID, State.SOLID)
			;

			private State continuousState, dispersedState;
			
			private SimpleTypes(State continuousState, State dispersedState) {
				this.continuousState = continuousState;
				this.dispersedState = dispersedState;
			}

			@Override
			public State getContinuousState() {
				return this.continuousState;
			}

			@Override
			public State getDispersedState() {
				return this.dispersedState;
			}			
		}

	}
}
