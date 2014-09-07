package org.pfaa;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;

import org.pfaa.chemica.ChemicaItems;
import org.pfaa.geologica.Geologica;
import org.pfaa.geologica.fluid.ColoredBucketItem;

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
					String name = fieldNameToUnlocalizedName(field.getName());
					block.setBlockName(name);
					GameRegistry.registerBlock(block, itemClass, name);
				}
			} catch (Exception e) {
				Geologica.log.fatal("Failed to register field '" + field.getName() + "' as block");
				throw new LoaderException(e);
			}
		}
	}

	public static void registerDeclaredItems(Class catalogClass) {
	    Field[] fields = catalogClass.getFields();
        for (Field field : fields) {
            try {
                Item item = (Item)field.get(null);
                String name = fieldNameToUnlocalizedName(field.getName());
                item.setUnlocalizedName(name);
                GameRegistry.registerItem(item, name);
            } catch (Exception e) {
                Geologica.log.fatal("Failed to register field '" + field.getName() + "' as item");
                throw new LoaderException(e);
            }
        }
	}
	
	private static String fieldNameToUnlocalizedName(String name) {
		return CaseFormat.UPPER_UNDERSCORE.
				      to(CaseFormat.LOWER_CAMEL, name.replaceAll("__", "."));
	}
	
	public static void registerContainersForDeclaredFluidBlocks(Class catalogClass) {
		Field[] fields = catalogClass.getFields();
		for (Field field : fields) {
			try {
				Object value = field.get(null);
				if (value instanceof BlockFluidBase) {
					BlockFluidBase block = (BlockFluidBase)value;
					if (!block.getFluid().isGaseous()) {
						registerBucketForFluid(block);
						registerFlaskForFluid(block.getFluid());
					}
				}
			} catch (Exception e) {
				Geologica.log.fatal("Failed to register bucket for fluid block '" + field.getName());
				throw new LoaderException(e);
			}
		}
	}

	private static void registerBucketForFluid(BlockFluidBase block) {
		String name = fieldNameToUnlocalizedName(block.getFluid().getName()) + "Bucket";
		Item item = new ColoredBucketItem(block);
		item.setUnlocalizedName(name);
		GameRegistry.registerItem(item, name);
		FluidContainerRegistry.registerFluidContainer(block.getFluid(), 
				new ItemStack(item), FluidContainerRegistry.EMPTY_BUCKET);
	}
	
	private static void registerFlaskForFluid(Fluid fluid) {
	    FluidContainerRegistry.registerFluidContainer(fluid, 
                new ItemStack(ChemicaItems.FILLED_GLASS_BOTTLE, fluid.getID()), FluidContainerRegistry.EMPTY_BOTTLE);
	}
}
