package org.pfaa.chemica.processing;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.MaterialState;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.SimpleMixture;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.processing.Form.Forms;

import com.google.common.collect.Lists;

public class Separation extends ConditionedConversion implements MassTransfer {
	private MaterialState<Mixture> input;
	private MaterialState<?> agent;
	private MaterialState<Mixture> residuum;
	private List<MixtureComponent> separated = Lists.newArrayList();
	private Type type;
	private double energy;
	
	protected Separation(MaterialState<Mixture> input) {
		this.input = input;
		this.residuum = input;
	}
	
	public MaterialState<Mixture> getInput() {
		return this.input;
	}

	public MaterialState<?> getAgent() {
		return this.agent;
	}
	
	public MaterialState<Mixture> getResiduum() {
		return this.residuum;
	}
	
	public Mixture getSeparatedMixture(int i) {
		MixtureComponent comp = this.separated.get(i); 
		return comp.material instanceof Mixture ? (Mixture)comp.material : new SimpleMixture(comp);
	}
	
	public Separation with(MaterialState<?> agent) {
		this.agent = agent;
		return this;
	}
	
	public Separation with(IndustrialMaterial agent) {
		this.agent = MaterialState.of(null, agent);
		return this;
	}
	
	private Separation separate(Mixture separated, Mixture residuum) {
		IndustrialMaterial agent = this.agent.material;
		if (agent != null && this.getType().getSeparatedState() == this.getType().getAgentState()) {
			separated = separated.mix(agent, 1.0);
		}
		this.separated.add(new MixtureComponent(separated, 1.0));
		this.residuum = this.residuum.state.of(residuum);
		return this;
	}
	
	public Separation extracts(IndustrialMaterial... outputs) {
		return this.separate(this.residuum.material.extract(outputs), this.residuum.material.without(outputs));
	}

	public Separation extractsAll() {
		this.residuum.material.getComponents().stream().forEach(this.separated::add);
		this.residuum = this.residuum.state.of(this.residuum.material.removeAll());
		return this;
	}
	
	public Separation extractsAllExcept(IndustrialMaterial... outputs) {
		return this.separate(this.residuum.material.without(outputs), this.residuum.material.extract(outputs));
	}
	
	public Separation by(Type type) {
		if (this.input.state == null)
			this.input = type.getInputState().of(this.input.material);
		if (this.agent.state == null)
			this.agent = type.getAgentState().of(this.agent.material);
		this.type = type;
		return this;
	}
	
	public Separation by(Axis axis) {
		Optional<Types> maybeType = Types.find(
				axis, 
				this.input.state, 
				this.agent != null ? this.agent.state : null, 
				this.separated.get(0).material.getStandardState());
		if (maybeType.isPresent()) {
			return this.by(maybeType.get());
		} else {
			throw new IllegalArgumentException("Cannot find type for axis: " + axis);
		}
	}
	
	@Override
	public List<MaterialStoich<?>> getInputs() {
		List<MaterialStoich<?>> inputs = Arrays.asList(MaterialStoich.of(this.input));
		if (this.agent != null) {
			inputs.add(MaterialStoich.of(this.agent));
		}
		return inputs;
	}

	@Override
	public List<MaterialStoich<?>> getOutputs() {
		return Stream.concat(this.separated.stream().
				map((comp) -> MaterialStoich.of((float)comp.weight, this.inferSeparatedState(), comp.material)),
				Stream.of(MaterialStoich.of(this.residuum))).collect(Collectors.toList());
	}

	private State inferSeparatedState() {
		MaterialState<?> source = this.agent == null ? this.input : this.agent;
		if (source.state.ofMatter() == this.type.getSeparatedState()) {
			return source.state;
		}
		return this.type.getSeparatedState();
	}

	public Separation given(double energy) {
		this.energy = energy;
		return this;
	}
	
	@Override
	public double getEnergy() {
		return this.energy;
	}

	@Override
	protected Condition deriveCondition() {
		Condition condition = null;
		if (this.type.getSeparationAxis() == Axis.VAPORIZATION_POINT) {
			condition = this.separated.get(this.separated.size()-1).material.getVaporization().getCondition();
		}
		return condition;
	}

	@Override
	public Separation at(Condition condition) {
		return (Separation)super.at(condition);
	}

	@Override
	public Separation at(int temp) {
		return (Separation)super.at(temp);
	}

	@Override
	public Type getType() {
		return this.type;
	}

	@Override
	public Form getOutputForm(Form inputForm) {
		if (inputForm == Forms.MILLIBUCKET)
			return super.getOutputForm(inputForm);
		if (inputForm.isGranular())
			return inputForm;
		/*
		 * Handles 'ore' case when the material itself is granular, like a sand.
		 * It might make sense to use a compound form like oreSand,
		 * but at the very least that would break compatibility. 
		 */
		if (this.input.material.isGranular())
			return Forms.DUST;
		return null;
	}

	public static Separation of(MaterialState<Mixture> mixture) {
		return new Separation(mixture);
	}
	
	public static Separation of(Mixture mixture) {
		return new Separation(MaterialState.of(null, mixture));
	}
	
	public static enum Axis {
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

	public static interface Type extends MassTransfer.Type { 		
		Axis getSeparationAxis();
		
		State getInputState();
		State getAgentState();
		State getSeparatedState();
	}

