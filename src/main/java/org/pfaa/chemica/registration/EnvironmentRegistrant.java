package org.pfaa.chemica.registration;

import org.pfaa.chemica.model.Mixture;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class EnvironmentRegistrant {
	public static EnvironmentRegistrant INSTANCE = new EnvironmentRegistrant();

	private EnvironmentRegistrant() {
	}
	
	private Mixture getAtmosphereForWorld(World world) {
		if (world.provider.dimensionId == 0) {
			return EnvironmentRegistry.OVERWORLD_ATMOSPHERE; 
		}
		return null;
	}

	private Mixture getOceanForWorld(World world) {
		if (world.provider.dimensionId == 0) {
			return EnvironmentRegistry.OVERWORLD_OCEAN; 
		}
		return null;
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		Mixture atmosphere = getAtmosphereForWorld(event.world);
		if (atmosphere != null) {
			EnvironmentRegistry.registerAtmosphere(event.world, atmosphere);
		}
		Mixture ocean = getOceanForWorld(event.world);
		if (ocean != null) {
			EnvironmentRegistry.registerOcean(event.world, ocean);
		}
	}

}
