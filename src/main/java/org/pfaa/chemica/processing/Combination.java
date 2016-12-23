package org.pfaa.chemica.processing;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.State;

import com.google.common.collect.Lists;

public class Combination extends ConditionedConversion implements MassTransfer {
	private List<MaterialStoich<?>> inputs = Lists.newArrayList();
	private List<MaterialStoich<?>> outputs = Lists.newArrayList();
	
	protected Combination(MaterialStoich<?>... materialStoichs) {
		this.with(materialStoichs);
	}
	
	protected Combination(List<MaterialStoich<?>> materialStoichs) {
		this.with(materialStoichs);
	}
	
	public Combination with(List<MaterialStoich<?>> inputs) {
		this.inputs.addAll(inputs);
		return this;
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

	@Override
	public List<MaterialStoich<?>> getInputs() {
		return Collections.unmodifiableList(this.inputs);
	}

	@Override
	public List<MaterialStoich<?>> getOutputs() {
		return this.outputs;
	}

	@Override
	public double getEnergy() {
		return 0;
	}

	@Override
	public Condition getCondition() {
		return Condition.STP;
	}
	
	@Override
	public Combination at(Condition condition) {
		this.at(condition);
		return this;
	}
	
	/*
	 * Allows a more intuitive syntax of e.g.
	 *   Mixture.of(Compounds.CO2).with(State.gas.of(Compounds.H2O))...
	 * Instead of having to specify the combination type, which is just state-based and thus redundant.
	 * We could compute the type dynamically.
	 * 
	 * That's generally true of most conversions: we already have the state of the inputs and outputs.
	 * 
	 * By beginning the expression with the participants, the code is easier to read, since we
	 * usually do not care about the actual type of conversion (it's implied). 
	 * 
	 *    EnthalpyChange.of(Compounds.H2O).boils()
	 *    Separation.of(mixture).with(x).yields(a, b).via(Separation.Axis.SOLUBILITY)
	 *    
	 * Since every class will override getType(), the overrides can specify the return value,
	 * and there is probably no need for generic types. All Type classes can be made internal again.
	 */
	
	public static Combination of(List<MaterialStoich<?>> inputs) {
		return new Combination(inputs);
	}
	
	public static Combination yielding(Mixture output) {
		return new Combination( 
				output.getComponents().stream().map(MaterialStoich::of).collect(Collectors.toList())).
				yields(output);
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
		public static enum CombinationTypes implements Type {
			GAS(State.GAS, State.GAS),
			LIQUID(State.LIQUID, State.LIQUID),
			SOLID(State.SOLID, State.SOLID),
			GAS_ABSORPTION(State.LIQUID, State.GAS),
			LIQUID_ABSORPTION(State.SOLID, State.LIQUID),
			SOLID_INTO_LIQUID(State.LIQUID, State.SOLID),
			FLUIDIZATION(State.GAS, State.SOLID)
			;

			private State continuousState, dispersedState;
			
			private CombinationTypes(State continuousState, State dispersedState) {
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
