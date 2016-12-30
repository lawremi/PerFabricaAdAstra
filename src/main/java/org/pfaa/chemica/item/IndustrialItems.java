package org.pfaa.chemica.item;

import java.util.List;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.registration.OreDictUtils;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class IndustrialItems {

	public static ItemStack getBestItemStack(Form form, IndustrialMaterial material) {
		return getBestItemStack(OreDictUtils.makeKey(form, material));
	}

	public static ItemStack getBestItemStack(String prefix, IndustrialMaterial material) {
		return getBestItemStack(OreDictUtils.makeKey(prefix, material));
	}

	public static ItemStack getBestItemStack(String key) {
		List<ItemStack> hits = OreDictionary.getOres(key);
		for (ItemStack hit : hits) {
			if (hit.getItem() instanceof IndustrialItemAccessors) {
				return hit;
			}
		}
		return hits.size() > 0 ? hits.get(0) : null;
	}

	public static ItemStack getBestItemStack(String prefix, ItemStack itemStack) {
		if (itemStack.getItem() instanceof IndustrialItemAccessors) {
			IndustrialMaterial material = ((IndustrialItemAccessors)itemStack.getItem()).getIndustrialMaterial(itemStack);
			return getBestItemStack(OreDictUtils.makeKey(prefix, material.getOreDictKey()));
		}
		return null;
	}

	public static ItemStack getBestItemStack(Form form, ItemStack itemStack) {
		return getBestItemStack(form.name(), itemStack);
	}

	public static List<ItemStack> getItemStacks(Form form, IndustrialMaterial material) {
		return OreDictionary.getOres(OreDictUtils.makeKey(form, material));
	}

	public static Form getForm(ItemStack itemStack) {
		for (int id : OreDictionary.getOreIDs(itemStack)) {
			for (Form form : Forms.values()) {
				if (OreDictionary.getOreName(id).startsWith(form.oreDictKey())) {
					return form;
				}
			}
		}
		return null;
	}

}
