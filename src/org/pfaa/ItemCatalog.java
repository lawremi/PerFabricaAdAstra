package org.pfaa;

import java.lang.reflect.Constructor;
import java.util.logging.Level;

import net.minecraft.item.Item;
import cpw.mods.fml.common.FMLLog;
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
			FMLLog.log(Level.SEVERE, e, "Failed to construct item");
			throw new LoaderException(e);
		}
		return item;
	}
	
	private static String nameForItemClass(Class<? extends Item> itemClass) {
		return itemClass.getSimpleName().replaceAll("Item$", "");
	}
}
