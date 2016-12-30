package org.pfaa.fabrica.registration;

import org.pfaa.chemica.item.IndustrialItems;
import org.pfaa.chemica.item.IndustrialMaterialItem;
import org.pfaa.chemica.item.MaterialStack;
import org.pfaa.chemica.model.IndustrialMaterial;
import org.pfaa.chemica.processing.Form.Forms;
import org.pfaa.chemica.registration.OreDictUtils;
import org.pfaa.fabrica.FabricaItems;
import org.pfaa.fabrica.model.Generic;
import org.pfaa.fabrica.model.Generic.Generics;

import net.minecraft.item.ItemStack;

public class OreRegistration {

	public static void init() {
		oreDictifyMaterialItems();
		oreDictifyGenerics();
	}

	private static void oreDictifyGenerics() {
		for (Generic generic : Generics.values()) {
			for (Forms form : Forms.values()) {
				for (IndustrialMaterial specific : generic.getSpecifics()) {
					for (ItemStack itemStack : IndustrialItems.getItemStacks(form, specific)) {
						OreDictUtils.register(new MaterialStack(form, generic), itemStack);
					}
				}
			}
		}
	}

	private static void oreDictifyMaterialItems() {
		for (IndustrialMaterialItem<?> item : FabricaItems.getIndustrialMaterialItems()) {
			OreDictUtils.register(item);
		}
	}
}
