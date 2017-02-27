package org.pfaa.core.registration;

import java.util.Map;

import org.pfaa.core.catalog.BlockCatalog;
import org.pfaa.core.catalog.CatalogUtils;
import org.pfaa.core.catalog.ItemCatalog;

import com.google.common.base.CaseFormat;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class RegistrationUtils {

	public static void registerDeclaredBlocks(Class<? extends BlockCatalog> catalogClass, Class<? extends Block> blockClass, Class<? extends ItemBlock> itemClass) {
		Map<String, ? extends Block> blocks = CatalogUtils.getNamedEntries(catalogClass, blockClass);
		blocks.forEach((fieldName, block) -> {
			registerBlock(block, itemClass, fieldNameToUnlocalizedName(fieldName));
        });
	}
	
	public static void registerBlock(Block block, Class<? extends ItemBlock> itemClass, String name) {
		block.setBlockName(name);
		String prefix = Loader.instance().activeModContainer().getModId().replaceFirst("PFAA", "");
		block.setBlockTextureName(prefix + ":" + name);
		GameRegistry.registerBlock(block, itemClass, name);
	}

	public static void registerDeclaredItems(Class<? extends ItemCatalog> catalogClass) {
		Map<String, Item> items = CatalogUtils.getNamedEntries(catalogClass, Item.class);
		items.forEach((fieldName, item) -> {
			registerItem(item, fieldNameToUnlocalizedName(fieldName));
        });
	}

	public static void registerItem(Item item, String name) {
		item.setUnlocalizedName(name);
		GameRegistry.registerItem(item, name);
	}
	
	public static String fieldNameToUnlocalizedName(String name) {
		return CaseFormat.UPPER_UNDERSCORE.
				      to(CaseFormat.LOWER_CAMEL, name.replaceAll("__", "."));
	}
	
}
