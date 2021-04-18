package org.pfaa.geologica.processing;

import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.MixtureComponent;
import org.pfaa.chemica.model.SimpleMixture;

public class Mixtures {
	public static final Mixture PURIFIED_BRINE = 
			new SimpleMixture("brine.purified", 
					new MixtureComponent(Compounds.H2O, 0.7), 
					new MixtureComponent(Compounds.NaCl, 0.3));
	public static final Mixture RPG = new SimpleMixture(Compounds.BENZENE, 0.5).mix(Compounds.TOLUENE, 0.5);
	public static final Mixture BUTENES = new SimpleMixture(Compounds.ISO_BUTENE, 0.5).mix(Compounds.N_BUTENE, 0.5);
}
