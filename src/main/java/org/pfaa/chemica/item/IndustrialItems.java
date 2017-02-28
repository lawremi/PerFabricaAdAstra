package org.pfaa.chemica.item;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.processing.MaterialStack;
import org.pfaa.chemica.registration.OreDictUtils;
import org.pfaa.core.item.ChanceStack;

import com.google.common.collect.Sets;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/*
 * Implemented on top of the ore dictionary for interoperability.
 * Relies on inheritance heuristic to prefer items in this framework.
 */

public class IndustrialItems {

	public static ItemStack getBestItemStack(Form form, IndustrialMaterial material) {
		return getBestItemStack(OreDictUtils.makeKey(form, material), 1);
	}
	
	private static ItemStack getBestItemStack(String key, int size) {
		List<ItemStack> hits = getItemStacks(key, size);
		for (ItemStack hit : hits) {
			if (hit.getItem() instanceof IndustrialItemAccessors) {
				return hit;
			}
		}
		return hits.size() > 0 ? hits.get(0) : null;
	}

	public static ItemStack getBestItemStack(Form form, ItemStack itemStack) {
		if (itemStack.getItem() instanceof IndustrialItemAccessors) {
			IndustrialMaterial material = ((IndustrialItemAccessors)itemStack.getItem()).getIndustrialMaterial(itemStack);
			return getBestItemStack(OreDictUtils.makeKey(form.oreDictKey(), material.getOreDictKey()), itemStack.stackSize);
		}
		return null;
	}

	public static ItemStack getBestItemStack(MaterialStack materialStack) {
		return getBestItemStack(materialStack.getOreDictKey(), materialStack.getSize());
	}
	
	private static List<ItemStack> getItemStacks(String key, int size) {
		List<ItemStack> stacks = OreDictionary.getOres(key);
		if (size > 1) {
			stacks = stacks.stream().map((stack) -> {
				stack = stack.copy();
				stack.stackSize = size;
				return stack;
			}).collect(Collectors.toList());
		}
		return stacks;
	}

	/*
	 * If an ItemStack is requested for a fluid, could use glass bottles as dust/mol equivalent.
	 * Could have a "vial" for tiny dust equivalent. But wouldn't those
	 * be their own forms? Potentially but not necessarily, as they are handled
	 * by the FluidContainerRegistry. Energy requirements could be represented
	 * by lava buckets in the recipe. 
	 * 
	 */
	public static List<ItemStack> getItemStacks(MaterialStack materialStack) {
		return getItemStacks(materialStack.getOreDictKey(), materialStack.getSize());
	}
	
	public static List<ItemStack> getItemStacks(Form form, IndustrialMaterial material) {
		return getItemStacks(OreDictUtils.makeKey(form, material), 1);
	}
	
	public static Set<List<ItemStack>> getItemStackProduct(List<MaterialStack> stacks) {
		List<Set<ItemStack>> itemStacks = stacks.stream().map(IndustrialItems::getItemStacks).
				map(Sets::newHashSet).collect(Collectors.toList());
		return Sets.cartesianProduct(itemStacks);
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

	public static ChanceStack getChanceStack(MaterialStack secondary) {
		return new ChanceStack(getBestItemStack(secondary), secondary.getChance());
	}
}
