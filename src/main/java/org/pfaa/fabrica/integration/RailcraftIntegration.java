package org.pfaa.fabrica.integration;

import org.pfaa.chemica.integration.ModIds;

import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class RailcraftIntegration {
	public static void init() {
		if (Loader.isModLoaded(ModIds.RAILCRAFT)) {
			oredictifyItems();
		}
	}

	private static void oredictifyItems() {
		OreDictionary.registerOre("concrete", new ItemStack(Block.getBlockFromName("Railcraft:cube"), 1, 1));
	}

}
