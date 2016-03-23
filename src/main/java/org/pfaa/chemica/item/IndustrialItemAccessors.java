package org.pfaa.chemica.item;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.processing.Form;

import net.minecraft.item.ItemStack;

public interface IndustrialItemAccessors {
	public IndustrialMaterial getIndustrialMaterial(ItemStack itemStack);
	public Form getForm();
}
