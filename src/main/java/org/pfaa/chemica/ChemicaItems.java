package org.pfaa.chemica;

import org.pfaa.ItemCatalog;
import org.pfaa.chemica.fluid.FlaskItem;
import org.pfaa.chemica.item.MoleculeItem;

public class ChemicaItems extends ItemCatalog {
	public static final MoleculeItem DUST = createItem(MoleculeItem.class);
	public static final FlaskItem FLASK = createItem(FlaskItem.class);
}
