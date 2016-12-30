package org.pfaa.chemica.registration;

import org.pfaa.chemica.block.ConstructionMaterialBlock;
import org.pfaa.chemica.item.IndustrialItems;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.item.MaterialStack;
import org.pfaa.chemica.model.ConstructionMaterial;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;

import com.google.common.base.CaseFormat;

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
		OreDictionary.registerOre(materialStack.getOreDictKey(), itemStack);
	}
}
