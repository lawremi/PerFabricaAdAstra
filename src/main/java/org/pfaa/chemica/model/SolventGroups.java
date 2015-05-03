package org.pfaa.chemica.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum SolventGroups implements SolventGroup {
	POLAR, NON_POLAR, ACID, ALKALI, ACID_ALKALI;
	
	private List<Chemical> solvents = new ArrayList<Chemical>();
	
	private SolventGroups(Chemical... solvents) {
		Collections.addAll(this.solvents, solvents);
	}

	@Override
	public boolean containsSolvent(Chemical solvent) {
		return solvents.contains(solvent);
	}
}
