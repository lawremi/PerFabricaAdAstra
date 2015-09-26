package org.pfaa.geologica.registration;

import org.pfaa.chemica.registration.FluidRegistrationUtils;
import org.pfaa.core.registration.RegistrationUtils;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.GeologicaItems;

public class ItemRegistration {
	public static void init() {
		FluidRegistrationUtils.registerFluidContainers(GeologicaBlocks.class);
		RegistrationUtils.registerDeclaredItems(GeologicaItems.class);
	}
}
