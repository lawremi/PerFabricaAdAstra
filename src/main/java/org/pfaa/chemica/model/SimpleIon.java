package org.pfaa.chemica.model;

import org.pfaa.chemica.model.ChemicalStateProperties.Aqueous;
import org.pfaa.chemica.model.Formula.Part;

public class SimpleIon extends SimpleChemical implements Ion {

	public SimpleIon(Formula formula, Aqueous aqueous) {
		super(formula, aqueous);
	}

	@Override
	public Part getPart() {
		return this._(1);
	}

	@Override
	public Part _(int stoichiometry) {
		return new Formula.Part(this, stoichiometry);
	}

}
