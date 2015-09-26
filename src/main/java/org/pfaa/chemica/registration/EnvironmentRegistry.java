package org.pfaa.chemica.registration;

import java.util.HashMap;
import java.util.Map;

import org.pfaa.chemica.model.Compound.Compounds;
import org.pfaa.chemica.model.Element.Elements;
import org.pfaa.chemica.model.Mixture;
import org.pfaa.chemica.model.SimpleMixture;

import net.minecraft.world.World;

public abstract class EnvironmentRegistry {
	private static Map<World,Mixture> atmospheres = new HashMap<World,Mixture>();
	private static Map<World,Mixture> oceans = new HashMap<World,Mixture>();
	
	public static Mixture OVERWORLD_ATMOSPHERE = 
			new SimpleMixture(Compounds.N2, 0.78).mix(Compounds.O2,	0.21)
				.mix(Elements.Ar, 0.01).mix(Elements.Ne, 0.00182)
				.mix(Elements.He, 0.000524).mix(Compounds.METHANE, 0.00018)
				.mix(Elements.Kr, 0.000114);
	
	public static Mixture OVERWORLD_OCEAN = 
			new SimpleMixture(Compounds.H2O).mix(Compounds.NaCl, 0.03)
				.mix(Compounds.MgCl2_6H2O, 0.003).mix(Compounds.MgSO4_7H2O, 0.001)
				.mix(Compounds.CaSO4_2H2O, 0.001).mix(Compounds.KCl, 0.001)
				.mix(Compounds.NaBr, 0.0001);
	
	public static void registerAtmosphere(World world, Mixture atmosphere) {
		atmospheres.put(world, atmosphere);
	}
	
	public static void registerOcean(World world, Mixture atmosphere) {
		oceans.put(world, atmosphere);
	}
	
	public static Mixture getAtmosphere(World world) {
		Mixture atmosphere = atmospheres.get(world);
		if (atmosphere == null) {
			atmosphere = OVERWORLD_ATMOSPHERE;
		}
		return atmosphere;
	}
	
	public static Mixture getOcean(World world) {
		Mixture ocean = oceans.get(world);
		if (ocean == null) {
			ocean = OVERWORLD_OCEAN;
		}
		return ocean;
	}
}
