package org.pfaa.chemica.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.Sets;

public interface Mixture extends IndustrialMaterial {
	List<MixtureComponent> getComponents();
	Mixture removeAll();
	
	default MixtureComponent getComponent(IndustrialMaterial material) {
		for (MixtureComponent comp : this.getComponents()) {
			if (comp.material.equals(material)) {
				return comp;
			}
		}
		return null;
	}
	
	default Mixture without(Set<IndustrialMaterial> materials) {
		List<MixtureComponent> comps = this.getComponents().stream().
				filter((x) -> !materials.contains(x.material)).
				collect(Collectors.toList());
		return this.removeAll().mixAll(comps);
	}
	
	default Mixture without(IndustrialMaterial... materials) {
		return this.without(Sets.newHashSet(materials));
	}
	
	default Mixture concentrate(double factor) {
		List<MixtureComponent> comps = this.getComponents();
		return this.removeAll().mixAll(comps.stream().map((comp) -> {
			return comp.concentrate(factor);
		}).collect(Collectors.toList()));
	}
	
	default Mixture extract(List<IndustrialMaterial> materials) {
		List<MixtureComponent> comps = materials.stream().
				map(this::getComponent).
				filter(Objects::nonNull).
				collect(Collectors.toList());
		return this.removeAll().mixAll(comps);
	}
	
	default Mixture extract(IndustrialMaterial... materials) {
		return this.extract(Arrays.asList(materials));
	}
	
	default List<Mixture> separate() {
		return this.getComponents().stream().map(SimpleMixture::new).collect(Collectors.toList());
	}
	
	class Phases {
		public final Mixture solid, liquid, gas;

		public Phases(Mixture solid, Mixture liquid, Mixture gas) {
			this.solid = solid;
			this.liquid = liquid;
			this.gas = gas;
		}
	}
	
	default Phases separateByState(Condition condition) {
		Mixture gas = this.removeAll(), liquid = this.removeAll(), solid = this.removeAll();
		for (MixtureComponent comp : this.getComponents()) {
			State state = comp.material.getProperties(condition).state;
			if (state == State.GAS) {
				gas = gas.mix(comp);
			} else if (state == State.AQUEOUS || state == State.LIQUID) {
				liquid = liquid.mix(comp);
			} else {
				solid = solid.mix(comp);
			}
		}
		return new Phases(solid, liquid, gas);
	}
	
	default double getTotalWeight() {
		return this.getComponents().stream().mapToDouble((comp) -> comp.weight).sum();
	}

	default Mixture normalize() {
		return this.concentrate(1/this.getTotalWeight());
	}
	
	default Mixture discretize() {
		OptionalDouble minorWeight = this.normalize().getComponents().stream().
				mapToDouble((comp) -> comp.weight).min();
		if (minorWeight.isPresent()) {
			return this.concentrate(1 / Math.max(minorWeight.getAsDouble(), 0.1F));
		} else {
			return this;
		}
	}
	
	default IndustrialMaterial simplify() {
		if (this.getComponents().size() == 1)
			return this.getComponents().get(0).material;
		return this;
	}
	
	default Mixture mixAll(Mixture other) {
		return this.mixAll(other.getComponents());
	}

	default Mixture mixAll(List<MixtureComponent> comps) {
		Mixture mixture = this;
		for (MixtureComponent comp : comps) {
			mixture = mixture.mix(comp);
		}
		return mixture;
	}
	
	default Mixture getFraction(Condition cond, State state) {
		List<MixtureComponent> comps = this.getComponents().stream().
				filter((comp) -> comp.getMaterial().getProperties(cond).state == state).
				collect(Collectors.toList());
		return this.removeAll().mixAll(comps);
	}
	
	@Override
	default Condition getCanonicalCondition(State state) {
		IntStream temps = this.getComponents().stream().
				mapToInt((comp) -> comp.material.getCanonicalCondition(state).temperature);
		OptionalInt extreme = OptionalInt.empty();
		switch(state) {
		case SOLID:
			extreme = temps.min();
			break;
		case LIQUID:
			extreme = this.getStandardState() == State.SOLID ? temps.max() : temps.min();  
			break;
		case GAS:
			extreme = temps.max();
			break;
		default:
		}
		return extreme.isPresent() ? new Condition(extreme.getAsInt()) : null; 
	}
}
