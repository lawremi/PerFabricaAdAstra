package org.pfaa.geologica.integration;

import org.pfaa.chemica.integration.ModIds;
import org.pfaa.chemica.registration.OreDictUtils;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.block.SlabBlock;
import org.pfaa.geologica.block.SpringBlock;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class BuildcraftIntegration {
	public static void init() {
		if (Loader.isModLoaded(ModIds.BUILDCRAFT_TRANSPORT)) {
			blacklistFacadeBlocks();
		}
	}

	private static void blacklistFacadeBlocks() {
		for (Block block : GeologicaBlocks.getBlocks()) {
			ItemStack itemStack = new ItemStack(block);
			if (!block.isOpaqueCube() || OreDictUtils.hasPrefix(itemStack, "ore") || 
					block instanceof SpringBlock || block instanceof SlabBlock) {
				FMLInterModComms.sendMessage(ModIds.BUILDCRAFT_TRANSPORT, "blacklist-facade", itemStack);
			}
		}
	}
}
