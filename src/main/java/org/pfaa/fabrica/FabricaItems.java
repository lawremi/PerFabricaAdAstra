package org.pfaa.fabrica;

import java.util.List;

import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.core.catalog.CatalogUtils;
import org.pfaa.core.catalog.ItemCatalog;
import org.pfaa.fabrica.item.JointCompoundItem;
import org.pfaa.fabrica.model.Intermediate.Intermediates;

import net.minecraft.item.Item;

public class FabricaItems implements ItemCatalog {
	
	public static final IndustrialMaterialItem<Intermediates> INTERMEDIATE_LUMP = 
			new IndustrialMaterialItem<Intermediates>(Forms.LUMP, Intermediates.class);
	public static final IndustrialMaterialItem<Intermediates> INTERMEDIATE_DUST = 
			new IndustrialMaterialItem<Intermediates>(Forms.DUST, Intermediates.class);
	
	public static final Item JOINT_COMPOUND = new JointCompoundItem();
	
	@SuppressWarnings("rawtypes")
	public static List<IndustrialMaterialItem> getIndustrialMaterialItems() {
		return CatalogUtils.getEntries(FabricaItems.class, IndustrialMaterialItem.class);
	}
}
