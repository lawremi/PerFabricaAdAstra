package org.pfaa.core.catalog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.LoaderException;

public class CatalogUtils {
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> getEntries(Class<? extends Catalog> catalogClass, Class<T> fieldClass) {
		List<T> items = new ArrayList<T>(); 
		for (Field field : catalogClass.getFields()) {
			try {
				Object fieldValue = field.get(null);
				if (fieldClass.isAssignableFrom(fieldValue.getClass())) {
					items.add((T)fieldValue);
				}
			} catch (Exception e) {
				throw new LoaderException(e);
			}
		}
		return items;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Map<String,T> getNamedEntries(Class<? extends Catalog> catalogClass, Class<T> fieldClass) {
		Map<String,T> items = new HashMap<String,T>(); 
		for (Field field : catalogClass.getFields()) {
			try {
				Object fieldValue = field.get(null);
				if (fieldClass.isAssignableFrom(fieldValue.getClass())) {
					items.put(field.getName(), (T)fieldValue);
				}
			} catch (Exception e) {
				throw new LoaderException(e);
			}
		}
		return items;
	}
	
}
