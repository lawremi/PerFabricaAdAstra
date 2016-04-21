package org.pfaa.fabrica.registration;

import org.pfaa.core.registration.RegistrationUtils;
import org.pfaa.fabrica.FabricaBlocks;

import net.minecraft.item.ItemBlock;

public class BlockRegistration {

	public static void init() {
		RegistrationUtils.registerBlock(FabricaBlocks.DRYWALL, ItemBlock.class, "drywall");
	}

}
