package org.pfaa.fabrica.registration;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreMapping {
	public static void init() {
		map("itemSlag", "lumpSlag");
	}

	private static void map(String from, String to) {
		for (ItemStack fromStack : OreDictionary.getOres(from)) {
			OreDictionary.registerOre(to, fromStack);
		}
	}
}
