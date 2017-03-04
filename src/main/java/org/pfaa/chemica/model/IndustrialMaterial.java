package org.pfaa.chemica.model;

import com.google.common.base.CaseFormat;

public interface IndustrialMaterial {
	String name();
	
	default String getOreDictKey() {
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
	}
	
	default Strength getStrength() { return null; }
	
	default StateProperties getStateProperties(State state) {
		return null;
	}
	default ConditionProperties getProperties(Condition condition, State state) {
		StateProperties stateProps = this.getStateProperties(state);
		if (stateProps != null) {
			Thermo adjacent = null;
			if (!stateProps.thermo.knowsHeatCapacity())
				adjacent = this.getStateProperties(this.getAdjacentState(condition, state)).thermo;
			return stateProps.at(condition, adjacent);
		} else {
			ConditionProperties props = this.getProperties(condition);
			return (props != null && props.state == state) ? props : null;
		}
	}
	default State getStateForCondition(Condition condition) {
		if (condition.aqueous && this.isSoluble(condition)) {
			return State.AQUEOUS;
		} else if (this.getVaporization() != null && 
				condition.temperature >= this.getVaporization().getTemperature(condition.pressure)) {
			return State.GAS;
		} else if (this.getFusion() != null && condition.temperature >= this.getFusion().getTemperature()) {
			return State.LIQUID;
		} else {
			return State.SOLID;
		}
	}
	default ConditionProperties getProperties(Condition condition) {
		State state = this.getStateForCondition(condition);
		return this.getProperties(condition, state);
	}
	default ConditionProperties getStandardProperties() {
		return this.getProperties(Condition.STP);
	}
	default State getStandardState() {
		return this.getStandardProperties().state;
	}
	
	default Fusion getFusion() {
		return null;
	}
	default Vaporization getVaporization() {
		return null;
	}
	default boolean isSoluble(Condition condition) {
		return false;
	}
	
	default Condition getCanonicalCondition(State state) {
		int temp = Constants.STANDARD_TEMPERATURE;
		switch(state) {
		case SOLID:
			if (this.getFusion() != null) {
				temp = Math.min(this.getFusion().getTemperature() - 1, Constants.STANDARD_TEMPERATURE);
			}
			if (this.getVaporization() != null) {
				temp = Math.min(this.getVaporization().getTemperature() - 1, Constants.STANDARD_TEMPERATURE);
			}
			break;
		case LIQUID:
			int lowerBound = Constants.STANDARD_TEMPERATURE;
			if (this.getVaporization() != null) {
				lowerBound = Math.min(lowerBound, this.getVaporization().getTemperature() - 1);
			}
			if (this.getFusion() != null) {
				temp = Math.max(this.getFusion().getTemperature() + 1, lowerBound);
			} else {
				return null;
			}
		case GAS:
			if (this.getVaporization() != null) {
				temp = Math.max(this.getVaporization().getTemperature() + 1, Constants.STANDARD_TEMPERATURE);
			} else {
				return null;
			}
		default:
		}
		
		return new Condition(temp, Constants.STANDARD_PRESSURE);
	}
		
	default double getEnthalpyChange(State to) {
		return this.getEnthalpyChange(this.getStandardState(), to);
	}
	
	default double getEnthalpyChange(Condition to) {
		return this.getEnthalpyChange(Condition.STP, to);
	}
	
	default double getEnthalpyChange(Condition from, Condition to) {
		ConditionProperties fromProps = this.getProperties(from);
		ConditionProperties toProps = this.getProperties(to);
		return toProps.thermo.enthalpy - fromProps.thermo.enthalpy;
	}

	default double getEnthalpyChange(State from, State to) {
		Condition fromCond = this.getCanonicalCondition(from);
		Condition toCond = this.getCanonicalCondition(to);
		if (fromCond != null && toCond != null) {
			ConditionProperties fromProps = this.getProperties(fromCond, from);
			ConditionProperties toProps = this.getProperties(toCond, to);
			return toProps.thermo.enthalpy - fromProps.thermo.enthalpy;
		}
		return Double.NaN;
	}
	
	
	default State getAdjacentState(Condition condition, State state) {
		switch(state) {
		case SOLID:
			if (this.getFusion() != null)
				return State.LIQUID;
			return State.GAS;
		case LIQUID:
			if (this.getVaporization() == null)
				return State.SOLID;
			if ((condition.temperature - this.getFusion().getTemperature()) < 
					(this.getVaporization().getTemperature() - condition.temperature)) {
				return State.SOLID;
			} else return State.GAS;
		case GAS:
			if (this.getFusion() != null)
				return State.LIQUID;
			return State.SOLID;
		default:
			return null;
		}
	}
	
	default Mixture mix(IndustrialMaterial material, double weight) {
		return this.mix(new MixtureComponent(material, weight));
	}
	default Mixture mix(MixtureComponent comp) {
		return this.mix(comp.material, comp.weight);
	}
}
