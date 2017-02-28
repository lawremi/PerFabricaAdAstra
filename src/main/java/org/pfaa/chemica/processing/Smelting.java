package org.pfaa.chemica.processing;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.Element;
import org.pfaa.chemica.model.Formula;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.State;

// Refers specifically to carbothermal reduction
public class Smelting extends ConditionedConversion {
	private Chemical oxidized;
	private List<IndustrialMaterial> additives;
	private MaterialStoich<Element> metal;
	
	protected Smelting(Chemical oxidized) {
		this.oxidized = oxidized;
	}

	@Override
	public Type getType() {
		return null;
	}

	@Override
	public List<MaterialStoich<?>> getInputs() {
		return Stream.concat(Stream.of(MaterialStoich.of(this.oxidized)), 
				this.additives.stream().map(MaterialStoich::of)).
				collect(Collectors.toList());
	}

	@Override
	public List<MaterialStoich<?>> getOutputs() {
		return Collections.singletonList(metal);
	}

	public Smelting with(IndustrialMaterial material) {
		this.additives.add(material);
		return this;
	}
	
	@Override
	public Form getOutputForm(Form inputForm) {
		return inputForm.compact();
	}

	public Smelting at(TemperatureLevel level) {
		return (Smelting)this.at(level.getReferenceTemperature());
	}
	
	public Smelting yields(State state) {
		Formula.Part metalPart = oxidized.getFormula().getFirstPart();
		this.metal = MaterialStoich.of(metalPart.stoichiometry, state.of(metalPart.element));
		return this;
	}
	
	public static Smelting of(Chemical oxidized) {
		return new Smelting(oxidized);
	}
}
