package org.pfaa.chemica.processing;

import org.pfaa.chemica.model.IndustrialMaterial;

/* Mechanical modification of particle size */
public interface SizingType extends UnitOperationType<IndustrialMaterial,Sizing> { 
	
	public static enum SizingTypes implements SizingType {
		COMPACTION,
		COMMUNITION;
		
		@Override
		public Sizing of(MaterialStoich<IndustrialMaterial> input) {
			return new Sizing(this, input);
		}
	}
	
}