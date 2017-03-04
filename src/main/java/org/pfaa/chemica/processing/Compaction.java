package org.pfaa.chemica.processing;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.IndustrialMaterial;

public class Compaction extends DegenerateConversion implements Sizing {

	protected Compaction(IndustrialMaterial material) {
		super(material);
	}

	@Override
	public Direction getDirection() {
		return Direction.INCREASE;
	}

	@Override
	public Condition getCondition() {
		return this.getMaterial().getSinteringCondition();
	}

	@Override
	public double getEnergy() {
		return this.getMaterial().getEnthalpyChange(this.getCondition());
	}

	@Override
	public Form getOutputForm(Form inputForm) {
		return inputForm.compact();
	}

	public static Compaction of(IndustrialMaterial material) {
		return new Compaction(material);
	}
	
	public static <T extends Enum<?> & IndustrialMaterial> Stream<Compaction> of(Class<T> material, Predicate<T> predicate) {
		return Arrays.stream(material.getEnumConstants()).filter(predicate).map(Compaction::of);
	}
	
	public static <T extends Enum<?> & IndustrialMaterial> Stream<Compaction> of(Class<T> material) {
		return of(material, (x) -> true);
	}
}
