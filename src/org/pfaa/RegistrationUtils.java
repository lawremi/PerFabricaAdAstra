package org.pfaa;

import java.lang.reflect.Field;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.LoaderException;
import cpw.mods.fml.common.registry.GameRegistry;

public class RegistrationUtils {

	public static void registerDeclaredBlocks(Class catalogClass, Class<? extends ItemBlock> itemClass) {
		Field[] fields = catalogClass.getFields();
		for (Field field : fields) {
			try {
				Object value = field.get(null);
				if (value instanceof Block) {
					registerBlock((Block)value, itemClass);
				}
			} catch (Exception e) {
				FMLLog.log(Level.SEVERE, e, "Caught an exception during block registration");
				throw new LoaderException(e);
			}
		}
	}
	private static void registerBlock(Block block, Class<? extends ItemBlock> itemClass) {
		GameRegistry.registerBlock(block, itemClass);
	}
		
	public static void registerDeclaredTextures(Class catalogClass) {
		Field[] fields = catalogClass.getFields();
		for (Field field : fields) {
			try {
				Object value = field.get(null);
				if (value instanceof String) {
					registerTexture((String)value);
				}
			} catch (Exception e) {
				FMLLog.log(Level.SEVERE, e, "Caught an exception during texture preloading");
				throw new LoaderException(e);
			}
		}
	}
	private static void registerTexture(String texture) {
		MinecraftForgeClient.preloadTexture(texture);
	}
	
}
