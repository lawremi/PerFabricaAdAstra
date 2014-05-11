package org.pfaa;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import org.pfaa.geologica.Geologica;
import org.pfaa.geologica.block.StairsBlock;

import com.google.common.base.CaseFormat;

import cpw.mods.fml.common.LoaderException;
import cpw.mods.fml.common.registry.GameRegistry;

public class RegistrationUtils {

	public static void registerDeclaredBlocks(Class catalogClass, Class blockClass, Class<? extends ItemBlock> itemClass) {
		Field[] fields = catalogClass.getFields();
		for (Field field : fields) {
			try {
				Object value = field.get(null);
				if (value instanceof Block && blockClass.isAssignableFrom(value.getClass())) {
					Block block = (Block)value;
					String name = CaseFormat.UPPER_UNDERSCORE.
							      to(CaseFormat.LOWER_CAMEL, field.getName().replaceAll("__", "."));
					block.setBlockName(name);
					GameRegistry.registerBlock(block, itemClass, name);
				}
			} catch (Exception e) {
				Geologica.log.fatal("Failed to register field '" + field.getName() + "' as block");
				throw new LoaderException(e);
			}
		}
	}
	
}
