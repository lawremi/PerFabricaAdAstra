package org.pfaa.chemica.registration;

import java.util.List;

import org.pfaa.chemica.Chemica;
import org.pfaa.chemica.block.ConstructionMaterialBlock;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.model.ConstructionMaterial;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.processing.Form;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.core.item.CompositeBlockItem;

import com.google.common.base.CaseFormat;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public abstract class OreDictUtils {
	public static ItemStack lookupBest(Form form, IndustrialMaterial material) {
		List<ItemStack> hits = lookup(form, material);
		for (ItemStack hit : hits) {
			if (hit.getItem() instanceof IndustrialMaterialItem || (hit.getItem() instanceof CompositeBlockItem)) {
				return hit;
			}
		}
		return hits.size() > 0 ? hits.get(0) : null;
	}
	
	private static String makeKey(Form form, IndustrialMaterial material) {
		return makeKey(form.oreDictKey(), material.getOreDictKey());
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
}
