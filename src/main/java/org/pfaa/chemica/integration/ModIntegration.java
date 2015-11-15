package org.pfaa.chemica.integration;

import cpw.mods.fml.common.Loader;

public abstract class ModIntegration {

	public static void init() {
		if (Loader.isModLoaded(ModIds.GREGTECH)) {
			return; // we let Greg run the show
		}
		VanillaIntegration.init();
		RailcraftIntegration.init();
		AppliedEnergistics2Integration.init();
		ThermalExpansionIntegration.init();
		EnderIOIntegration.init();
		ImmersiveEngineeringIntegration.init();
		RotaryCraftIntegration.init();
		BuildcraftAdditionsIntegration.init();
	}
	
}
