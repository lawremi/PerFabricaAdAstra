package org.pfaa.geologica.processing;

import java.util.ArrayList;
import java.util.List;

import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.ChemicalPhaseProperties.Solid;
import org.pfaa.chemica.model.CompoundDictionary;
import org.pfaa.chemica.model.Formula;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.SimpleChemical;

public class SimpleOreMineral extends SimpleMineral implements OreMineral {
	public SimpleOreMineral(Chemical concentrate, Substitution... substitutions) {
		this(substitute(concentrate, substitutions));
	}
	
	public SimpleOreMineral(Mixture mixture) {
		this(mixture.getComponents());
	}
	
	public SimpleOreMineral(List<MixtureComponent> components) {
		super(components);
	}
	
	@Override
	public OreMineral mix(Chemical material, double weight) {
		Mixture mixture = super.mix(material, weight);
		return new SimpleOreMineral(mixture.getComponents());
	}
	
	@Override
	public String name() {
		return getOreDictKey() + "Ore";
	}

	@Override
	public String getOreDictKey() {
		return getConcentrate().getOreDictKey();
	}

	@Override
	public Chemical getConcentrate() {
		return (Chemical)this.getComponents().get(0).material;
	}

	private static List<MixtureComponent> substitute(Chemical concentrate, Substitution[] substitutions) {
		List<MixtureComponent> components = new ArrayList();
		components.add(new MixtureComponent(concentrate, 1.0));
		for (Substitution substitution : substitutions) {
			Formula formula = concentrate.getFormula().substituteFirstPart(substitution.getMaterial());
			Chemical compound = CompoundDictionary.lookup(formula);
			if (compound == null) {
				compound = new SimpleChemical(formula, substitution.material.getOreDictKey(), new Solid());
			}
			components.add(new MixtureComponent(compound, substitution.weight));
		}
		return components;
	}
}
