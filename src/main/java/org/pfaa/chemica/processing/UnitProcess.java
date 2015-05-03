package org.pfaa.chemica.processing;

import org.pfaa.chemica.model.Reaction;

public interface UnitProcess extends Step {
	public Type getType();
	public Reaction getChemistry();
	
	public interface Type {
		public Type getParent();
		
		public enum Types implements Type {
			OXIDATION,
			COMBUSTION(OXIDATION),
			REDUCTION,
			HYDROGENATION,
			DEHYDROGENATION,
			HYDROLYSIS,
			HYDRATION,
			DEHYDRATION,
			HALOGENATION,
			NITRIFICATION,
			SULFONATION,
			ALKYLATION,
			DEALKYLATION,
			AMINATION,
			ESTERIFICATION,
			POLYMERIZATION,
			DECOMPOSITION,
			THERMAL_DECOMPOSITION(DECOMPOSITION),
			PYROLYSIS(THERMAL_DECOMPOSITION)
			;
			
			private Type parent;
			
			private Types() {
			}
			
			private Types(Type parent) {
				this.parent = parent;
			}

			@Override
			public Type getParent() {
				return this.parent;
			}
		}
	}
}
