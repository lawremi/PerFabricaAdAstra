package org.pfaa.geologica.registration;

import net.minecraft.item.ItemBlock;

import org.pfaa.RegistrationUtils;
import org.pfaa.chemica.block.IndustrialFluidBlock;
import org.pfaa.geologica.Geologica;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.block.GeoBlock;
import org.pfaa.geologica.block.SlabBlock;
import org.pfaa.geologica.block.StairsBlock;
import org.pfaa.geologica.block.VanillaOreOverrideBlock;
import org.pfaa.geologica.block.WallBlock;
import org.pfaa.geologica.item.SlabItem;
import org.pfaa.item.CompositeBlockItem;

public class BlockRegistration  {

	public static void init() {
		RegistrationUtils.registerDeclaredBlocks(GeologicaBlocks.class, GeoBlock.class, CompositeBlockItem.class);
		RegistrationUtils.registerDeclaredBlocks(GeologicaBlocks.class, WallBlock.class, CompositeBlockItem.class);
		RegistrationUtils.registerDeclaredBlocks(GeologicaBlocks.class, SlabBlock.class, SlabItem.class);
		RegistrationUtils.registerDeclaredBlocks(GeologicaBlocks.class, StairsBlock.class, ItemBlock.class);
		RegistrationUtils.registerDeclaredBlocks(GeologicaBlocks.class, IndustrialFluidBlock.class, ItemBlock.class);
		
		if (Geologica.getConfiguration().isVanillaOreOverrideEnabled()) {
			RegistrationUtils.registerDeclaredBlocks(GeologicaBlocks.class, VanillaOreOverrideBlock.class, ItemBlock.class);
		}
	}

}
