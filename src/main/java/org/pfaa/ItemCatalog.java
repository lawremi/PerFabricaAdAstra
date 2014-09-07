package org.pfaa;

import java.lang.reflect.Constructor;

import net.minecraft.item.Item;

import org.pfaa.geologica.Geologica;

import com.google.common.base.CaseFormat;

import cpw.mods.fml.common.LoaderException;

public class ItemCatalog {
	protected static <T extends Item> T createItem(Class<T> itemClass) {
		T item = null;
		try {
			Constructor<T> constructor = itemClass.getConstructor();
			item = constructor.newInstance();
		} catch (Exception e) {
			Geologica.log.fatal("Failed to construct item of class " + itemClass.getCanonicalName());
			throw new LoaderException(e);
		}
		return item;
	}
}
