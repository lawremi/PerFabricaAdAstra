package org.pfaa.chemica.processing;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.processing.Form.Forms;

import com.google.common.collect.Lists;

public class Communition extends DegenerateConversion implements Sizing {
	private boolean reduces;
	
	protected Communition(IndustrialMaterial material) {
		super(material);
	}

	private IndustrialMaterial getOutputMaterial() {
		if (this.reduces && this.getMaterial() instanceof Mixture) {
			Mixture mixture = (Mixture)this.getMaterial();
			return mixture.getComponents().get(0).material;
		} else {
			return this.getMaterial();
		}
	}
	
	public Communition reduces() {
		this.reduces = true;
		return this;
	}
	
	@Override
	public Direction getDirection() {
		return Direction.DECREASE;
	}

	@Override
	public Form getOutputForm(Form inputForm) {
		if (this.getOutputMaterial().isGranular())
			return Forms.BLOCK;
		return inputForm.communite();
	}

	@Override
	public List<MaterialStoich<?>> getOutputs() {
		List<MaterialStoich<?>> outputs = super.getOutputs();
		if (this.getMaterial() instanceof Mixture) { 
			Mixture mixture = (Mixture)this.getMaterial();
			if (this.reduces) {
				outputs = Lists.newArrayList(MaterialStoich.of(mixture.getComponents().get(0)));
			} else {
				outputs = Lists.newArrayList(outputs);
			}
			mixture.getComponents().stream().
					skip(1).
					map(MaterialStoich::of).
					map((s) -> s.scale(1/9)).
					forEach(outputs::add);
		}
		return outputs;
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
