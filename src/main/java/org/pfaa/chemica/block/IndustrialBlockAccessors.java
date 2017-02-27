package org.pfaa.chemica.block;

import java.util.List;

import org.pfaa.chemica.item.IndustrialItemAccessors;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.core.block.CompositeBlockAccessors;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;

public interface IndustrialBlockAccessors extends IndustrialItemAccessors, CompositeBlockAccessors {
	default IndustrialMaterial getIndustrialMaterial(ItemStack stack) {
		return this.getIndustrialMaterial(stack.getItemDamage());
	}
	default List<? extends IndustrialMaterial> getIndustrialMaterials() {
		List<IndustrialMaterial> materials = Lists.newArrayList();
		for (int m = 0; m < this.getMetaCount(); m++) {
			materials.add(this.getIndustrialMaterial(m));
		}
		return materials;
	}
	IndustrialMaterial getIndustrialMaterial(int meta);
}
