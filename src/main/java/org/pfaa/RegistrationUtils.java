package org.pfaa;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import org.pfaa.geologica.Geologica;

import cpw.mods.fml.common.LoaderException;
import cpw.mods.fml.common.registry.GameRegistry;

public class RegistrationUtils {

	public static void registerDeclaredBlocks(Class catalogClass, Class blockClass, Class<? extends ItemBlock> itemClass) {
		Field[] fields = catalogClass.getFields();
		for (Field field : fields) {
			try {
				Object value = field.get(null);
				if (value instanceof Block && blockClass.isAssignableFrom(value.getClass())) {
					registerBlock((Block)value, itemClass);
				}
			} catch (Exception e) {
				Geologica.log.fatal("Failed to register field '" + field.getName() + "' as block");
				throw new LoaderException(e);
			}
		}
	}
	private static void registerBlock(Block block, Class<? extends ItemBlock> itemClass) {
		GameRegistry.registerBlock(block, itemClass, null);
	}
	
}
