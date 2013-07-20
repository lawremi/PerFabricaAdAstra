package org.pfaa;

import java.lang.reflect.Field;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.Icon;
import net.minecraftforge.client.MinecraftForgeClient;

import org.pfaa.block.CompositeBlockAccessors;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.LoaderException;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
				FMLLog.log(Level.SEVERE, e, "Caught an exception during block registration");
				throw new LoaderException(e);
			}
		}
	}
	private static void registerBlock(Block block, Class<? extends ItemBlock> itemClass) {
		GameRegistry.registerBlock(block, itemClass, null);
	}
	
}
