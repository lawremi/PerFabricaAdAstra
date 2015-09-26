package org.pfaa.chemica.registration;

import org.pfaa.chemica.ChemicaItems;
import org.pfaa.core.registration.RegistrationUtils;

public class ItemRegistration {
    public static void init() {
        RegistrationUtils.registerDeclaredItems(ChemicaItems.class);
    }
}
