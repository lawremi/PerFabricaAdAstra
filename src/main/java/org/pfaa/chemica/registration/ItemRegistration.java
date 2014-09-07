package org.pfaa.chemica.registration;

import org.pfaa.RegistrationUtils;
import org.pfaa.chemica.ChemicaItems;

public class ItemRegistration {
    public static void init() {
        RegistrationUtils.registerDeclaredItems(ChemicaItems.class);
    }
}
