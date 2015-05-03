package org.pfaa.chemica.processing;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.State;

public interface UnitOperation extends Step {

	public UnitProcess getUnitProcess();
	
	public static interface Type {
		public UnitOperation of(IndustrialMaterial material);
	}
	
	/*
	 * Proposal:
	 * 
	 * We will generate/declare unit operations that define the available means of manipulating, 
	 * transforming and producing materials. Another module will process the database of unit 
	 * operations and register recipes on machines, usually
	 * those implemented by other mods. Each mod would be represented by a plugin, which would
	 * visit each unit process, perhaps with a separate handler for each main type of operation, such
	 * as mixing, separation, phase change, etc. To make unit operations easier to compute, we will
	 * label them with a type. That type serves as a factory for new unit operation instances.
	 * Given a set of inputs (and perhaps outputs), some central factory might query each type for 
	 * a corresponding unit operation, and collect them into a list, which it returns.
	 * Or is that just reinventing OOP?
	 */
	
	/* Moving heat from one location/material to another. 
	 * While chemical engineering textbooks describe this in terms of a source and destination,
	 * we break heat transfer/exchange into two separate processes: removing and adding heat.
	 * Effects include simple heating and cooling, as well as state transitions. 
	 */
	public static interface HeatTransferType extends Type {
		public static enum HeatChange {
			GAIN,
			LOSS;
		}
		
		public State getInputState();
		public State getOutputState();
		public HeatChange getChange();
	}
	
	public static enum HeatTransferTypes implements HeatTransferType { 
		GAS_HEATING(State.GAS, HeatChange.GAIN, State.GAS),
		GAS_COOLING(State.GAS, HeatChange.LOSS, State.GAS),
		LIQUID_HEATING(State.LIQUID, HeatChange.GAIN, State.LIQUID),
		LIQUID_COOLING(State.LIQUID, HeatChange.LOSS, State.LIQUID),
		SOLID_HEATING(State.SOLID, HeatChange.GAIN, State.SOLID),
		SOLID_COOLING(State.SOLID, HeatChange.LOSS, State.SOLID),
		
		CONDENSATION(State.GAS, HeatChange.LOSS, State.LIQUID),
		VAPORIZATION(State.LIQUID, HeatChange.GAIN, State.GAS),
		FREEZING(State.LIQUID, HeatChange.LOSS, State.SOLID),
		MELTING(State.SOLID, HeatChange.GAIN, State.LIQUID);

		private State inputState, outputState;
		private HeatChange heatChange;
		
		private HeatTransferTypes(State inputState, HeatChange heatChange, State outputState) {
			this.inputState = inputState;
			this.heatChange = heatChange;
			this.outputState = outputState;
		}
		
		@Override
		public State getInputState() {
			return this.inputState;
		}

		@Override
		public State getOutputState() {
			return this.outputState;
		}

		@Override
		public HeatChange getChange() {
			return this.heatChange;
		}

		@Override
		public UnitOperation of(IndustrialMaterial material) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	/* Moving one material with respect to another; either mixing or separation */
	public static interface MassTransferType extends Type { }
	
	public static interface MixingType extends MassTransferType { 
		public State getContinuousState();
		public State getDispersedState();
	}
	
	public static enum MixingTypes implements MixingType {
		GAS(State.GAS, State.GAS),
		LIQUID(State.LIQUID, State.LIQUID),
		SOLID(State.SOLID, State.SOLID),
		GAS_ABSORPTION(State.LIQUID, State.GAS),
		LIQUID_ABSORPTION(State.SOLID, State.LIQUID),
		SOLID_INTO_LIQUID(State.LIQUID, State.SOLID), // often dissolution
		FLUIDIZATION(State.GAS, State.SOLID)
		;

		private State continuousState, dispersedState;
		
