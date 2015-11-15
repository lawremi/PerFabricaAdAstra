package org.pfaa.geologica.processing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.pfaa.chemica.model.Chemical;
import org.pfaa.chemica.model.ChemicalStateProperties.Solid;
import org.pfaa.chemica.model.CompoundDictionary;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.ConditionProperties;
import org.pfaa.chemica.model.Formula;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.SimpleChemical;

public class SimpleOreMineral extends SimpleMineral implements OreMineral {
	private Color overrideColor; /* crystal structures can have alternative colors */
	
	public SimpleOreMineral(Chemical concentrate, Substitution... substitutions) {
		this(concentrate, null, substitutions);
	}
	
	public SimpleOreMineral(Chemical concentrate, Color overrideColor, Substitution... substitutions) {
		this(substitute(concentrate, substitutions), overrideColor);
	}
	
	public SimpleOreMineral(Mixture mixture) {
		this(mixture.getComponents());
	}
	
	public SimpleOreMineral(List<MixtureComponent> components, Color overrideColor) {
		super(components);
		this.overrideColor = overrideColor;
	}
	
	public SimpleOreMineral(List<MixtureComponent> components) {
		this(components, null);
	}
	
	@Override
	public Chemical getConcentrate() {
		return (Chemical)this.getComponents().get(0).material;
	}

	private static List<MixtureComponent> substitute(Chemical concentrate, Substitution[] substitutions) {
		List<MixtureComponent> components = new ArrayList<MixtureComponent>();
		components.add(new MixtureComponent(concentrate, 1.0F));
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

	@Override
	public ConditionProperties getProperties(Condition condition) {
		ConditionProperties props = super.getProperties(condition);
		if (this.overrideColor != null) {
			props = props.recolor(this.overrideColor);
		}
		return props;
	}
}
