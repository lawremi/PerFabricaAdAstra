package org.pfaa.chemica.registration;

import org.pfaa.chemica.model.Reaction;
import org.pfaa.chemica.processing.Stacking;
import org.pfaa.chemica.processing.Combination;
import org.pfaa.chemica.processing.EnthalpyChange;
import org.pfaa.chemica.processing.Separation;
import org.pfaa.chemica.processing.Sizing;

public interface ConversionRegistry {
	void register(Stacking stacking);
	void register(Separation separation);
	void register(Combination combination);
	void register(Sizing sizing);
	void register(EnthalpyChange enthalpyChange);
	void register(Reaction reaction);
}
