package org.pfaa.chemica.item;

import java.util.List;

import org.pfaa.chemica.block.IndustrialBlockAccessors;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.processing.Form;
import org.pfaa.core.item.CompositeBlockItem;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class IndustrialBlockItem extends CompositeBlockItem implements IndustrialItemAccessors {

	public IndustrialBlockItem(Block block) {
		super(block);
	}

	public IndustrialBlockAccessors getBlock() {
		return (IndustrialBlockAccessors)this.field_150939_a;
	}
	
	@Override
	public IndustrialMaterial getIndustrialMaterial(ItemStack itemStack) {
		IndustrialBlockAccessors block = this.getBlock();
		return block.getIndustrialMaterial(itemStack.getItemDamage());
	}

	@Override
	public Form getForm() {
		return this.getBlock().getForm();
	}

	@Override
	public List<? extends IndustrialMaterial> getIndustrialMaterials() {
		return this.getBlock().getIndustrialMaterials();
	}

	@Override
	public String oreDictKey() {
		return this.getBlock().oreDictKey();
	}
}
