package org.pfaa.chemica.item;

import java.util.List;
import java.util.stream.Collectors;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.MaterialStack;
import org.pfaa.core.item.ItemStackContainer;

import net.minecraft.item.ItemStack;

public interface IndustrialItemAccessors extends ItemStackContainer {
	IndustrialMaterial getIndustrialMaterial(ItemStack itemStack);
	
	Form getForm();
	default String oreDictKey() { return null; }
	
	List<? extends IndustrialMaterial> getIndustrialMaterials();
	default List<MaterialStack> getMaterialStacks() {
		Form form = this.getForm();
		return this.getIndustrialMaterials().stream().map(form::of).collect(Collectors.toList());
	}
}
