package org.pfaa.fabrica;

import org.pfaa.chemica.integration.ModIds;
import org.pfaa.core.catalog.BlockCatalog;
import org.pfaa.fabrica.block.DrywallBlock;
import org.pfaa.fabrica.block.FluidReactorBlock;
import org.pfaa.fabrica.block.HoodBlock;
import org.pfaa.fabrica.block.PaintableDrywallBlock;

import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;

public class FabricaBlocks implements BlockCatalog {
	public static final Block DRYWALL;
	public static final Block HOOD = new HoodBlock();
	public static final Block FLUID_REACTOR = new FluidReactorBlock();
	
	static {
		if (Loader.isModLoaded(ModIds.OPEN_BLOCKS)) { 
			DRYWALL = new PaintableDrywallBlock();
		} else {
			DRYWALL = new DrywallBlock();
		}
	}
}
