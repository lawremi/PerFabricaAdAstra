package org.pfaa.fabrica;

import java.util.List;

import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.core.catalog.CatalogUtils;
import org.pfaa.core.catalog.ItemCatalog;
import org.pfaa.fabrica.model.Intermediate.Intermediates;

public class FabricaItems implements ItemCatalog {
	public static final IndustrialMaterialItem<Intermediates> INTERMEDIATE_CRUSHED = 
			new IndustrialMaterialItem<Intermediates>(Forms.CRUSHED, Intermediates.class);
	public static final IndustrialMaterialItem<Intermediates> INTERMEDIATE_DUST = 
			new IndustrialMaterialItem<Intermediates>(Forms.DUST, Intermediates.class);
	
	@SuppressWarnings("rawtypes")
	public static List<IndustrialMaterialItem> getIndustrialMaterialItems() {
		return CatalogUtils.getEntries(FabricaItems.class, IndustrialMaterialItem.class);
	}
}
