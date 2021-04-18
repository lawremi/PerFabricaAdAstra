package org.pfaa.chemica.processing;

import org.pfaa.chemica.model.Condition;
import org.pfaa.chemica.model.IndustrialMaterial;

public class Compaction extends FormConversion implements Sizing {

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
}
