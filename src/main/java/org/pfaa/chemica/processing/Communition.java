package org.pfaa.chemica.processing;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.pfaa.chemica.model.IndustrialMaterial;

public class Communition extends DegenerateConversion implements Sizing {
	protected Communition(IndustrialMaterial material) {
		super(material);
	}

	@Override
	public Direction getDirection() {
		return Direction.DECREASE;
	}

	@Override
	public Form getOutputForm(Form inputForm) {
		return inputForm.communite();
	}

	@Override
	public Stream<MaterialStoich<?>> getBonusOutputs(Form form) {
		// TODO Get components from mixture, scaling by output form
		return super.getBonusOutputs(form);
	}

	public static Communition of(IndustrialMaterial material) {
		return new Communition(material);
	}
	
	public static <T extends Enum<?> & IndustrialMaterial> Stream<Communition> of(Class<T> material, Predicate<T> predicate) {
		return Arrays.stream(material.getEnumConstants()).filter(predicate).map(Communition::of);
	}
	
	public static <T extends Enum<?> & IndustrialMaterial> Stream<Communition> of(Class<T> material) {
		return of(material, (x) -> true);
	}
}
