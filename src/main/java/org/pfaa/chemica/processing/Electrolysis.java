package org.pfaa.chemica.processing;

import java.util.List;

import org.pfaa.chemica.model.Compound;
import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.Compound.Compounds;

public class Electrolysis implements Conversion {

	private Reaction reaction;
	private Compound solvent;
	
	protected Electrolysis(Reaction reaction) {
		this.reaction = reaction;
		this.solvent = Compounds.H2O;
	}

	public static Electrolysis drives(Reaction reaction) {
		return new Electrolysis(reaction);
	}

	public Electrolysis in(Compound solvent) {
		this.solvent = solvent;
		return this;
	}
	
	@Override
	public Type getType() {
		return null;
	}

	@Override
	public List<MaterialStoich<?>> getInputs() {
		return this.reaction.getInputs();
	}

	@Override
	public List<MaterialStoich<?>> getOutputs() {
		return this.reaction.getOutputs();
	}
	
	@Override
	public double getEnergy() {
		// Electricity is doing all of the work.
		return this.reaction.getFreeEnergyChange(this.getCondition());
	}

	@Override
	public Condition getCondition() {
		return this.reaction.getCondition();
	}

	public Compound getSolvent() {
		return solvent;
	}

}
