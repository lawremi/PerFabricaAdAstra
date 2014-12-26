package org.pfaa.geologica;

import net.minecraft.item.Item;

import org.pfaa.ItemCatalog;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.geologica.item.CrudeChunkItem;
import org.pfaa.geologica.item.IndustrialMineralItem;

public class GeologicaItems extends ItemCatalog {
	public static final IndustrialMaterialItem INDUSTRIAL_MINERAL_DUST = createItem(IndustrialMineralItem.class);
	
	public static final Item CRUDE_CHUNK = createItem(CrudeChunkItem.class);
}

