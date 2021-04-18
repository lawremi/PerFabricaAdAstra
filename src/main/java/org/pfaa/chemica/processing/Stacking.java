package org.pfaa.chemica.processing;

import java.util.Arrays;
import java.util.stream.Stream;

import org.pfaa.chemica.model.IndustrialMaterial;

public class Stacking extends FormConversion implements MassTransfer {
	
	protected Stacking(IndustrialMaterial material) {
		super(material);
	}
	
	@Override
	public Form getOutputForm(Form inputForm) {
		return inputForm.stack();
	}

	public boolean bidirectional() {
		return true;
	}
	
	public static Stacking of(IndustrialMaterial material) {
		return new Stacking(material);
	}
	
	public static <T extends Enum<?> & IndustrialMaterial> Stream<Stacking> of(Class<T> material) {
		return Arrays.stream(material.getEnumConstants()).map(Stacking::of);
	}
	
}
