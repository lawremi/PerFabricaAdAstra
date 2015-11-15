package org.pfaa.chemica.registration;

import org.pfaa.chemica.ChemicaBlocks;
import org.pfaa.chemica.ChemicaItems;
import org.pfaa.core.registration.RegistrationUtils;

public class ItemRegistration {
    public static void init() {
    	FluidRegistrationUtils.registerFluidContainers(ChemicaBlocks.class);
        RegistrationUtils.registerDeclaredItems(ChemicaItems.class);
    }
}
