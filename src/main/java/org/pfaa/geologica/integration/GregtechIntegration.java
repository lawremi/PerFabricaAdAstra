package org.pfaa.geologica.integration;

import org.pfaa.chemica.model.Strength;

public class GregtechIntegration {
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
