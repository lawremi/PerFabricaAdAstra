package org.pfaa;

import java.lang.reflect.Constructor;

import net.minecraft.item.Item;

import org.pfaa.geologica.Geologica;

import cpw.mods.fml.common.LoaderException;

public class ItemCatalog {
	protected static <T extends Item> T createItem(Class<T> itemClass) {
		T item = null;
		try {
			Constructor<T> constructor = itemClass.getConstructor();
			String name = nameForItemClass(itemClass);
			item = constructor.newInstance();
			item.setUnlocalizedName(name);
		} catch (Exception e) {
			Geologica.log.fatal("Failed to construct item of class " + itemClass.getCanonicalName());
			throw new LoaderException(e);
		}
		return item;
	}
	
	private static String nameForItemClass(Class<? extends Item> itemClass) {
		return itemClass.getSimpleName().replaceAll("Item$", "");
	}
}
