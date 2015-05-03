package org.pfaa.geologica.registration;

import org.pfaa.RegistrationUtils;
import org.pfaa.geologica.GeologicaBlocks;
import org.pfaa.geologica.GeologicaItems;

public class ItemRegistration {
	public static void init() {
		RegistrationUtils.registerContainersForDeclaredFluidBlocks(GeologicaBlocks.class);
		RegistrationUtils.registerDeclaredItems(GeologicaItems.class);
	}
}
