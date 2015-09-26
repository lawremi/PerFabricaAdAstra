package org.pfaa.chemica.registration;

import java.lang.reflect.Field;

import org.pfaa.chemica.ChemicaItems;
import org.pfaa.chemica.fluid.ColoredBucketItem;
import org.pfaa.core.catalog.Catalog;
import org.pfaa.core.registration.RegistrationUtils;

import cpw.mods.fml.common.LoaderException;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.IFluidBlock;

public class FluidRegistrationUtils {
	public static void registerFluidContainers(Class<? extends Catalog> catalogClass) {
		Field[] fields = catalogClass.getFields();
		for (Field field : fields) {
			try {
				Object value = field.get(null);
				if (value instanceof IFluidBlock) {
					IFluidBlock block = (IFluidBlock)value;
					if (!block.getFluid().isGaseous()) {
						registerBucket(block);
						registerFlask(block.getFluid());
					}
				}
			} catch (Exception e) {
				throw new LoaderException(e);
			}
		}
	}
	
	private static void registerBucket(IFluidBlock block) {
		String name = RegistrationUtils.fieldNameToUnlocalizedName(block.getFluid().getName()) + "Bucket";
		Item item = new ColoredBucketItem((Block)block);
		item.setUnlocalizedName(name);
		GameRegistry.registerItem(item, name);
		FluidContainerRegistry.registerFluidContainer(block.getFluid(), 
				new ItemStack(item), FluidContainerRegistry.EMPTY_BUCKET);
	}
	
	private static void registerFlask(Fluid fluid) {
	    FluidContainerRegistry.registerFluidContainer(fluid, 
                new ItemStack(ChemicaItems.FILLED_GLASS_BOTTLE, 1, fluid.getID()), FluidContainerRegistry.EMPTY_BOTTLE);
	}
}
