package org.pfaa.chemica.integration;

public abstract class ModIntegration {

	public static void init() {
		VanillaIntegration.init();
		RailcraftIntegration.init();
		AppliedEnergistics2Integration.init();
		ThermalExpansionIntegration.init();
		EnderIOIntegration.init();
		ImmersiveEngineeringIntegration.init();
		// TODO: IndustrialCraft (macerator, ore washer, thermal centrifuge)
		BuildcraftAdditionsIntegration.init();
		TConstructIntegration.init();
		MagneticraftIntegration.init();
		ForestryIntegration.init();
		// 1.7.10: MaricultureIntegration.init();
		// TODO: Pneumaticraft: Reborn (refinery with 4 outputs)
		// TODO: 1.10+, Immersive Petroleum has a distillation tower
		// TODO: Nuclearcraft
		// TODO: Advanced Rocketry
		// TODO: Gregtech and addons like Gregicality, Shadows of Greg
	}
	
}
