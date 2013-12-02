package org.pfaa;

import java.lang.reflect.Constructor;

import net.minecraft.item.Item;

import org.pfaa.geologica.Geologica;

import cpw.mods.fml.common.LoaderException;

public class ItemCatalog {
	protected static <T extends Item> T createItem(Class<T> itemClass) {
		T item = null;
		try {
			Constructor<T> constructor = itemClass.getConstructor(int.class);
			String name = nameForItemClass(itemClass);
			int id = ConfigIDProvider.getInstance().nextItemID(name);
			item = constructor.newInstance(id);
			item.setUnlocalizedName(name);
		} catch (Exception e) {
			Geologica.log.severe("Failed to construct item of class " + itemClass.getCanonicalName());
			throw new LoaderException(e);
		}
		return item;
	}
	
	private static String nameForItemClass(Class<? extends Item> itemClass) {
		return itemClass.getSimpleName().replaceAll("Item$", "");
	}
}
