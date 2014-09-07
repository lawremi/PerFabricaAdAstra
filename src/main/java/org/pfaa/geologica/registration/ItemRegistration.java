package org.pfaa.geologica.registration;

import org.pfaa.RegistrationUtils;
import org.pfaa.geologica.GeologicaBlocks;

public class ItemRegistration {
	public static void init() {
		RegistrationUtils.registerContainersForDeclaredFluidBlocks(GeologicaBlocks.class);
	}
}
