package org.pfaa.fabrica.registration;

import org.pfaa.core.registration.RegistrationUtils;
import org.pfaa.fabrica.FabricaBlocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class BlockRegistration {

	public static void init() {
		RegistrationUtils.registerDeclaredBlocks(FabricaBlocks.class, Block.class, ItemBlock.class);
	}

}
