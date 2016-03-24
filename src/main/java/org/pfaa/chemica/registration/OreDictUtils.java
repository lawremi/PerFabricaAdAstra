package org.pfaa.chemica.registration;

import java.util.List;

import org.pfaa.chemica.block.ConstructionMaterialBlock;
import org.pfaa.chemica.item.IndustrialItemAccessors;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.model.ConstructionMaterial;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;

import com.google.common.base.CaseFormat;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public abstract class OreDictUtils {
	public static ItemStack lookupBest(Form form, IndustrialMaterial material) {
		return lookupBest(makeKey(form, material));
	}

	public static ItemStack lookupBest(String prefix, IndustrialMaterial material) {
		return lookupBest(makeKey(prefix, material));
	}

	public static ItemStack lookupBest(String key) {
		List<ItemStack> hits = OreDictionary.getOres(key);
		for (ItemStack hit : hits) {
			if (hit.getItem() instanceof IndustrialItemAccessors) {
				return hit;
			}
		}
		return hits.size() > 0 ? hits.get(0) : null;
	}
	
	public static ItemStack lookupBest(String prefix, ItemStack itemStack) {
		if (itemStack.getItem() instanceof IndustrialItemAccessors) {
			IndustrialMaterial material = ((IndustrialItemAccessors)itemStack.getItem()).getIndustrialMaterial(itemStack);
			return lookupBest(makeKey(prefix, material.getOreDictKey()));
		}
		return null;
	}
	
	public static ItemStack lookupBest(Form form, ItemStack itemStack) {
		return lookupBest(form.name(), itemStack);
	}
	
	private static String makeKey(String prefix, IndustrialMaterial material) {
		return makeKey(prefix, material.getOreDictKey());
	}
	
	private static String makeKey(Form form, IndustrialMaterial material) {
		return makeKey(form.oreDictKey(), material);
	}
	
	public static String makeKey(String prefix, String postfix) {
		if (postfix == null) {
			throw new IllegalArgumentException("a material lacks an ore dict key");
		}
		return prefix + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, postfix);
	}
	
	public static List<ItemStack> lookup(Form form, IndustrialMaterial material) {
		return OreDictionary.getOres(makeKey(form, material));
	}

	public static <T extends Enum<?> & IndustrialMaterial> void register(IndustrialMaterialItem<T> item) {
		for (T material : item.getIndustrialMaterials()) {
			OreDictionary.registerOre(makeKey(item.getForm(), material), item.getItemStack(material));	
		}
	}
	
	public static void register(ConstructionMaterialBlock block) {
		for (ConstructionMaterial material : block.getConstructionMaterials()) {
			OreDictionary.registerOre(makeKey(Forms.BLOCK, material), block.getItemStack(material));	
		}
	}
	
	public static void registerDye(String color, IndustrialMaterial material) {
		ItemStack itemStack = OreDictUtils.lookupBest(Forms.DUST, material);
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
