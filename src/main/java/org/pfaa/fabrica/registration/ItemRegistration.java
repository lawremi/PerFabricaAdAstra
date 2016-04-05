package org.pfaa.fabrica.registration;

import org.pfaa.core.registration.RegistrationUtils;
import org.pfaa.fabrica.FabricaItems;

public class ItemRegistration {

	public static void init() {
		RegistrationUtils.registerDeclaredItems(FabricaItems.class);
	}

}
