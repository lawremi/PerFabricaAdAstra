package org.pfaa.geologica.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public abstract class GeoItem extends Item {
	public GeoItem() {
		this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabMaterials);
	}
}
