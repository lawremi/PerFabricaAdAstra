package org.pfaa.chemica.model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.Lists;

public interface Mixture extends IndustrialMaterial {
	List<MixtureComponent> getComponents();
	
	default MixtureComponent getComponent(IndustrialMaterial material) {
		for (MixtureComponent comp : this.getComponents()) {
			if (comp.material.equals(material)) {
				return comp;
			}
		}
		return null;
	}
	
	default Mixture removeComponents(IndustrialMaterial... materials) {
		List<MixtureComponent> comps = Lists.newArrayList(this.getComponents());
		Iterator<MixtureComponent> it = comps.iterator();
		while(it.hasNext()) {
			if (ArrayUtils.contains(materials, it.next().material)) {
				it.remove();
				break;
			}
		}
		return new SimpleMixture(comps);
	}
	
	default Mixture concentrate(Mixture mixture, int factor) {
		List<MixtureComponent> comps = mixture.getComponents();
		return new SimpleMixture(comps.stream().map((comp) -> {
			if (comp.weight < 1.0)
				return comp.concentrate(factor);
			return comp;
		}).collect(Collectors.toList()));
	}
	
	default Extraction extract(IndustrialMaterial extractant, IndustrialMaterial... materials) {
		Mixture extract = new SimpleMixture();
		Mixture residuum = new SimpleMixture();
		for (IndustrialMaterial material : materials) {
			MixtureComponent comp = this.getComponent(material);
			if (comp != null) {
				extract.mix(comp);
			}
		}
		List<IndustrialMaterial> materialList = Arrays.asList(materials);
		for (MixtureComponent comp : this.getComponents()) {
			if (!materialList.contains(comp.material)) {
				residuum.mix(comp);
			}
		}
		return new Extraction(extractant, extract, residuum);
	}
	
	default double getTotalWeight() {
		double weight = 0;
		for (MixtureComponent comp : this.getComponents()) {
			weight += comp.weight;
		}
		return weight;
	}

	default IndustrialMaterial simplify() {
		if (this.getComponents().size() == 1)
			return this.getComponents().get(0).material;
		return this;
	}
}
