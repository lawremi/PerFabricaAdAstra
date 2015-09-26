package org.pfaa.chemica.integration;

public abstract class ModIntegration {

	public static void init() {
		VanillaIntegration.init();
		RailcraftIntegration.init();
	}
	
}
