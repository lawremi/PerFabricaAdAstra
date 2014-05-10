package org.pfaa.geologica.item;

import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.geologica.processing.IndustrialMineral.IndustrialMinerals;

public class IndustrialMineralItem extends IndustrialMaterialItem<IndustrialMinerals> {

	public IndustrialMineralItem(int id) {
		super(id, IndustrialMinerals.class);
	}

}