		private MixingTypes(State continuousState, State dispersedState) {
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
		public UnitOperation of(IndustrialMaterial material) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public static interface SeparationType extends MassTransferType { 
		public static enum SeparationAxis {
			DENSITY,
			MOLECULAR_SIZE,
			PARTICLE_SIZE,
			VAPORIZATION_POINT,
			MELTING_POINT,
			SOLUBILITY,
			ADSORPTIVITY,
			CONDUCTIVITY,
			MAGNETIC_SUSCEPTIBILITY
			;
		}
		
		public SeparationAxis getSeparationAxis();
		
		public State getInputState();
		public State getAddedState();
		public State getSeparatedState();
		public State getResidualState();
	}
	
	/*
	 * Most of these techniques imply phase segregation, i.e., they yield
	 * separate streams, not just a heterogeneous (multi-phase) mixture. That often but
	 * not always requires a (mechanical) segregation step that is so trivial as to not warrant
	 * explicit description. Thus, it usually makes sense to describe a separation as going
	 * from a single input stream to multiple output streams, but we will still provide
	 * operations for mechanical phase segregation. A notable exception is solid + liquid
	 * mixtures; precipitating a solid is not enough -- it must be dried. Thus, the solid is
	 * always a mixture with some liquid.  
	*/
	
	public static enum SeparationTypes implements SeparationType {
		// These state transitions can obviously occur by either temperature or pressure changes
		PARTIAL_CONDENSATION(SeparationAxis.VAPORIZATION_POINT, State.GAS, State.LIQUID),
		PARTIAL_VAPORIZATION(SeparationAxis.VAPORIZATION_POINT, State.LIQUID, State.GAS),
		PARTIAL_DESUBLIMATION(SeparationAxis.VAPORIZATION_POINT, State.GAS, State.SOLID),
		PARTIAL_SUBLIMATION(SeparationAxis.VAPORIZATION_POINT, State.SOLID, State.GAS),
		// Distillation combines vaporization and condensation steps, but we model it as one
		DISTILLATION(SeparationAxis.VAPORIZATION_POINT, State.LIQUID, State.LIQUID),
		DRYING(SeparationAxis.VAPORIZATION_POINT, State.LIQUID, null, State.SOLID, State.GAS),
		PARTIAL_FREEZING(SeparationAxis.MELTING_POINT, State.LIQUID, State.SOLID),
		PARTIAL_MELTING(SeparationAxis.MELTING_POINT, State.SOLID, State.LIQUID),
		
		ABSORPTION(SeparationAxis.SOLUBILITY, State.GAS, State.LIQUID, State.LIQUID, State.GAS),
		STRIPPING(SeparationAxis.SOLUBILITY, State.LIQUID, State.GAS, State.GAS, State.LIQUID),
		LIQUID_LIQUID_EXTRACTION(SeparationAxis.SOLUBILITY, State.LIQUID, State.LIQUID, State.LIQUID, State.LIQUID),
		LEACHING(SeparationAxis.SOLUBILITY, State.SOLID, State.LIQUID, State.LIQUID, State.SOLID),
		PRECIPITATION(SeparationAxis.SOLUBILITY, State.LIQUID, State.SOLID),
		/* Froth flotation: 
		 * Concentrating sulfide ores by froth flotation requires first adsorbing the sulfide mineral
		 * to a "collector", so that the complex has a non-polar surface and thus is floatable.
		 */
		FLOTATION(SeparationAxis.SOLUBILITY, State.LIQUID, State.GAS, State.SOLID, State.LIQUID),
		
		LIQUID_ADSORPTION(SeparationAxis.ADSORPTIVITY, State.LIQUID, State.LIQUID, State.LIQUID, State.LIQUID),
		GAS_ADSORPTION(SeparationAxis.ADSORPTIVITY, State.GAS, State.LIQUID, State.LIQUID, State.GAS),
		LIQUID_CHROMATOGRAPHY(SeparationAxis.ADSORPTIVITY, State.LIQUID, State.LIQUID),
		GAS_CHROMATOGRAPHY(SeparationAxis.ADSORPTIVITY, State.GAS, State.GAS),
		
		/* Molecular filters via pressure differential across membrane */
		REVERSE_OSMOSIS(SeparationAxis.MOLECULAR_SIZE, State.LIQUID, State.LIQUID),
		GAS_PERMEATION(SeparationAxis.MOLECULAR_SIZE, State.GAS, State.GAS),
		
		/* Cyclones, spirals, etc */
		GAS_GRAVITY(SeparationAxis.DENSITY, State.GAS, State.GAS),
		LIQUID_GRAVITY(SeparationAxis.DENSITY, State.LIQUID, State.LIQUID),
		/* Jigs, shaking tables */
		SOLID_GRAVITY(SeparationAxis.DENSITY, State.SOLID, State.SOLID),
		
		/* Vapor-liquid separation vessels */
		LIQUID_FROM_GAS(SeparationAxis.DENSITY, State.GAS, State.LIQUID),
		GAS_FROM_LIQUID(SeparationAxis.DENSITY, State.LIQUID, State.GAS),
		
		/* Sedimentation, followed by physical phase separation */
		LIQUID_DECANTATION(SeparationAxis.DENSITY, State.LIQUID, State.LIQUID),
		SEDIMENTARY_DECANTATION(SeparationAxis.DENSITY, State.LIQUID, State.SOLID),
		
		LIQUID_FILTRATION(SeparationAxis.PARTICLE_SIZE, State.LIQUID, State.SOLID),
		GAS_FILTRATION(SeparationAxis.PARTICLE_SIZE, State.GAS, State.SOLID),
		
		ELECTROSTATIC(SeparationAxis.CONDUCTIVITY, State.SOLID, State.SOLID),
		MAGNETIC(SeparationAxis.MAGNETIC_SUSCEPTIBILITY, State.SOLID, State.SOLID);
		
		private SeparationAxis separationAxis;	
		private State inputState, addedState, separatedState, residualState;
		
		private SeparationTypes(SeparationAxis separationAxis, State inputState) {
			this(separationAxis, inputState, inputState);
		}
		
		private SeparationTypes(SeparationAxis separationAxis, State inputState, State separatedState) {
			this(separationAxis, inputState, null, separatedState, inputState);
		}
		
		private SeparationTypes(SeparationAxis separationAxis,
				State inputState, State addedState, State separatedState, State residualState) {
			this.separationAxis = separationAxis;
			this.inputState = inputState;
			this.addedState = addedState;
			this.separatedState = separatedState;
			this.residualState = residualState;
		}
		
		@Override
		public SeparationAxis getSeparationAxis() {
			return this.separationAxis;
		}
		
		@Override
		public State getInputState() {
			return this.inputState;
		}

		@Override
		public State getSeparatedState() {
			return this.separatedState;
		}

		@Override
		public State getResidualState() {
			return this.residualState;
		}

		@Override
		public State getAddedState() {
			return this.addedState;
		}

		@Override
		public UnitOperation of(IndustrialMaterial material) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	/* Mechanical modification of particle size or pressure */
	public static interface MechanicalType extends Type { }
	
	public static enum MechanicalTypes implements MechanicalType {
		COMPACTION,
		COMMUNITION,
		COMPRESSION,
		DECOMPRESSION;

		@Override
		public UnitOperation of(IndustrialMaterial material) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
