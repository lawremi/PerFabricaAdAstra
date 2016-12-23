package org.pfaa.chemica.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
	
	default Mixture without(List<IndustrialMaterial> materials) {
		Set<IndustrialMaterial> toRemove = Sets.newHashSet(materials);
		List<MixtureComponent> comps = this.getComponents().stream().
				filter((x) -> !toRemove.contains(x)).
				collect(Collectors.toList());
		return this.removeAll().mixAll(comps);
	}
	
	default Mixture without(IndustrialMaterial... materials) {
		return this.without(Arrays.asList(materials));
	}
	
	default Mixture concentrate(double factor) {
		List<MixtureComponent> comps = this.getComponents();
		return this.removeAll().mixAll(comps.stream().map((comp) -> {
			if (comp.weight < 1.0)
				return comp.concentrate(factor);
			return comp;
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
		double weight = 0;
		for (MixtureComponent comp : this.getComponents()) {
			weight += comp.weight;
		}
		return weight;
	}

	default Mixture normalize() {
		return this.concentrate(1/this.getTotalWeight());
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
}
