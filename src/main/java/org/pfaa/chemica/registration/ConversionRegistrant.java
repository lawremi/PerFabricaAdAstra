package org.pfaa.chemica.registration;

import java.util.Arrays;
import java.util.function.Predicate;

import org.pfaa.chemica.model.Alloy;
import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.Compound;
import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.MaterialState;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.Reaction;
import org.pfaa.chemica.model.SimpleMixture;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.processing.Alloying;
import org.pfaa.chemica.processing.Combination;
import org.pfaa.chemica.processing.Communition;
import org.pfaa.chemica.processing.Compaction;
import org.pfaa.chemica.processing.EnthalpyChange;
import org.pfaa.chemica.processing.Reduction;
import org.pfaa.chemica.processing.Separation;
import org.pfaa.chemica.processing.Separation.Axis;
import org.pfaa.chemica.processing.Smelting;
import org.pfaa.chemica.processing.Stacking;
import org.pfaa.chemica.processing.TemperatureLevel;

public class ConversionRegistrant {
	private ConversionRegistry registry;

	public ConversionRegistrant(ConversionRegistry registry) {
		this.registry = registry;
	}
	
	public <T extends Enum<?> & IndustrialMaterial> void stack(Class<T> material) {
		Arrays.stream(material.getEnumConstants()).map(Stacking::of).forEach(registry::register);
	}
	
	public void meltAndFreeze(IndustrialMaterial material) {
		EnthalpyChange change = EnthalpyChange.of(material);
		registry.register(change.melts());
		registry.register(change.freezes());
	}
	
	public <T extends Enum<?> & IndustrialMaterial> void meltAndFreeze(Class<T> material) {
		Arrays.stream(material.getEnumConstants()).forEach(this::meltAndFreeze);
	}

	public void melt(IndustrialMaterial material) {
		registry.register(EnthalpyChange.of(material).melts());
	}
	
	public <T extends Enum<?> & IndustrialMaterial> void melt(Class<T> material) {
		Arrays.stream(material.getEnumConstants()).forEach(this::melt);
	}
	
	public <T extends Enum<?> & IndustrialMaterial> void melt(Class<T> material, Predicate<T> predicate) {
		Arrays.stream(material.getEnumConstants()).filter(predicate).forEach(this::melt);
	}
	
	public void vaporizeAndCondense(IndustrialMaterial material) {
		EnthalpyChange change = EnthalpyChange.of(material);
		registry.register(change.vaporizes());
		registry.register(change.condenses());
	}
	
	public <T extends Enum<?> & IndustrialMaterial> void vaporizeAndCondense(Class<T> material) {
		Arrays.stream(material.getEnumConstants()).forEach(this::vaporizeAndCondense);
	}

	public <T extends Enum<?> & IndustrialMaterial> void communite(Class<T> material) {
		Arrays.stream(material.getEnumConstants()).map(Communition::of).forEach(registry::register);
	}

	public <T extends Enum<?> & Mixture> void reduce(Class<T> material, Predicate<T> predicate) {
		Arrays.stream(material.getEnumConstants()).filter(predicate).map(Reduction::of).forEach(registry::register);
	}

	public <T extends Enum<?> & IndustrialMaterial> void compact(Class<T> material) {
		Arrays.stream(material.getEnumConstants()).map(Compaction::of).forEach(registry::register);
	}
	
	public <T extends Enum<?> & IndustrialMaterial> void compact(Class<T> material, Predicate<T> predicate) {
		Arrays.stream(material.getEnumConstants()).filter(predicate).map(Compaction::of).forEach(registry::register);
	}
	
	public void smelt(TemperatureLevel temp, Compounds... compounds) {
		this.smeltWithFlux(temp, null, compounds);
	}
	
	public void smeltWithFlux(TemperatureLevel temp, IndustrialMaterial flux, Compounds... compounds) {
		for (Compounds compound : compounds) {
			Smelting smelting = Smelting.of(compound).with(flux);
			registry.register(smelting.yields(State.SOLID));
			registry.register(smelting.yields(State.LIQUID));
		}
	}
	
	public <T extends Enum<?> & Alloy> void alloy(Class<T> material) {
		Arrays.stream(material.getEnumConstants()).forEach((alloy) -> {
			registry.register(Alloying.yielding(State.SOLID.of(alloy)));
			registry.register(Alloying.yielding(State.LIQUID.of(alloy)));
		});
	}
	
	public <T extends Enum<?> & Mixture> void mix(Class<T> material) {
		Arrays.stream(material.getEnumConstants()).map(Combination::yielding).forEach(registry::register);
	}
	
	public void dissolveAndUndissolve(Compound compound) {
		if (!compound.isSoluble(Condition.STP))
			return;
		registry.register(Combination.of(compound).
				with(State.AQUEOUS.getVolumeFactor(), Compounds.H2O).
				yields(State.AQUEOUS.of(compound)).
				given(Chemical.MIN_SOLUBILITY / compound.getSolubility() * 10000));
		registry.register(Separation.of(State.AQUEOUS.of(new SimpleMixture(compound))).
				extracts(MaterialState.of(compound)).
				by(Axis.SOLUBILITY).
				given(compound.getSolubility() / Chemical.MIN_SOLUBILITY));
	}
	
	public <T extends Enum<?> & Compound> void dissolveAndUndissolve(Class<T> material) {
		Arrays.stream(material.getEnumConstants()).forEach(this::dissolveAndUndissolve);
	}
	
	public void decompose(Compound compound) {
		Reaction reaction = Reactions.decompose(compound);
		if (reaction == null) {
			return;
		}
		registry.register(reaction);
	}
	
	public <T extends Enum<?> & Compound> void decompose(Class<T> material) {
		Arrays.stream(material.getEnumConstants()).forEach(this::decompose);
	}
	
	public void separateOre(Mixture mixture) {
		registry.register(Separation.of(mixture).extractsAll().by(Separation.Axis.DENSITY));
	}
	
	public <T extends Enum<?> & Mixture> void separateOre(Class<T> mixture) {
		Arrays.stream(mixture.getEnumConstants()).forEach(this::separateOre);
	}
}
