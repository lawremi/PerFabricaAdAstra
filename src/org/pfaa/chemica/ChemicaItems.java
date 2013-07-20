package org.pfaa.chemica;

import org.pfaa.ItemCatalog;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.geologica.item.CrudeMaterialItem;

public class ChemicaItems extends ItemCatalog {
	public static final IndustrialMaterialItem DUST = createItem(CrudeMaterialItem.class);
}
