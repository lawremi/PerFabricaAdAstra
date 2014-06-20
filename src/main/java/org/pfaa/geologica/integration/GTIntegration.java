package org.pfaa.geologica.integration;

import org.pfaa.geologica.GeoMaterial.Strength;

import cpw.mods.fml.common.Loader;

public class GTIntegration {
	public static boolean isGregtechInstalled() {
		return Loader.isModLoaded("gregtech");
	}

	public static float getDefaultInitialStoneToolDamage(Strength strength) {
		float damage;
		switch(strength) {
		case WEAK:
			damage = 0.2F;
			break;
		case MEDIUM:
			damage = 0.1F;
			break;
		default:
			damage = 0F;
		}
		return damage;
	}
}
