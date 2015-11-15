package org.pfaa.chemica.item;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.processing.Form;

import net.minecraft.item.ItemStack;

public class MaterialStack<T extends Enum<?> & IndustrialMaterial> {
	private IndustrialMaterialItem<T> item;
	private T material;
	private int size;
	
	public MaterialStack(IndustrialMaterialItem<T> item, T material, int size) {
		super();
		this.item = item;
		this.material = material;
		this.size = size;
	}

	public Form getForm() {
		return this.item.getForm();
	}
	
	public T getMaterial() {
		return this.material;
	}

	public ItemStack getItemStack() {
		return this.item.getItemStack(this.material, this.size);
	}
}
