package org.pfaa.geologica.processing;

import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.SimpleMixture;

public class Solutions {
	public static final Mixture PURIFIED_BRINE = 
			new SimpleMixture("brine.purified", 
					new MixtureComponent(Compounds.H2O, 1.0), 
					new MixtureComponent(Compounds.NaCl, 0.3));
}
