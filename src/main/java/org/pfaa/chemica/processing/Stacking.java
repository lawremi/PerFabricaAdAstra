package org.pfaa.chemica.processing;

import java.util.Collections;
import java.util.List;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.model.State;
import org.pfaa.chemica.processing.Form.Forms;

public class Stacking implements MassTransfer {
	private IndustrialMaterial solid;
	private Form unstacked, stacked;
	
	protected Stacking(IndustrialMaterial solid) {
		this.solid = solid;
	}
	
	public IndustrialMaterial getSolid() {
		return solid;
	}
	
	@Override
	public Conversion.Type getType() {
		return null;
	}

	@Override
	public List<MaterialStoich<?>> getInputs() {
		return Collections.singletonList(MaterialStoich.of(State.SOLID.of(solid)));
	}

	@Override
	public List<MaterialStoich<?>> getOutputs() {
		return this.getInputs();
	}

	public Stacking from(Form unstacked) {
		this.unstacked = unstacked;
		return this;
	}
	
	public Stacking to(Form stacked) {
		this.stacked = stacked;
		return this;
	}

	public Form getUnstackedForm() {
		return unstacked;
	}

	public Form getStackedForm() {
		return stacked;
	}

	public static Stacking of(IndustrialMaterial solid) {
		return new Stacking(solid);
	}

	public static interface Type extends MassTransfer.Type {
		Form getUnstackedForm();
		Form getStackedForm();
	}
	
	public static enum Types implements Type {
		DUST_TINY_TO_DUST(Forms.DUST_TINY, Forms.DUST),
		DUST_IMPURE_TINY_TO_DUST_IMPURE(Forms.DUST_IMPURE_TINY, Forms.DUST_IMPURE),
		NUGGET_TO_INGOT(Forms.NUGGET, Forms.INGOT),
		INGOT_TO_BLOCK(Forms.INGOT, Forms.BLOCK),
		LUMP_TO_BLOCK(Forms.LUMP, Forms.BLOCK),
		PILE_TO_BLOCK(Forms.PILE, Forms.BLOCK);
		
		private Form unstacked, stacked;
		
		private Types(Form unstacked, Form stacked) {
			this.unstacked = unstacked;
			this.stacked = stacked;
		}
		
		@Override
		public Form getUnstackedForm() {
			return this.unstacked;
		}
		
		@Override
		public Form getStackedForm() {
			return this.stacked;
		}
	}
}
