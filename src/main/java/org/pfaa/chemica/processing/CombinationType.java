package org.pfaa.chemica.processing;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.State;

public interface CombinationType extends MassTransferType<IndustrialMaterial, Combination> { 
	public State getContinuousState();
	public State getDispersedState();
	
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

		public Combination of(@SuppressWarnings("unchecked") MaterialStoich<IndustrialMaterial>... stoichs) {
			return new Combination(this, stoichs);
		}
		
		@Override
		public Combination of(MaterialStoich<IndustrialMaterial> stoich) {
			return new Combination(this, stoich);
		}
		
	}

}