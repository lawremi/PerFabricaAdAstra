package org.pfaa.chemica.model;

import java.util.Map;

import org.pfaa.chemica.model.Compound.Compounds;

import com.google.common.collect.Maps;

public class SimpleIon implements Ion {

	private Formula formula;
	private int charge;
	private Map<Chemical,Thermo> thermoForChemical = Maps.newHashMap();
	
	public SimpleIon(Formula formula, int charge, Thermo waterThermo) {
		this.formula = formula;
		this.charge = charge;
		this.putThermo(Compounds.H2O, waterThermo);
	}
	
	public void putThermo(Chemical chemical, Thermo thermo) {
		this.thermoForChemical.put(chemical, thermo);
	}

	@Override
	public Formula getFormula() {
		return this.formula;
	}

	@Override
	public int getCharge() {
		return this.charge;
	}

	@Override
	public Thermo getThermo(Chemical solvent) {
		return this.thermoForChemical.get(solvent);
	}

	@Override
	public Formula.Part _(int quantity) {
		return new Formula.Part(this, quantity);
	}

	@Override
	public Formula.Part getPart() {
		return this._(1);
	}
}
