package org.pfaa.chemica.registration;

import org.pfaa.chemica.processing.Alloying;
import org.pfaa.chemica.processing.Combination;
import org.pfaa.chemica.processing.Communition;
import org.pfaa.chemica.processing.Compaction;
import org.pfaa.chemica.processing.EnthalpyChange;
import org.pfaa.chemica.processing.Reaction;
import org.pfaa.chemica.processing.Separation;
import org.pfaa.chemica.processing.Smelting;
import org.pfaa.chemica.processing.Stacking;

public interface ConversionRegistry {
	void register(Stacking stacking);
	void register(Separation separation);
	void register(Smelting smelting);
	void register(Alloying alloying);
	void register(Combination combination);
	void register(Communition communition);
	void register(Compaction compaction);
	void register(EnthalpyChange enthalpyChange);
	void register(Reaction reaction);
}
