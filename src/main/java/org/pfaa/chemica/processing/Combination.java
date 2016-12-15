package org.pfaa.chemica.processing;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.State;

public class Combination extends MassTransfer {
	protected Combination(Type type, MaterialSpec<?> input) {
		super(type, input);
	}

	public Combination with(MaterialSpec<?>... inputs) {
		super.with(inputs);
		return this;
	}

	public Combination with(IndustrialMaterial... inputs) {
		super.with(inputs);
		return this;
	}

	public Combination yields(MaterialSpec<?>... outputs) {
		super.yields(outputs);
		return this;
	}

	public Combination yields(IndustrialMaterial... outputs) {
		super.yields(outputs);
		return this;
	}
	
	public Combination at(int temp) {
		super.at(temp);
		return this;
	}
	
	public Combination given(int energy) {
		super.given(energy);
		return this;
	}

	public static interface CombinationType extends MassTransferType { 
		public State getContinuousState();
		public State getDispersedState();
	}

	// TODO: If continuous phase is water, and material is soluble, yield aqueous state.
	public static enum CombinationTypes implements CombinationType {
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

		@Override
		public Combination of(MaterialSpec<?> stream) {
			return new Combination(this, stream);
		}
		
	}

}
