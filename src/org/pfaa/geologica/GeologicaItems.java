package org.pfaa.geologica;

import net.minecraft.item.Item;

import org.pfaa.ItemCatalog;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.geologica.item.ClayDustItem;
import org.pfaa.geologica.item.CrudeDustItem;
import org.pfaa.geologica.item.CrudeLiquidItem;

public class GeologicaItems extends ItemCatalog {
	public static final IndustrialMaterialItem CRUDE_DUST = createItem(CrudeDustItem.class);
	public static final Item CLAY_DUST = createItem(ClayDustItem.class);
	
	public static final IndustrialMaterialItem CRUDE_LIQUID = createItem(CrudeLiquidItem.class);
}

