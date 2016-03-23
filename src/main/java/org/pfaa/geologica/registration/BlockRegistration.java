package org.pfaa.geologica.registration;

import org.pfaa.chemica.block.IndustrialFluidBlock;
import org.pfaa.chemica.item.IndustrialBlockItem;
import org.pfaa.core.item.ColoredBlockItem;
import org.pfaa.core.item.CompositeBlockItem;
import org.pfaa.core.registration.RegistrationUtils;
import org.pfaa.geologica.Geologica;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.block.GeoBlock;
import org.pfaa.geologica.block.SlabBlock;
import org.pfaa.geologica.block.StairsBlock;
import org.pfaa.geologica.block.VanillaOreOverrideBlock;
import org.pfaa.geologica.block.WallBlock;
import org.pfaa.geologica.item.SlabItem;

import net.minecraft.item.ItemBlock;

public class BlockRegistration  {

	public static void init() {
		RegistrationUtils.registerDeclaredBlocks(GeologicaBlocks.class, GeoBlock.class, IndustrialBlockItem.class);
		RegistrationUtils.registerDeclaredBlocks(GeologicaBlocks.class, WallBlock.class, CompositeBlockItem.class);
		RegistrationUtils.registerDeclaredBlocks(GeologicaBlocks.class, SlabBlock.class, SlabItem.class);
		RegistrationUtils.registerDeclaredBlocks(GeologicaBlocks.class, StairsBlock.class, ItemBlock.class);
		RegistrationUtils.registerDeclaredBlocks(GeologicaBlocks.class, IndustrialFluidBlock.class, ColoredBlockItem.class);
		RegistrationUtils.registerBlock(GeologicaBlocks.SPRING, CompositeBlockItem.class, "spring");
		
		if (Geologica.getConfiguration().isVanillaOreOverrideEnabled()) {
			RegistrationUtils.registerDeclaredBlocks(GeologicaBlocks.class, VanillaOreOverrideBlock.class, ItemBlock.class);
		}
	}

}
