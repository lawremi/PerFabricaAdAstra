package org.pfaa.chemica.item;

import org.pfaa.chemica.block.IndustrialBlockAccessors;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.core.item.CompositeBlockItem;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class IndustrialBlockItem extends CompositeBlockItem implements IndustrialItemAccessors {

	public IndustrialBlockItem(Block block) {
		super(block);
	}

	@Override
	public IndustrialMaterial getIndustrialMaterial(ItemStack itemStack) {
		IndustrialBlockAccessors block = (IndustrialBlockAccessors)this.field_150939_a;
		return block.getIndustrialMaterial(itemStack.getItemDamage());
	}

	@Override
	public Form getForm() {
		return Forms.BLOCK;
	}

}
