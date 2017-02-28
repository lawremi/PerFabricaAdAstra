package org.pfaa.chemica.processing;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.MaterialState;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.model.Transition;
import org.pfaa.chemica.processing.Form.Forms;

/* Currently, we only model state changes */
public class EnthalpyChange implements UnitOperation {

	private IndustrialMaterial material;
	private Type type;
	
	public EnthalpyChange(IndustrialMaterial material) {
		this.material = material;
	}

	public IndustrialMaterial getMaterial() {
		return this.material;
	}

	public MaterialState<?> getInput() {
		return this.type.getInputState().of(this.material);
	}
	
	public MaterialState<?> getOutput() {
		return this.type.getOutputState().of(this.material);
	}

	@Override
	public List<MaterialStoich<?>> getInputs() {
		return Collections.singletonList(MaterialStoich.of(this.getInput()));
	}

	@Override
	public List<MaterialStoich<?>> getOutputs() {
		return Collections.singletonList(MaterialStoich.of(this.getOutput()));
	}

	@Override
	public double getEnergy() {
		return this.material.getEnthalpyChange(this.type.getInputState(), this.type.getOutputState());
	}

	@Override
	public Condition getCondition() {
		Transition transition;
		if (this.type.getInputState() == State.GAS || this.type.getOutputState() == State.GAS) {
			transition = this.material.getVaporization();
		} else {
			transition = this.material.getFusion();
		}
		return transition == null ? null : transition.getCondition();
	}

	@Override
	public Stream<Form> getOutputForms(Form inputForm) {
		Type type = this.getType();
		if (type.getAxis() != Type.Axis.STATE) {
			return Stream.empty();
		} 
		if (type.getOutputState() == State.SOLID) {
			Set<Form> forms = CanonicalForms.of(this.getMaterial());
			Form irregular = forms.contains(Forms.NUGGET) ? Forms.NUGGET : Forms.DUST_TINY; 
			return Stream.concat(Stream.of(irregular), forms.stream().filter(Form::isRegular));
		}
		return Stream.of(inputForm);
	}

	@Override
	public Type getType() {
		return this.type;
	}
	
	public static EnthalpyChange of(IndustrialMaterial material) {
		return new EnthalpyChange(material);
	}
	
	public EnthalpyChange melts() {
		this.type = Types.MELTING;
		return this;
	}
	
	public EnthalpyChange vaporizes() {
		this.type = Types.VAPORIZATION;
		return this;
	}

	public EnthalpyChange condenses() {
		this.type = Types.CONDENSATION;
		return this;
	}
	
	public EnthalpyChange freezes() {
		this.type = Types.FREEZING;
		return this;
	}
	
	public static interface Type extends UnitOperation.Type {
		enum Direction {
			GAIN,
			LOSS;
		}
		enum Axis {
			TEMPERATURE,
			PRESSURE,
			STATE;
		}
		
		public Type.Axis getAxis();
		public State getInputState();
		public State getOutputState();
		public Type.Direction getDirection();
	}
	
	public static enum Types implements Type {
		GAS_HEATING(Axis.TEMPERATURE, State.GAS, Direction.GAIN, State.GAS),
		GAS_COOLING(Axis.TEMPERATURE, State.GAS, Direction.LOSS, State.GAS),
		LIQUID_HEATING(Axis.TEMPERATURE, State.LIQUID, Direction.GAIN, State.LIQUID),
		LIQUID_COOLING(Axis.TEMPERATURE, State.LIQUID, Direction.LOSS, State.LIQUID),
		SOLID_HEATING(Axis.TEMPERATURE, State.SOLID, Direction.GAIN, State.SOLID),
		SOLID_COOLING(Axis.TEMPERATURE, State.SOLID, Direction.LOSS, State.SOLID),
		
		COMPRESSION(Axis.PRESSURE, State.GAS, Direction.GAIN, State.GAS),
		DECOMPRESSION(Axis.PRESSURE, State.GAS, Direction.LOSS, State.GAS),
		
		CONDENSATION(Axis.STATE, State.GAS, Direction.LOSS, State.LIQUID),
		VAPORIZATION(Axis.STATE, State.LIQUID, Direction.GAIN, State.GAS),
		FREEZING(Axis.STATE, State.LIQUID, Direction.LOSS, State.SOLID),
		MELTING(Axis.STATE, State.SOLID, Direction.GAIN, State.LIQUID),
		SUBLIMATION(Axis.STATE, State.SOLID, Direction.GAIN, State.GAS),
		DESUBLIMATION(Axis.STATE, State.GAS, Direction.LOSS, State.SOLID);
	
		private State inputState, outputState;
		private Direction heatChange;
		private Axis axis;
		
		private Types(Axis axis, State inputState, Direction heatChange, State outputState) {
			this.axis = axis;
			this.inputState = inputState;
			this.heatChange = heatChange;
			this.outputState = outputState;
		}
		
		@Override
		public Axis getAxis() {
			return this.axis;
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
		public Direction getDirection() {
			return this.heatChange;
		}
	}
}