	/* The "aqueous" problem:
	 * the states of the types refer to the states of _matter_,
	 * so the "aqueous" state is irrelevant. This means that the separation type cannot adequately represent the states
	 * of the chemicals involved in the separation, because the "aqueous" state is lost.
	 * 
	 * Solutions:
	 * - Track the aqueous state within the Separation.
	 *   - Simplest would be to store MaterialState's for the input and the agent, and the agent (or input if none) state would imply
	 *     the output state (as long as their physical state is the same). Water, as an agent, would need to be an empty aqueous mixture.
	 * - Infer the aqueous state based on the conditions. This is fraught with peril.
	 */
	public static enum Types implements Type {
		// These state transitions can obviously occur by either temperature or pressure changes
		CONDENSATION(Axis.VAPORIZATION_POINT, State.GAS, State.LIQUID),
		VAPORIZATION(Axis.VAPORIZATION_POINT, State.LIQUID, State.GAS),
		DESUBLIMATION(Axis.VAPORIZATION_POINT, State.GAS, State.SOLID),
		SUBLIMATION(Axis.VAPORIZATION_POINT, State.SOLID, State.GAS),
		
		// Distillation combines vaporization and condensation steps, but we model it as one
		DISTILLATION(Axis.VAPORIZATION_POINT, State.LIQUID, State.LIQUID),
		CRYOGENIC_DISTILLATION(Axis.VAPORIZATION_POINT, State.GAS, State.GAS),
		EVAPORATION(Axis.VAPORIZATION_POINT, State.SOLID, State.GAS),
		FREEZING(Axis.MELTING_POINT, State.LIQUID, State.SOLID),
		MELTING(Axis.MELTING_POINT, State.SOLID, State.LIQUID),
		
		ABSORPTION(Axis.SOLUBILITY, State.GAS, State.LIQUID, State.LIQUID),
		STRIPPING(Axis.SOLUBILITY, State.LIQUID, State.GAS, State.GAS),
		LIQUID_LIQUID_EXTRACTION(Axis.SOLUBILITY, State.LIQUID, State.LIQUID, State.LIQUID),
		LEACHING(Axis.SOLUBILITY, State.SOLID, State.LIQUID, State.LIQUID),
		PRECIPITATION(Axis.SOLUBILITY, State.LIQUID, State.SOLID),
		LIQUID_PRECIPITATION(Axis.SOLUBILITY, State.LIQUID, State.LIQUID),
		DEGASIFICATION(Axis.SOLUBILITY, State.LIQUID, State.GAS),
		/* Froth flotation: 
		 * Concentrating sulfide ores by froth flotation requires first adsorbing the sulfide mineral
		 * to a "collector", so that the complex has a non-polar surface and thus is floatable.
		 */
		FLOTATION(Axis.SOLUBILITY, State.LIQUID, State.GAS, State.SOLID),
		
		LIQUID_ADSORPTION(Axis.ADSORPTIVITY, State.LIQUID, State.LIQUID, State.LIQUID),
		GAS_ADSORPTION(Axis.ADSORPTIVITY, State.GAS, State.LIQUID, State.LIQUID),
		LIQUID_CHROMATOGRAPHY(Axis.ADSORPTIVITY, State.LIQUID, State.LIQUID),
		GAS_CHROMATOGRAPHY(Axis.ADSORPTIVITY, State.GAS, State.GAS),
		PRESSURE_SWING_ADSPORPTION(Axis.ADSORPTIVITY, State.GAS, State.GAS),
		
		/* Molecular filters via pressure differential across membrane */
		REVERSE_OSMOSIS(Axis.MOLECULAR_SIZE, State.LIQUID, State.LIQUID),
		GAS_PERMEATION(Axis.MOLECULAR_SIZE, State.GAS, State.GAS),
		
		/* Cyclones, spirals, etc */
		GAS_GRAVITY(Axis.DENSITY, State.GAS, State.GAS),
		LIQUID_GRAVITY(Axis.DENSITY, State.LIQUID, State.LIQUID),
		/* Jigs, shaking tables */
		SOLID_GRAVITY(Axis.DENSITY, State.SOLID, State.SOLID),
		/* Vapor-liquid separation vessels */
		LIQUID_FROM_GAS(Axis.DENSITY, State.GAS, State.LIQUID),
		GAS_FROM_LIQUID(Axis.DENSITY, State.LIQUID, State.GAS),
		SEDIMENTATION(Axis.DENSITY, State.LIQUID, State.SOLID),
		
		LIQUID_FILTRATION(Axis.PARTICLE_SIZE, State.LIQUID, State.SOLID),
		GAS_FILTRATION(Axis.PARTICLE_SIZE, State.GAS, State.SOLID),
		
		ELECTROSTATIC(Axis.CONDUCTIVITY, State.SOLID, State.SOLID),
		MAGNETIC(Axis.MAGNETIC_SUSCEPTIBILITY, State.SOLID, State.SOLID);
		
		private Axis separationAxis;	
		private State inputState, agentState, separatedState;
		
		private Types(Axis separationAxis, State inputState) {
			this(separationAxis, inputState, inputState);
		}
		
		private Types(Axis separationAxis, State inputState, State separatedState) {
			this(separationAxis, inputState, null, separatedState);
		}
		
		private Types(Axis separationAxis,
				State inputState, State agentState, State separatedState) {
			this.separationAxis = separationAxis;
			this.inputState = inputState;
			this.agentState = agentState;
			this.separatedState = separatedState;
		}
		
		@Override
		public Axis getSeparationAxis() {
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
		public State getAgentState() {
			return this.agentState;
		}
		
		public static Optional<Types> find(Separation.Axis axis, State input, State agent, State separated) {
			return Arrays.stream(values()).filter((t) -> 
					t.separationAxis == axis && 
					t.inputState == input && 
					t.agentState == agent && 
					t.separatedState == separated).
					findFirst();
		}
	}
}
