package org.pfaa.chemica.registration;

import java.util.Iterator;
import java.util.List;

import org.pfaa.chemica.item.IndustrialItemAccessors;
import org.pfaa.chemica.item.IndustrialItems;
import org.pfaa.chemica.model.Generic;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.processing.CanonicalForms;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.processing.MaterialStack;

import com.google.common.base.CaseFormat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public abstract class OreDictUtils {
	public static String makeKey(String prefix, IndustrialMaterial material) {
		return makeKey(prefix, material.getOreDictKey());
	}
	
	public static String makeKey(Form form, IndustrialMaterial material) {
		return makeKey(form == null ? null : form.oreDictKey(), material);
	}
	
	public static String makeKey(String prefix, String postfix) {
		if (postfix == null) {
			throw new IllegalArgumentException("a material lacks an ore dict key");
		}
		if (prefix == null) {
			return postfix;
		}
		return prefix + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, postfix);
	}
	
	public static void registerDye(String color, IndustrialMaterial material) {
		ItemStack itemStack = IndustrialItems.getBestItemStack(Forms.DUST, material);
		OreDictionary.registerOre("dye", itemStack);
		OreDictionary.registerOre(OreDictUtils.makeKey("dye", color), itemStack);
	}

	public static String reprefix(String key, String prefix) {
		return makeKey(key, getPostfix(key));
	}

	private static String getPostfix(String key) {
		return key.replaceFirst("[a-z]+", "");
	}

	public static boolean hasPrefix(ItemStack itemStack, String prefix) {
		for (int id : OreDictionary.getOreIDs(itemStack)) {
			if (OreDictionary.getOreName(id).startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}
	
	public static void register(MaterialStack materialStack, ItemStack itemStack) {
		CanonicalForms.register(materialStack.getMaterial(), materialStack.getForm());
		OreDictionary.registerOre(materialStack.getOreDictKey(), itemStack);
	}

	public static void register(MaterialStack materialStack, Item item) {
		register(materialStack, new ItemStack(item));
	}

	public static void register(MaterialStack materialStack, String name) {
		register(materialStack, OreDictionary.getOres(name).get(0));
	}
	
	public static void register(IndustrialItemAccessors item) {
		Iterator<ItemStack> itemStacks = item.getItemStacks().iterator();
		item.getMaterialStacks().forEach((stack) -> {
			register(stack, itemStacks.next());
		});
		String key = item.oreDictKey();
		if (key != null) {
			OreDictionary.registerOre(key, item.getWilcardStack());
		}	
	}
	
	public static void register(List<? extends IndustrialItemAccessors> itemations) {
		itemations.forEach(OreDictUtils::register);
	}
	
	public static <T extends Enum<?> & Generic> void register(Class<T> generics) {
		for (Generic generic : generics.getEnumConstants()) {
			register(generic);
		}
	}

	public static void register(Generic generic) {
		for (IndustrialMaterial specific : generic.getSpecifics()) {
			for (Form form : CanonicalForms.of(specific)) {
				for (ItemStack itemStack : IndustrialItems.getItemStacks(form, specific)) {
					OreDictUtils.register(form.of(generic), itemStack);
				}
			}
		}
	}
}
